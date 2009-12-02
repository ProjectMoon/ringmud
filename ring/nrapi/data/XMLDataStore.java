package ring.nrapi.data;

import ring.nrapi.aggregate.ResourceAggregate;
import ring.nrapi.aggregate.TestAggregate;
import ring.nrapi.movement.RoomAggregate;

public interface XMLDataStore {
	public boolean storeAggregate(ResourceAggregate agg);
	
	public TestAggregate retrieveTestAggregate(String id);
	public RoomAggregate retrieveRoomAggregate(String id);
	
	public boolean storeDocument(String collectionName, Persistable doc);
}
