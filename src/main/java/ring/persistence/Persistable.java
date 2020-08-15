package ring.persistence;

import java.util.List;
import java.util.UUID;

/**
 * Interface representing persistable to a DataStore. Persistables are modeled
 * as an in-memory hierarchy; they are object representations of the XML document
 * or fragment that their data comes from. When Persistables are loaded, parent-child
 * relationships are created by implementing code.
 * <br/><br/>
 * This interface exists to enforce a sort of design-by-contract process. Currently the
 * only implementing code of this interface is the default code that deals with
 * <code>AbstractBusinessObject</code>s. 
 * <br/><br/>
 * When a Persistable is loaded, the implementation is responsible for creating its object
 * hierarchy. No method is exposed by this interface to propagate information to a Persistable's
 * children, so is up to the implementation to provide some concrete way of propagating information
 * to child Persistables and executing that logic internally without user interaction.
 * <br/><br/>
 * When a Persistable is loaded, the following information is always passed on to its child objects,
 * resulting in these properties being propagated through the whole hierarchy:
 * <ul>
 * 	<li>The <code>storeAsUpdate</code> property.</li>
 * </ul>
 * In addition, the <code>documentName</code> property is generally passed down to the child,
 * though this is not always the case. For example, an implementation that creates a hierarchy
 * that pulls Persistables from many different documents would likely not pass the document name
 * property to child objects. 
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
	 * Transforms this Persistable into an XML fragment. It does not
	 * contain the header information or the &lt;ring&gt; root element.
	 * @return an XML String representation of this Persistable.
	 */
	public String toXML();
	
	/**
	 * Transforms this Persistable into a full XML documemnt with a &lt;ring&gt;
	 * root element.
	 * @return The XML string.
	 */
	public String toXMLDocument();
	
	/**
	 * Gets the parent object of this Persistable. If this Persistable is the
	 * root of the hierarchy, this method returns null.
	 * @return the parent, or null if this is the root object.
	 */
	public Persistable getParent();
	
	/**
	 * Sets the parent object of this Persistable. Called by implementations
	 * when creating the object hierarchy.
	 * @param parent
	 */
	public void setParent(Persistable parent);
	
	/**
	 * Tells whether or not this Persistable is the root of its hierarchy.
	 * That is, this method returns true if getParnet() == null for this
	 * Persistable.
	 * @return True or false
	 */
	public boolean isRoot();
	/**
	 * Returns the root Persistable in the object hierarchy, regardless of
	 * where this object is located in the hierarchy. If this Persistable is
	 * the root of the hierarchy, this method simply returns the same object.
	 * @return The root of the object hierarchy.
	 */
	public Persistable getRoot();
	
	/**
	 * Returns the list of children this Persistable has. The children returned
	 * are the ones that were created when this Persistable was loaded into
	 * memory. Implementations <b>should not</b> return dynamically added children.
	 * This is subject to change in the future.
	 * @return The children
	 */
	public List<Persistable> getChildren();
	
	/**
	 * Adds a child Persistable to this Persistable. Called by implementations when
	 * creating the object hierarchy.
	 * @param child
	 */
	public void addChild(Persistable child);
	
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
	 * Gets the name of the XML document that this Persistable comes from.
	 * This may differ from Persistable to Persistable within the hierarchy.
	 * A Persistable can have children that come from different documents due
	 * to XQuery's ability to include fragments from many disparate data sources.
	 * @return The document name.
	 */
	public String getDocumentName();
	
	/**
	 * Sets the document name.
	 * @param docName The desired document name.
	 */
	public void setDocumentName(String docName);

	/**
	 * Gets the canonical ID for this Persistable. The canonical ID is a read-only, absolute,
	 * unchanging attribute used to uniquely identifiy this Persistable in the system.
	 * The canonical ID is of the format "documentName:objectID". Implementations typically
	 * just concatenate <code>getDocumentName()</code>, a colon, and <code>getID()</code> together
	 * to produce this value.
	 * <br/><br/>
	 * This property is subject to disappear.
	 * @return
	 */
	public String getCanonicalID();
	
	/**
	 * Returns the UUID that this Persistable has. The UUID, or universally unique identifier,
	 * identifies this Persistable instance to the MUD engine. 
	 * @return
	 */
	public UUID getUuid();
	
	/**
	 * Sets the UUID for this Persistable. User code should generally not call this method,
	 * because anomalous behavior will result when searching for this Persistable using the
	 * engine's object searching capability.
	 */
	public void setUuid(UUID uuid);
	
	
}
