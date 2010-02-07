package ring.deployer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
	
	public XMLDeployer(ExistDB db, DeployableFileEntry entry) {
		this.db = db;
		this.entry = entry;
		documentName = DeployModule.stripEntryPrefix(entry.getEntryName());
	}
	
	public void deploy() throws XMLDBException, SAXException, IOException {
		Collection col = db.getCollection(ExistDBStore.STATIC_COLLECTION);
		
		//Determine if this document needs to be updated:
		//Retrieve the document content of the same name from the database.
		//Retrieve the document content of the incoming document.
		//if shaHash(incomingDocument) == shaHash(documentInDB):
		//	reutrn. the document does not need to be updated.
		xmlInDocument = DeployModule.getContent(entry);
		XMLResource resource = (XMLResource)col.getResource(documentName);
		if (resource != null) {
			String xmlInDB = (String)resource.getContent();
			String hash1 = UserUtilities.sha1Hash(xmlInDB);
			String hash2 = UserUtilities.sha1Hash(xmlInDocument);
			System.out.println("hash1: " + hash1);
			System.out.println("hash2: " + hash2);
			if (UserUtilities.sha1Hash(xmlInDB).equals(UserUtilities.sha1Hash(xmlInDocument))) {
				col.close();
				System.out.println("This document does not need to be updated.");
				return;
			}
		}
		col.close();
		
		//Now, we have determined that the document needs to be updated.
		
		//Next, we handle ID clashes.
		handleClashes();

		//If a document with this name already exists in the DB:
		//	delete it.
		deleteExistingDocument();
		
		//Finally, import this document.
		importDocument();
	}
	
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
			parent.removeResource(removeIt);
			parent.close();
		}
		else {
			System.out.println("Would replace content with referential, but not implemented yet!");
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
}
