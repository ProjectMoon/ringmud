package ring.persistence;

import java.io.File;

import ring.items.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.movement.Zone;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.entities.Entity;

/**
 * The DataStore interface is the friendly entry point to RingMUD's backend
 * database. This allows saving of Persistable objects of
 * all types, as well as loading of the default defined business objects
 * available in RingMUD. An object is always retrieved via its ID, which
 * is a UUID stored by the object.
 * <br/><br/>
 * If you want to define extra types of retrieval methods for business objects
 * that you have created, extend this interface, and add your methods.
 * @author projectmoon
 *
 */
public interface DataStore {	
	/**
	 * Stores a Persistable object in the datastore. This method must
	 * accepts only Persistable objects. The object is transformed
	 * into XML by calling its toXML() method, which is specified
	 * by the Persistable interface.
	 * @param p
	 * @return true if successful, false otherwise.
	 */
	public boolean storePersistable(Persistable p);
	
	/**
	 * Imports an XML document into the Datastore. This method, unlike
	 * storePersistable, will accept any XML.
	 * @param file
	 * @return true if successful, false otherwise.
	 */
	public boolean importDocument(File file);
	
	/**
	 * Removes the XML document from the datastore.
	 * @param docID
	 * @return true if successful, false otherwise
	 */
	public boolean removeDocument(String docID);
	
	/**
	 * Sets the load point. The laod point can either be default,
	 * static, or game. The default load point will pull objects
	 * from the game collection, and then the static collection if
	 * it cannot find the object in game. Static and game load points
	 * pull from the static and game collections only, respectively.
	 * @param point
	 */
	public void setLoadpoint(Loadpoint point);
	
	/**
	 * Gets the load point.
	 * @return the load point.
	 */
	public Loadpoint getLoadpoint();
	
	/**
	 * Get an Entity from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public Entity retrieveEntity(String id);
	
	/**
	 * Gets an Item from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public Item retrieveItem(String id);
	
	/**
	 * Get a Room from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was found.
	 */
	public Room retrieveRoom(String id);
	
	/**
	 * Get a Zone from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public Zone retrieveZone(String id);
	
	/**
	 * Get a Mobile from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public Mobile retrieveMobile(String id);
	
	/**
	 * Gets a Player from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public Player retrievePlayer(String id);
	
	/**
	 * Get a PlayerCharacter from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was not found.
	 */
	public PlayerCharacter retrievePlayerCharacter(String id);
}
