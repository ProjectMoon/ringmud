package ring.deployer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.persistence.ExistDB;
import ring.persistence.ExistDBStore;
import ring.persistence.Loadpoint;
import ring.persistence.XQuery;
import ring.util.UserUtilities;

/**
 * Encapsulates the logic of deploying a given XML document.
 * @author projectmoon
 *
 */
public class XMLDeployer {
	private ExistDB db;
	private DeployableFileEntry entry;
	private String documentName;
	private String xmlInDocument; //for the imported doc
	private static boolean xmlUpdates = false;
	
	public XMLDeployer(ExistDB db, DeployableFileEntry entry) {
		this.db = db;
		this.entry = entry;
		documentName = entry.getStrippedEntryName();
	}
	
	public boolean xmlUpdates() {
		return xmlUpdates;
	}
	
	public void deploy() throws XMLDBException, SAXException, IOException {
		Collection col = db.getCollection(ExistDBStore.STATIC_COLLECTION);
		col.setProperty(OutputKeys.INDENT, "no");
		
		//Determine if this document needs to be updated:
		//Retrieve the document content of the same name from the database.
		//Retrieve the document content of the incoming document.
		//if shaHash(incomingDocument) == shaHash(documentInDB):
		//	reutrn. the document does not need to be updated.
		xmlInDocument = stripWhitespaceAndHeader(entry.getInputStream());
	
		XMLResource resource = (XMLResource)col.getResource(documentName);
		if (resource != null) {
			String xmlInDB = (String)resource.getContent(); //This is already stripped of its header/whitespace

			String hash1 = UserUtilities.sha1Hash(xmlInDB);
			String hash2 = UserUtilities.sha1Hash(xmlInDocument);
			
			if (hash1.equals(hash2)) {
				col.close();
				//System.out.println("This document does not need to be updated.");
				return;
			}
			else {
				System.out.println("Updating XML: " + documentName);
			}
		}
		else {
			System.out.println("Adding new XML: " + documentName);
		}
		col.close();
		
		//Now, we have determined that the document needs to be updated, or imported.
		xmlUpdates = true;
		//Next, we handle ID clashes.
		//COMMENTED OUT because clashing is handled by removing of existing documents, and removal of deleted documents.
		//handleClashes();

		//If a document with this name already exists in the DB:
		//	delete it.
		deleteExistingDocument();
		
		//Finally, import this document.
		importDocument();
	}
	
	/**
	 * Handles clashses. Currently not necessary.
	 * @throws SAXException
	 * @throws IOException
	 * @throws XMLDBException
	 */
	@SuppressWarnings("unused")
	private void handleClashes() throws SAXException, IOException, XMLDBException {
		//First, all clashes need to be handled.
		//For each ID found in this XML document:
		//	Atempt to retrieve an object from the database with this ID.
		//	If object returned is not null:
		//		boolean yes = idHasOwnDocument(id)
		//		if yes to ID has its own document:
		//			Delete that document.
		//		else:
		//			Replace definition for ID with a referential definition.
		
		//Find all IDs in the document.
		IDFinder finder = new IDFinder();
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(finder);
		InputStream input = entry.getInputStream();
		InputSource src = new InputSource(new BufferedInputStream(input));
		parser.parse(src);
		
		//For each found ID, attempt to retrieve an object from the database.
		//If an object is found, that means we have a clash.
		//We need to handle each clash individually.
		XQuery xq = new XQuery();
		xq.setLoadpoint(Loadpoint.STATIC);
		
		
		for (String id : finder.getIDs()) {
			String query = "for $doc in //ring//*[@id=\"" + id + "\"] return $doc";
			xq.setQuery(query);
			
			List<XMLResource> set = xq.execute();
			
			//If size > 0, we have a clash.
			if (set.size() > 0) {
				//There should be one resource.
				//All IDs are unique per collection. This is enforced by compiler.
				assert (set.size() == 1);
				XMLResource res = set.get(0);
				handleClashForResource(id, res);
			}
		}
	}
	
