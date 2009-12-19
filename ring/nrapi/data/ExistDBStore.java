package ring.nrapi.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.entities.Entity;
import ring.nrapi.movement.Room;
import ring.nrapi.movement.Zone;

public class ExistDBStore implements DataStore {
	//XMLDB mappings
	public static final String XML_RESOURCE = "XMLResource";
	
	//Collection mappings
	public static final String STATIC_COLLECTION = "static";
	public static final String GAME_COLLECTION = "game";
	
	//Compiled expressions
	private static CompiledExpression retrieveExpression;
	
	private void initializeBusinessObject(AbstractBusinessObject bo, String docID) {
		bo.setDocumentID(docID);
		bo.createChildRelationships();
	}
	
	@Override
	public Room retrieveRoom(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Room.class);
			Unmarshaller um = ctx.createUnmarshaller();
			Room r = (Room)um.unmarshal(res.getContentAsDOM());
			
			if (r != null) {
				r.setStoreAsUpdate(true);
				initializeBusinessObject(r, res.getDocumentId());
			}
			
			return r;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Entity retrieveEntity(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Entity.class);
			Unmarshaller um = ctx.createUnmarshaller();
			Entity e = (Entity)um.unmarshal(res.getContentAsDOM());
			
			if (e != null) {
				e.setStoreAsUpdate(true);
				initializeBusinessObject(e, res.getDocumentId());
			}
			
			return e;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Zone retrieveZone(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Zone.class);
			Unmarshaller um = ctx.createUnmarshaller();
			Zone z = (Zone)um.unmarshal(res.getContentAsDOM());
			
			if (z != null) {
				z.setStoreAsUpdate(true);
				initializeBusinessObject(z, res.getDocumentId());
			}
			
			return z;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Nothing to return
		return null;
	}

	@Override
	/**
	 * Updates the parent document this Persistable is tied to.
	 * Due to the nature of this method, other changes to the
	 * parent document will be committed as well.
	 */
	public boolean storePersistable(Persistable p) {
		ExistDB db = new ExistDB();
		try {
			if (p.getDocumentID() == null) {
				p.setDocumentID(p.getID() + ".xml");
			}
			
			Collection col = db.getCollection(STATIC_COLLECTION);
			
			//Find the existing document, or create a new one.
			XMLResource doc = (XMLResource)col.getResource(p.getDocumentID());
			if (doc == null) {
				doc = createXMLResource(col, p.getDocumentID());
			}
			
			//Now update the document's content with all of the
			//info that the root of p has.
			doc.setContent(p.getRoot().toXMLDocument());
			col.storeResource(doc);
			return true;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void initializeRetrievalExpression() throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(STATIC_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		
		//Needs to be declared in order to compile.
		xq.declareVariable("id", "");
		
		String query = "for $doc in collection(\"" + STATIC_COLLECTION + "\")/ring/*[@id=$id] return $doc";
		retrieveExpression = xq.compile(query);		
	}
	
	private XMLResource retrieveResource(String id) throws XMLDBException {
		if (retrieveExpression == null) {
			initializeRetrievalExpression();
		}
		
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(STATIC_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		xq.declareVariable("id", id);
		ResourceSet resources = xq.execute(retrieveExpression);
		
		if (resources.getSize() > 0) {
			if (resources.getSize() > 1) {
				System.err.println("Warning: there are duplicate resources for " + id + ". Returning the first.");
			}
			
			ResourceIterator i = resources.getIterator();
			return (XMLResource)i.nextResource();
		}
		else {
			return null;
		}
	}
	
	private XMLResource createXMLResource(Collection col, String id) throws XMLDBException {
		return (XMLResource) col.createResource(id, XML_RESOURCE);
	}

	@Override
	public boolean importDocument(File file) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(STATIC_COLLECTION);
			String name = file.getName();
			
			if (!name.endsWith(".xml")) name += ".xml";

			XMLResource res = createXMLResource(col, name);
			
			res.setContent(file);
			col.storeResource(res);
			return true;
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean removeDocument(String docID) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(STATIC_COLLECTION);
			XMLResource res = (XMLResource)col.getResource(docID);
			if (res != null) {
				col.removeResource(res);
				return true;
			}
			else {
				return false;
			}
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
}
