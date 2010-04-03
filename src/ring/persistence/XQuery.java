package ring.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.movement.Zone;
import ring.nrapi.business.AbstractBusinessObject;

/**
 * Class for running XQuery against the XML database and
 * converting the results into an arbitrary object.
 * @author projectmoon
 *
 */
public class XQuery {
	private String query;
	private Loadpoint loadpoint = Loadpoint.STATIC;
	
	public XQuery() {}
	
	public XQuery(String query) {
		this.query = query;
	}
	
	public XQuery(File xqFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(xqFile));
		String line = "";
		query = "";
			
		while ((line = reader.readLine()) != null) {
			query += line + "\n";
		}
	}
	
	public XQuery(InputStream xqStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(xqStream));
		String line = "";
		query = "";
			
		while ((line = reader.readLine()) != null) {
			query += line + "\n";
		}		
	}
	
	public XQuery(Loadpoint loadpoint, String query) {
		this.loadpoint = loadpoint;
		this.query = query;
	}
	
	public void setLoadpoint(Loadpoint point) {
		loadpoint = point;
	}
	
	public Loadpoint getLoadpoint() {
		return loadpoint;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	
	public void executeUpdate() throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = getCollection(db);
		db.query(col, getQuery());
		col.close();
	}
	
	/**
	 * Queries the database and returns a ResourceList of Resources that were found.
	 * This ResourceList can be iterated over with a foreach loop, unlike the regular
	 * ResourceSet. It also provides a close() method for when operations on the resources
	 * are complete. Make sure to always call close() on the ResourceList.
	 * @return
	 * @throws XMLDBException
	 */
	public ResourceList execute() throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = getCollection(db);
		ResourceSet xmlDocs = db.query(col, getQuery());
		return new ResourceList(col, xmlDocs);
	}
	
	public <T extends AbstractBusinessObject> List<T> execute(Class<T> cl) throws XMLDBException, JAXBException {
		if (getLoadpoint() == Loadpoint.DEFAULT) {
			return loadFromDefault(cl);
		}
		else {
			ExistDB db = new ExistDB();
			Collection col = getCollection(db);
			return loadFromCollection(cl, db, col);
		}
	}
	
	private <T extends AbstractBusinessObject> List<T> loadFromDefault(Class<T> cl) throws XMLDBException, JAXBException {
		ExistDB db = new ExistDB();
		
		Collection col = db.getCollection(ExistDBStore.GAME_COLLECTION);
		List<T> results = loadFromCollection(cl, db, col);
		
		if (results.size() > 0) {
			return results;
		}
		else {
			//Fall back to static
			col.close(); //release previous collection
			col = db.getCollection(ExistDBStore.STATIC_COLLECTION);
			return loadFromCollection(cl, db, col);
		}
	}
	
	private <T extends AbstractBusinessObject> List<T> loadFromCollection(Class<T> cl, ExistDB db, Collection col) throws XMLDBException, JAXBException {
		ResourceSet xmlDocs = db.query(col, getQuery());
		List<T> results = new ArrayList<T>((int)xmlDocs.getSize());
		
		ResourceIterator iter = xmlDocs.getIterator();
		while (iter.hasMoreResources()) {
			XMLResource res = (XMLResource)iter.nextResource();
			T conv = convertToObject(res, cl);
			results.add(conv);
		}
		
		col.close();
		
		return results;
	}
	
	private Collection getCollection(ExistDB db) throws XMLDBException {
		if (loadpoint == Loadpoint.GAME) {
			return db.getCollection(ExistDBStore.GAME_COLLECTION);
		}
		else if (loadpoint == Loadpoint.STATIC){
			return db.getCollection(ExistDBStore.STATIC_COLLECTION);
		}
		else if (loadpoint == Loadpoint.PLAYERS) {
			return db.getCollection(ExistDBStore.PLAYERS_COLLECTION);
		}
		else {
			throw new UnsupportedOperationException();
		}
	}
	
	private <T extends AbstractBusinessObject> T convertToObject(XMLResource res, Class<T> cl) throws JAXBException, XMLDBException {
		JAXBContext ctx = JAXBContext.newInstance(cl);
		Unmarshaller um = ctx.createUnmarshaller();
		um.setListener(new ReferenceLoader());
		Node node = res.getContentAsDOM();
		T conv = (T)um.unmarshal(node);
		
		conv.setStoreAsUpdate(true);
		conv.setDocumentID(res.getDocumentId());
		conv.createChildRelationships();
		
		return conv;
	}
}