	/**
	 * Handles a clash for a given resource found in the DB. This clash is handled in one of two ways:
	 * if the resource is determined to be its own document as per the idHasOwnDocument method, then it
	 * is outright deleted. If the resource is determined to be embedded in a larger document, it
	 * is instead replaced with a referential definition. This ensures that if an object definition is
	 * refactored out of an xml file it is embedded into, that existing data will be updated. 
	 * @param id
	 * @param res
	 * @throws XMLDBException
	 */
	private void handleClashForResource(String id, XMLResource res) throws XMLDBException {
		System.out.println("handling clash for: " + id); 
		//Determine if this resource is its own document.
		//If yes, delete that document.
		//Else, replace with a referential definition.
		if (idHasOwnDocument(id, res)) {
			System.out.println("has own document");
			//eXist cannot figure out how to find the parent collection of these fragments properly.
			//So, we have to do it slightly differently.
			Collection parent = db.getCollection(ExistDBStore.STATIC_COLLECTION);
			Resource removeIt = parent.getResource(res.getDocumentId());
			
			if (removeIt != null) {
				parent.removeResource(removeIt);
			}
			
			parent.close();
		}
		else {
			String delete = "for $doc in //ring//*[@id=\"" + id + "\"] return (update delete $doc/*)";
			String update = "for $doc in //ring//*[@id=\"" + id + "\"] return update value $doc/@ref with \"true\"";
			XQuery xq = new XQuery();
			xq.setQuery(delete);
			xq.executeUpdate();
			xq.setQuery(update);
			xq.executeUpdate();
		}
		
	}
	
	private boolean idHasOwnDocument(String id, XMLResource res) throws XMLDBException {
		//In order for an ID to be considered to have its own document, the following must be true:
		//1. The document has only one element within the ring element. This element may have any # of subelements.
		//2. The element must have an id that matches the id passed to this method.
		Collection parent = db.getCollection(ExistDBStore.STATIC_COLLECTION);
		XMLResource actualDocument = (XMLResource)parent.getResource(res.getDocumentId());
		boolean hasOwnDocument = true;
		
		Element ringRoot = (Element)actualDocument.getContentAsDOM().getChildNodes().item(0);
		NodeList children = ringRoot.getChildNodes();
		
		int elements = 0;
		int elementIndex = 0; //This is used if if we meet requirement #1.
		
		for (int c = 0; c < children.getLength(); c++) {
			if (children.item(c).getNodeType() == Node.ELEMENT_NODE) {
				elements++;
				elementIndex = c;
			}
		}
		
		//Fulfills requirement #1.
		if (elements != 1) {
			hasOwnDocument = false;
		}
		else {
			//Fulfills requirement #1.
			Element e = (Element)children.item(elementIndex);
			if (e.getAttribute("id").equalsIgnoreCase(id) == false) {
				System.out.println("Doesn't meet requirement #2");
				hasOwnDocument = false;
			}
		}
		
		parent.close();
		//If one of these conditions is not met, it is considered embedded in another document.
		return hasOwnDocument;
	}
	
	private void deleteExistingDocument() throws XMLDBException {
		Collection col = db.getCollection(ExistDBStore.STATIC_COLLECTION);
		XMLResource res = (XMLResource)col.getResource(documentName);
		
		if (res != null) {
			col.removeResource(res);
		}
		
		col.close();
	}
	
	private void importDocument() throws XMLDBException {
		Collection col = db.getCollection(ExistDBStore.STATIC_COLLECTION);
		XMLResource res = (XMLResource)col.createResource(documentName, XMLResource.RESOURCE_TYPE);
		
		res.setContent(xmlInDocument);
		col.storeResource(res);
			
		col.close();
	}
	
	private String stripWhitespaceAndHeader(InputStream xml) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			InputStream input = this.getClass().getClassLoader().getResourceAsStream("ring/deployer/strip-space.xsl");
			Transformer transformer = tf.newTransformer(new StreamSource(input));
			
			StreamSource src = new StreamSource(xml);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(stream);  
	
			transformer.transform(src, result);
			return new String(stream.toByteArray());
		}
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		catch (TransformerException e) {
			e.printStackTrace();
			return null;
			
		}
	}
}
