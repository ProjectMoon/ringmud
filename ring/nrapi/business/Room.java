package ring.nrapi.business;

import java.util.ArrayList;
import java.util.List;

import ring.nrapi.data.XMLDataStore;
import ring.nrapi.data.XMLDataStoreFactory;
import ring.nrapi.movement.RoomAggregate;
import ring.nrapi.movement.RoomModel;
import ring.nrapi.movement.Zone;

public class Room extends AbstractBusinessObject<RoomAggregate> {
	private ArrayList<Entity> entities;
	
	public RoomModel getModel() {
		return super.getAggregate().getModel();
	}
	
	public Zone getZone() {
		return super.getAggregate().getZone();
	}

	@Override
	public boolean load(String id) {
		XMLDataStore store = XMLDataStoreFactory.getDefaultStore();
		RoomAggregate agg = store.retrieveRoomAggregate(id);
		if (agg != null) {
			super.setAggregate(agg);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void save() {
		XMLDataStoreFactory.getDefaultStore().storeAggregate(super.getAggregate());		
	}
	
	public void addEntity(Entity ent) {
		entities.add(ent);
		getModel().addEntityID(ent.getID());
	}
	
	public boolean removeEntity(Entity ent) {
		getModel().removeEntityID(ent.getID());
		return entities.remove(ent);
	}
	
	public String generateBlindExitsString(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public String generateRandomOccupantsList(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	//TODO implement mobiles for Room
}
