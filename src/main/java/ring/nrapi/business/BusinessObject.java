package ring.nrapi.business;

import ring.events.listeners.BusinessObjectListener;
import ring.persistence.DataStoreFactory;
import ring.persistence.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BusinessObject implements Persistable {
	private Persistable parent;
	private List<Persistable> children = new ArrayList<Persistable>();
	
	private UUID uuid;
	private String id;
	private String docName;
	private List<BusinessObjectListener> listeners = new ArrayList<BusinessObjectListener>();
	
	private boolean storeAsUpdate;
	
	public void save() {
		DataStoreFactory.getDefaultStore().storePersistable(this);
	}

	/**
	 * Responsible for propagating information down the hierarchy
	 * of objects found in the document or fragment that this
	 * BusinessObject was loaded from. It propagates information
	 * and calls each child's <code>createChildRelationships()</code> method. This
	 * results in a recursive propagation of information down through the object
	 * hierarchy.
	 * <br/><br/>
	 * This method only concerns objects that are created at object load. It does
	 * not propagate information to children added later during server operation.
	 */
	public final void createChildRelationships() {
		for (Persistable child : children) {
			createChildRelationship(this, child);
		}
	}
	
	private void createChildRelationship(Persistable parent, Persistable child) {
		child.setParent(parent);
		child.setStoreAsUpdate(parent.storeAsUpdate());
		
		//If the document name is null, then it wasn't set anywhere else
		//so the child is probably from the same document...
		if (child.getDocumentName() == null) {
			child.setDocumentName(parent.getDocumentName());
		}
		
		for (Persistable grandchild : child.getChildren()) {
			createChildRelationship(child, grandchild);
		}
	}
	
	/**
	 * Adds a child to the list of children to propagate information
	 * to when <code>createChildRelationships()</code> is called. This
	 * list is only read when this abstract business object is first loaded.
	 * Afterwards, it is not considered relevant. In other words, any new
	 * child business objects added to this object (i.e. a mobile added to a
	 * room) do not have parent information propagated to them. It is 
	 * possible that this might change depending on how world state saving
	 * is implemented.
	 * @param child The child to add.
	 */
	@Override
	public void addChild(Persistable child) {
		children.add(child);
	}
	
	@Override
	public List<Persistable> getChildren() {
		return children;
	}
	
	@Override
	public boolean isRoot() {
		return (getParent() == null);
	}
	
	@Override
	public Persistable getRoot() {
		if (getParent() == null) {
			return this;
		}
		else {
			Persistable root = getParent();
			while (root.getParent() != null) {
				root = root.getParent();
			}
			
			return root;
		}
	}
	
	@Override
	public Persistable getParent() {
		return parent;
	}
		
	@Override
	public void setParent(Persistable obj) {
		parent = obj;
	}
	

	public String getCanonicalID() {
		return getDocumentName() + ":" + getID();
	}

	public String getDocumentName() {
		return docName;
	}
	
	public void setDocumentName(String docName) {
		this.docName = docName;
	}
	
	/**
	 * Returns the objects unique's ID.
	 * @return the id
	 */
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public void setStoreAsUpdate(boolean val) {
		storeAsUpdate = val;
	}

	@Override
	public boolean storeAsUpdate() {
		return storeAsUpdate;
	}
	
	@Override
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void addBusinessObjectListener(BusinessObjectListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public boolean removeBusinessObjectListener(BusinessObjectListener listener) {
		return listeners.remove(listener);
	}
	
	public List<BusinessObjectListener> getBusinessObjectListeners() {
		return listeners;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docName == null) ? 0 : docName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result + (storeAsUpdate ? 1231 : 1237);
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessObject other = (BusinessObject) obj;
		if (docName == null) {
			if (other.docName != null)
				return false;
		} else if (!docName.equals(other.docName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (listeners == null) {
			if (other.listeners != null)
				return false;
		} else if (!listeners.equals(other.listeners))
			return false;
		if (storeAsUpdate != other.storeAsUpdate)
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (uuid != other.uuid)
			return false;
		return true;
	}

	
}
