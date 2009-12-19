package ring.nrapi.data;

import java.io.File;

import ring.nrapi.movement.Room;
import ring.nrapi.movement.Zone;
import ring.nrapi.entities.Entity;

public interface DataStore {
	/**
	 * Stores a Persistable object in the datastore.
	 * @param p
	 * @return true if successful, false otherwise.
	 */
	public boolean storePersistable(Persistable p);
	
	/**
	 * Imports an XML document into the Datastore.
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
	 * Get an Entity from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was found.
	 */
	public Entity retrieveEntity(String id);
	
	/**
	 * Get a Room from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was found.
	 */
	public Room retrieveRoom(String id);
	
	/**
	 * Get a Zone from the datastore by the specified ID.
	 * @param id
	 * @return The requested object, or null if one was found.
	 */
	public Zone retrieveZone(String id);
}
