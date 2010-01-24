package ring.persistence;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
		
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Player.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Player p = (Player)um.unmarshal(res.getContentAsDOM());
				
				if (p != null) {
					p.setStoreAsUpdate(true);
					initializeBusinessObject(p, res.getDocumentId());
				}
				
				return p;
			}
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
		
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(PlayerCharacter.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				PlayerCharacter p = (PlayerCharacter)um.unmarshal(res.getContentAsDOM());
				
				if (p != null) {
					p.setStoreAsUpdate(true);
					initializeBusinessObject(p, res.getDocumentId());
				}
				
				return p;
			}
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
			setLoadpoint(prevLoadpoint);
		}
		
		//Nothing to return
		return null;
	}
	
	@Override
	public Room retrieveRoom(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Room.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Room r = (Room)um.unmarshal(res.getContentAsDOM());
				
				if (r != null) {
					r.setStoreAsUpdate(true);
					initializeBusinessObject(r, res.getDocumentId());
				}
				
				return r;
			}
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
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Entity e = (Entity)um.unmarshal(res.getContentAsDOM());
				
				if (e != null) {
					e.setStoreAsUpdate(true);
					initializeBusinessObject(e, res.getDocumentId());
				}
				
				return e;
			}
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
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Zone z = (Zone)um.unmarshal(res.getContentAsDOM());
				
				if (z != null) {
					z.setStoreAsUpdate(true);
					initializeBusinessObject(z, res.getDocumentId());
				}
				
				return z;
			}
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
	public Item retrieveItem(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Item.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Item i = (Item)um.unmarshal(res.getContentAsDOM());
				
				if (i != null) {
					i.setStoreAsUpdate(true);
					initializeBusinessObject(i, res.getDocumentId());
				}
				
				return i;
			}
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
	public Mobile retrieveMobile(String id) {
		try {
			XMLResource res = retrieveResource(id);
			JAXBContext ctx = JAXBContext.newInstance(Mobile.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setListener(new ReferenceLoader());
			
			if (res != null) {
				Mobile m = (Mobile)um.unmarshal(res.getContentAsDOM());
				
				if (m != null) {
					m.setStoreAsUpdate(true);
					initializeBusinessObject(m, res.getDocumentId());
				}
				
				return m;
			}
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
			
			//TODO implement update lock checking: all parts of a document must be ready for update. Could be good use of AspectJ?
			Collection col = null;
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
	}
	
	/**
	 * Initialize expressions for retrieving from static and game.
	 * @throws XMLDBException
	 */
	private void initializeRetrievalExpressions() throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(STATIC_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		
		//Needs to be declared in order to compile.
		xq.declareVariable("id", "");
		
		String query = "for $doc in collection(\"" + STATIC_COLLECTION + "\")/ring/*[@id=$id] return $doc";
		staticRetrieve = xq.compile(query);
		
		query = "for $doc in collection(\"" + GAME_COLLECTION + "\")/ring/*[@id=$id] return $doc";
		col = db.getCollection(GAME_COLLECTION);
		xq = db.getXQueryService(col);
		xq.declareVariable("id", "");
		gameRetrieve = xq.compile(query);
		
		query = "for $doc in collection(\"" + PLAYERS_COLLECTION + "\")/ring/*[@id=$id] return $doc";
		col = db.getCollection(PLAYERS_COLLECTION);
		xq = db.getXQueryService(col);
		xq.declareVariable("id", "");
		playersRetrieve = xq.compile(query);
	}
	
	private XMLResource retrieveResource(String id) throws XMLDBException {
		//First initialize expressions if necessary.
		if (gameRetrieve == null || staticRetrieve == null || playersRetrieve == null) {
			initializeRetrievalExpressions();
		}
		
		//Now, load the resource from the proper place based
		//on configured "loadpoint."
		if (loadpoint == Loadpoint.DEFAULT) {
			return defaultLoadMethod(id);
		}
		else if (loadpoint == Loadpoint.GAME) {
			return loadFromGame(id);
		}
		else if (loadpoint == Loadpoint.STATIC) {
			return loadFromStatic(id);
		}
		else if (loadpoint == Loadpoint.PLAYERS) {
			return loadFromPlayers(id);
		}
		//else, default to defaultLoadMethod
		else {
			return defaultLoadMethod(id);
		}
	}
	
	private XMLResource defaultLoadMethod(String id) throws XMLDBException {
		XMLResource res = loadFromGame(id);
		if (res == null) {
			System.out.println("Falling back to static for " + id);
			res = loadFromStatic(id);
		}
		
		return res;		
	}
	
	private XMLResource loadFromGame(String id) throws XMLDBException {
		//First try game collection, then static.
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(GAME_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		xq.declareVariable("id", id);
		ResourceSet resources = xq.execute(gameRetrieve);
		
		if (resources.getSize() > 0) {
			//TODO log duplicate resources?
			return (XMLResource)resources.getIterator().nextResource();
		}
		else {
			return null;
		}
	}
	
	private XMLResource loadFromStatic(String id) throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(STATIC_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		xq.declareVariable("id", id);
		ResourceSet resources = xq.execute(staticRetrieve);
		
		if (resources.getSize() > 0) {
			//TODO log duplicate resources?
			return (XMLResource)resources.getIterator().nextResource();
		}
		else {
			return null;
		}
	}
	
	private XMLResource loadFromPlayers(String id) throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(PLAYERS_COLLECTION);
		XQueryService xq = db.getXQueryService(col);
		xq.declareVariable("id", id);
		ResourceSet resources = xq.execute(playersRetrieve);
		
		if (resources.getSize() > 0) {
			//TODO log duplicate resources?
			return (XMLResource)resources.getIterator().nextResource();
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

	@Override
	public Loadpoint getLoadpoint() {
		return loadpoint;
	}

	@Override
	public void setLoadpoint(Loadpoint point) {
		loadpoint = point;
	}
}
