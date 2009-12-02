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
	 * @return
	 */
	public boolean storeAsUpdate();
	
	/**
	 * Sets whether or not to store this Persistable as an update or insert.
	 * @param yes
	 */
	public void setStoreAsUpdate(boolean val);
	
	/**
	 * Transforms this Persistable into an XML string.
	 * @return
	 */
	public String toXML();
	
	public String getID();
	public void setID(String id);
}
