package ring.persistence;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import ring.nrapi.business.AbstractBusinessObject;
import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.movement.Zone;
import ring.players.Player;
import ring.players.PlayerCharacter;

public class ExistDBStore implements DataStore {
	//XMLDB mappings
	public static final String XML_RESOURCE_TYPE = "XMLResource";
	
	//Collection mappings
	public static final String STATIC_COLLECTION = "static";
	public static final String GAME_COLLECTION = "game";
	public static final String PLAYERS_COLLECTION = "players";
	
	//Compiled expressions
	private static CompiledExpression staticRetrieve;
	private static CompiledExpression gameRetrieve;
	private static CompiledExpression playersRetrieve;
	
	//Loadpoint: Whether to do default load method, load explictly from game,
	//or explicitly from static.
	private Loadpoint loadpoint;
	
	//DBTuple: Stores Collection and XMLResource, so that collections may be
	//closed after converting the object.
	private class DBTuple {
		public Collection collection;
		public XMLResource resource;
	}
	
	private void initializeBusinessObject(AbstractBusinessObject bo, String docID) {
		bo.setDocumentID(docID);
		bo.createChildRelationships();
	}

	@Override
	/**
	 * Explicitly pulls from the players collection. 
	 */
	public Player retrievePlayer(String id) {
		Loadpoint prevLoadpoint = getLoadpoint();
		setLoadpoint(Loadpoint.PLAYERS);
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Player p = convertToObject(tuple.resource, Player.class);
			return p;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
			setLoadpoint(prevLoadpoint);
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public PlayerCharacter retrievePlayerCharacter(String id) {
		//Explicitly pull from players collection.
		Loadpoint prevLoadpoint = getLoadpoint();
		setLoadpoint(Loadpoint.PLAYERS);
		
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			PlayerCharacter p = convertToObject(tuple.resource, PlayerCharacter.class);
			return p;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
			setLoadpoint(prevLoadpoint);
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Room retrieveRoom(String id) {
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Room r = convertToObject(tuple.resource, Room.class);
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
		finally {
			if (tuple != null && tuple.collection != null) {			
				closeConnection(tuple.collection);
			}
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Entity retrieveEntity(String id) {
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Entity e = convertToObject(tuple.resource, Entity.class);
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
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Zone retrieveZone(String id) {
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Zone z = convertToObject(tuple.resource, Zone.class);
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
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Item retrieveItem(String id) {
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Item i = convertToObject(tuple.resource, Item.class);
			return i;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Mobile retrieveMobile(String id) {
		DBTuple tuple = null;
		
		try {
			tuple = retrieveResource(id);
			Mobile m = convertToObject(tuple.resource, Mobile.class);
			return m;
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (tuple != null && tuple.collection != null) {
				closeConnection(tuple.collection);
			}
		}
		
		//Nothing to return
		return null;
	}
	
	private void closeConnection(Collection col) {
		assert(col != null);
		
		try {
			col.close();
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
	}
	
	private <T extends AbstractBusinessObject> T convertToObject(XMLResource res, Class<T> cl) throws JAXBException, XMLDBException {
		if (res == null) {
			return null;
		}
		
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

	@Override
	/**
	 * Updates the parent document this Persistable is tied to.
	 * Due to the nature of this method, other changes to the
	 * parent document will be committed as well.
	 */
	public boolean storePersistable(Persistable p) {		
		ExistDB db = new ExistDB();
		Collection col = null;
		
		try {
			if (p.getDocumentID() == null) {
				p.setDocumentID(p.getID() + ".xml");
			}
			
			//TODO implement update lock checking: all parts of a document must be ready for update. Could be good use of AspectJ?
			if (p instanceof Player || p instanceof PlayerCharacter) {
				col = db.getCollection(PLAYERS_COLLECTION);
			}
			else {
				col = db.getCollection(GAME_COLLECTION);
			}
			
			assert(col != null);
			
			//Find the existing document, or create a new one.
			XMLResource doc = (XMLResource)col.getResource(p.getDocumentID());
			if (doc == null) {
				doc = createXMLResource(col, p.getDocumentID());
			}
			
			//Now update the document's content with all of the
			//info that the root of p has.f
			doc.setContent(p.getRoot().toXMLDocument());
			col.storeResource(doc);
			return true;
		}
		catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if (col != null) {
				closeConnection(col);
			}
		}
	}
		
	/**
	 * Gets a retrieval expression for a given collection.
	 * @throws XMLDBException
	 */
	private CompiledExpression getRetrievalExpression(Collection col, XQueryService xq) throws XMLDBException {
		ExistDB db = new ExistDB();

		//Needs to be declared in order to compile.
		xq.declareVariable("id", "");
		
		String query = "for $doc in collection(\"" + col.getName() + "\")/ring//*[@id=$id] return $doc";
		return xq.compile(query);
	}
	
	private DBTuple retrieveResource(String id) throws XMLDBException {
		//Now, load the resource from the proper place based
		//on configured "loadpoint."
		switch (getLoadpoint()) {
			case DEFAULT:
				return defaultLoadMethod(id);
			case GAME:
				return loadFromGame(id);
			case PLAYERS:
				return loadFromPlayers(id);
			case STATIC:
				return loadFromStatic(id);
			default:
				return defaultLoadMethod(id);
		}
	}
	
	private DBTuple defaultLoadMethod(String id) throws XMLDBException {
		DBTuple res = loadFromGame(id);
		if (res == null) {
			System.out.println("Falling back to static for " + id);
			res = loadFromStatic(id);
		}
		
		return res;		
	}
	
	private DBTuple loadFromGame(String id) throws XMLDBException {
		return load(GAME_COLLECTION, id);
	}
	
	private DBTuple loadFromStatic(String id) throws XMLDBException {
		return load(STATIC_COLLECTION, id);
	}
	
	private DBTuple loadFromPlayers(String id) throws XMLDBException {
		return load(PLAYERS_COLLECTION, id);
	}
	
	private DBTuple load(String collectionName, String id) throws XMLDBException {
		//First try game collection, then static.
		ExistDB db = new ExistDB();
		DBTuple tuple = new DBTuple();
		Collection col = db.getCollection(collectionName);
		tuple.collection = col;
	
		XQueryService xq = db.getXQueryService(col);	
		CompiledExpression expr = getRetrievalExpression(col, xq);
	
		xq.declareVariable("id", id);
		ResourceSet resources = xq.execute(expr);
		
		if (resources.getSize() > 0) {
			//TODO log duplicate resources?
			tuple.resource = (XMLResource)resources.getIterator().nextResource();
			return tuple;
		}
		else {
			return null;
		}
	}
	private XMLResource createXMLResource(Collection col, String id) throws XMLDBException {
		return (XMLResource) col.createResource(id, XML_RESOURCE_TYPE);
	}

	@Override
	public boolean importDocument(File file) {
		ExistDB db = new ExistDB();
		Collection col = null;
		try {
			col = db.getCollection(STATIC_COLLECTION);
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
		finally {
			if (col != null) {
				closeConnection(col);
			}
		}
	}
	
	@Override
	public boolean removeDocument(String docID) {
		ExistDB db = new ExistDB();
		Collection col = null;
		try {
			col = db.getCollection(STATIC_COLLECTION);
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
		finally {
			if (col != null) {
				closeConnection(col);
			}
		}
	}

	@Override
	public Loadpoint getLoadpoint() {
		if (loadpoint != null) {
			return loadpoint;
		}
		else {
			return Loadpoint.DEFAULT;
		}
	}

	@Override
	public void setLoadpoint(Loadpoint point) {
		loadpoint = point;
	}
}
