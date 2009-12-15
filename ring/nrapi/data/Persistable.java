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
}
