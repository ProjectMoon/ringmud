package ring.nrapi.data;

import ring.nrapi.movement.Room;
import ring.nrapi.movement.Zone;
import ring.nrapi.entities.Entity;

public interface DataStore {
	public boolean storePersistable(Persistable p);
	public boolean storeDocument(String collectionName, Persistable doc);
	
	public Entity retrieveEntity(String id);
	public Room retrieveRoom(String id);
	public Zone retrieveZone(String id);
}
