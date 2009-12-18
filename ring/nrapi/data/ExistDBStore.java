package ring.nrapi.data;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import ring.nrapi.entities.Entity;
import ring.nrapi.movement.Room;
import ring.nrapi.movement.Zone;

public class ExistDBStore implements DataStore {
	public static final String XML_RESOURCE = "XMLResource";
	
	//Collection mappings
	public static final String STATIC_COLLECTION = "static";
	public static final String GAME_COLLECTION = "game";
	
	@Override
	public Room retrieveRoom(String id) {
		try {
			XMLResource res = retrieveResource(id);
			System.out.println("Got aggregate: " + res);
			JAXBContext ctx = JAXBContext.newInstance(Room.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			Room r = (Room)um.unmarshal(res.getContentAsDOM());
			System.out.println("Got an aggregate.");
			if (r != null) {
				System.out.println("ID: " + r.getID());
				r.setStoreAsUpdate(true);
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
			System.out.println("Got aggregate: " + res);
			JAXBContext ctx = JAXBContext.newInstance(Entity.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			Entity e = (Entity)um.unmarshal(res.getContentAsDOM());
			System.out.println("Got an aggregate.");
			if (e != null) {
				System.out.println("ID: " + e.getID());
				e.setStoreAsUpdate(true);
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
			System.out.println("Got aggregate: " + res);
			JAXBContext ctx = JAXBContext.newInstance(Zone.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			Zone z = (Zone)um.unmarshal(res.getContentAsDOM());
			System.out.println("Got an aggregate.");
			if (z != null) {
				System.out.println("ID: " + z.getID());
				z.setStoreAsUpdate(true);
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
	public boolean storePersistable(Persistable p) {
		if (p.storeAsUpdate()) {
			return updatePersistable(p);
		}
		else {
			return insertPersistable(p);
		}
	}
	
	private XMLResource retrieveResource(String id) throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(STATIC_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		String query = "for $doc in collection(\"" + STATIC_COLLECTION + "\")/ring/*[id=\"" + id + "\"] return $doc";
		ResourceSet resources = xq.query(query);
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
	
	private boolean insertPersistable(Persistable p) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(STATIC_COLLECTION);
			XQueryService xq = db.getXQueryService(col);
			String where = "collection(\"" + STATIC_COLLECTION+ "\")/ring";
			String query = "for $doc in " + where + " return update insert " + p.toXML() + " into " + where;
			System.out.println("Query: " + query);
			xq.query(query);
			return true;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean updatePersistable(Persistable p) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(STATIC_COLLECTION);
			XQueryService xq = db.getXQueryService(col);
			String where = "collection(\"" + STATIC_COLLECTION + "\")/ring/*[id=\"" + p.getID() + "\"]";
			String query = "for $doc in " + where + " return update replace $doc with " + p.toXML();
			System.out.println("xq:" + query);
			xq.query(query);
			return true;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public boolean storeDocument(String collectionName, Persistable doc) {
		System.out.println("in store document");
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(collectionName);
			XMLResource res = createXMLResource(col);
			
			//Normally wants a File, but autoconverts to String otherwise
			//TODO investigate better way to do this
			res.setContent(doc.toXML());
			col.storeResource(res);
			return true;
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private XMLResource createXMLResource(Collection col) throws XMLDBException {
		return (XMLResource) col.createResource(null, XML_RESOURCE);
	}
}
