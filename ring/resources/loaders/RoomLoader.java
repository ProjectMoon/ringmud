package ring.resources.loaders;

import java.util.ArrayList;
import java.util.List;

import ring.movement.Room;
import ring.resources.beans.RoomBean;
import ring.resources.providers.RoomProvider;

public class RoomLoader extends AbstractResourceLoader<RoomBean, Room, RoomProvider> implements ResourceLoader<Room> {
	
	@Override
	public Room getResource(String beanID) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			Room r = new Room();
			RoomBean bean = super.getBean(beanID);
			r.populateFromBean(bean);
			return r;
		}
		else {
			throw new IllegalStateException(this + " getResource: beans not yet loaded!");
		}
	}

	@Override
	public void refresh(Room resource) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			resource.populateFromBean(super.getBean(resource.getBeanID()));
		}
		else {
			throw new IllegalStateException(this + " refresh: beans not yet loaded!");
		}
	}

	@Override
	public List<Room> getAllResources() throws IllegalStateException {
		ArrayList<Room> list = new ArrayList<Room>(super.getBeanAmount());
		
		for (RoomBean bean : super.getBeans()) {
			Room r = new Room();
			r.populateFromBean(bean);
			list.add(r);
		}
		
		return list;
	}
}
