package ring.resources.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ring.movement.Location;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.Room;
import ring.resources.beans.PortalBean;
import ring.resources.beans.RoomBean;
import ring.resources.providers.RoomProvider;

public class RoomLoader extends AbstractResourceLoader<RoomBean, Room, RoomProvider> implements ResourceLoader<Room> {
	private HashMap<String, Room> cachedLocations = new HashMap<String, Room>();
	
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
	
	/**
	 * Constructs the world.
	 */
	public void constructWorld() {		
		for (RoomBean roomBean : super.getBeans()) {
			System.out.println("Room bean: " + roomBean);
			Room room = getRoom(roomBean);
			
			//Create all the exits for this room and add it to the grid.
			for (PortalBean portBean : roomBean.getExit()) {
				Portal port = new Portal();
				port.populateFromBean(portBean);
				Location dest = getLocationFromID(portBean.getDestination());
				port.setDestination(dest);
				LocationManager.addToGrid(room, port);
			}
		}
	}
	
	private Location getLocationFromID(String id) {
		RoomBean bean = super.getBean(id);
		Room r = getRoom(bean);
		return r;
	}
	
	private Room getRoom(RoomBean roomBean) {
		Room room = cachedLocations.get(roomBean.getID());
		
		if (room == null) {
			room = new Room();
			room.populateFromBean(roomBean);
			cachedLocations.put(roomBean.getID(), room);
		}
		
		return room;
	}

}
