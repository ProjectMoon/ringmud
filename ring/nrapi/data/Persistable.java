package ring.nrapi.data;

/**
 * Interface representing something persistable to an XMLDataStore.
 * @author projectmoon
 *
 */
public interface Persistable {
	/**
	 * Returns true if this Persistable object is to be stored as an update to
	 * to an existing document, rather than as an insert.
	 * @return true or false
	 */
	public boolean storeAsUpdate();
	
	/**
	 * Sets whether or not to store this Persistable as an update or insert.
	 * @param val
	 */
	public void setStoreAsUpdate(boolean val);
	
	/**
	 * Transforms this Persistable into an XML string.
	 * @return an XML String representation of this Persistable.
	 */
	public String toXML();
	
	/**
	 * Gets the ID of this Persistable.
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Sets the id of this Persistable.
	 * @param id
	 */
	public void setID(String id);
	
	/**
	 * Tells whether or not this Persistable is referential. A referential
	 * Persistable does not store any data but its ID. Other objects can
	 * reference this ID and load the actual object from the datastore.
	 * A Persistable should be referential when being stored as part of
	 * a larger object. Persistables nested in larger Persistables cannot
	 * be stored referentially during server use. They can only be imported.
	 * That is, an Item in an Inventory will be stored as its own entry when
	 * the server is running, instead of an Item reference. When using the
	 * import module, the Item can be a reference, though.
	 * @return true or false
	 */
	public boolean isReferential();
	
	/**
	 * Sets the referentiality of this Persistable. A referential
	 * Persistable does not store any data but its ID. Other objects
	 * can reference the ID and load the actual object from the datastore. 
	 * A Persistable should be referential when being stored as part of a
	 * larger object. Persistables nested in larger Persistables cannot
	 * be stored referentially during server use. They can only be imported.
	 * That is, an Item in an Inventory will be stored as its own entry when
	 * the server is running, instead of an Item reference. When using the
	 * import module, the Item can be a reference, though.
	 * @param val
	 */
	public void setReferential(boolean val);
}
