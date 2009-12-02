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

import ring.nrapi.aggregate.AbstractResourceAggregate;
import ring.nrapi.aggregate.ResourceAggregate;
import ring.nrapi.aggregate.TestAggregate;
import ring.nrapi.movement.RoomAggregate;

public class ExistDBStore implements XMLDataStore {
	public static final String XML_RESOURCE = "XMLResource";
	
	//Collection mappings
	public static final String AGGREGATES_COLLECTION = "aggregate";
	public static final String RESOURCES_COLLECTION = "resource";
	
	@Override
	public TestAggregate retrieveTestAggregate(String id) {
		try {
			XMLResource agg = retrieveAggregate(id);
			System.out.println("Got aggregate: " + agg);
			JAXBContext ctx = JAXBContext.newInstance(TestAggregate.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			TestAggregate ta = (TestAggregate)um.unmarshal(agg.getContentAsDOM());
			System.out.println("Got an aggregate.");
			if (ta != null) {
				System.out.println("ID: " + ta.getID());
				ta.setStoreAsUpdate(true);
			}
			
			return ta;
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
	public boolean storeAggregate(ResourceAggregate agg) {
		if (agg.storeAsUpdate()) {
			return updateAggregate(agg);
		}
		else {
			return insertAggregate(agg);
		}
	}
	
	private XMLResource retrieveAggregate(String id) throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(AGGREGATES_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		String query = "for $doc in collection(\"" + AGGREGATES_COLLECTION+ "\")/aggregates/*[id=\"" + id + "\"] return $doc";
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
	
	private boolean insertAggregate(ResourceAggregate agg) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(AGGREGATES_COLLECTION);
			XQueryService xq = db.getXQueryService(col);
			String where = "collection(\"" + AGGREGATES_COLLECTION+ "\")/aggregates";
			String query = "for $doc in " + where + " return update insert " + agg.toXML() + " into " + where;
			System.out.println("Query: " + query);
			xq.query(query);
			return true;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean updateAggregate(ResourceAggregate agg) {
		ExistDB db = new ExistDB();
		try {
			Collection col = db.getCollection(AGGREGATES_COLLECTION);
			XQueryService xq = db.getXQueryService(col);
			String where = "collection(\"" + AGGREGATES_COLLECTION + "\")/aggregates/*[id=\"" + agg.getID() + "\"]";
			String query = "for $doc in " + where + " return update replace $doc with " + agg.toXML();
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

	@Override
	public RoomAggregate retrieveRoomAggregate(String id) {
		try {
			XMLResource agg = retrieveAggregate(id);
			JAXBContext ctx = JAXBContext.newInstance(RoomAggregate.class);
			Unmarshaller um = ctx.createUnmarshaller();
			RoomAggregate ra = (RoomAggregate)um.unmarshal(agg.getContentAsDOM());
			if (ra != null) {
				ra.setStoreAsUpdate(true);
			}
			
			return ra;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
		catch(JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
