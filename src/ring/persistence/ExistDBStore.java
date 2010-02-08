package ring.persistence;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.movement.Zone;
import ring.nrapi.business.AbstractBusinessObject;
import ring.players.Player;
import ring.players.PlayerCharacter;

public class ExistDBStore implements DataStore {
	//XMLDB mappings
	public static final String XML_RESOURCE_TYPE = "XMLResource";
	
	//Collection mappings
	public static final String STATIC_COLLECTION = "static";
	public static final String GAME_COLLECTION = "game";
	public static final String PLAYERS_COLLECTION = "players";
	
	//Loadpoint: Whether to do default load method, load explictly from game,
	//or explicitly from static.
	private Loadpoint loadpoint;
	
	@Override
	/**
	 * Explicitly pulls from the players collection. 
	 */
	public Player retrievePlayer(String id) {
		try {
			return retrieveResource(id, Player.class, Loadpoint.PLAYERS);
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public PlayerCharacter retrievePlayerCharacter(String id) {
		try {
			return retrieveResource(id, PlayerCharacter.class, Loadpoint.PLAYERS);
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Room retrieveRoom(String id) {
		try {
			return retrieveResource(id, Room.class, getLoadpoint());
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Entity retrieveEntity(String id) {
		try {
			return retrieveResource(id, Entity.class, getLoadpoint());
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Zone retrieveZone(String id) {
		try {
			return retrieveResource(id, Zone.class, getLoadpoint());
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Item retrieveItem(String id) {
		try {
			return retrieveResource(id, Item.class, getLoadpoint());
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Mobile retrieveMobile(String id) {
		try {
			return retrieveResource(id, Mobile.class, getLoadpoint());
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
	protected <T extends AbstractBusinessObject> T retrieveResource(String id, Class<T> type, Loadpoint point) throws XMLDBException, JAXBException {
		String query = "for $doc in //ring//*[@id=\"" + id + "\"] return $doc";
		XQuery xq = new XQuery(query);
		xq.setLoadpoint(point);
		List<T> results = xq.execute(type);
		
		if (results.size() > 0) {
			return results.get(0);
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
