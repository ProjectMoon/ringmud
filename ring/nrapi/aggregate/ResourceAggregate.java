package ring.nrapi.aggregate;

import ring.nrapi.data.Persistable;

public interface ResourceAggregate extends Persistable {
	public String toXML();
	public String getID();
	public void setID(String id);
	public void setStoreAsUpdate(boolean val);
	public boolean storeAsUpdate();
}
