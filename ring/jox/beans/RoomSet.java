package ring.jox.beans;

import java.io.Serializable;
import java.util.HashMap;

import ring.movement.Location;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.Room;

/**
 * This class represents a set of rooms being loaded in by the JOX parser. It stores
 * all rooms so that portals can be created for each one. It is the "zone building system."
 * @author projectmoon
 *
 */
public class RoomSet extends RingBean<RoomSet> implements Serializable {
	public static final long serialVersionUID = 1;
	
	private HashMap<String, RoomBean> roomSet;
	private String name;
	
	public RoomSet() {
		roomSet = new HashMap<String, RoomBean>();
	}
	
	public RoomBean[] getRoom() {
		return roomSet.values().toArray(new RoomBean[0]);
	}
	
	public void setRoom(RoomBean[] rooms) {
		for (RoomBean room : rooms) {
			roomSet.put(room.getID(), room);
		}
	}
	
	/**
	 * Constructs the zone based on the rooms here.
	 */
	public void construct() {
		System.out.println("Building RoomSet \"" + this.getName() + "\"");
		for (RoomBean roomBean : roomSet.values()) {
			System.out.println("Processing " + roomBean);
			Room room = new Room();
			room.populateFromBean(roomBean);
			
			//Create all the exits for this room and add it to the grid.
			for (PortalBean portBean : roomBean.getExit()) {
				Portal port = new Portal();
				port.populateFromBean(portBean);
				Location dest = getLocationFromID(roomBean.getID());
				port.setDestination(dest);
				LocationManager.addToGrid(room, port);
			}
		}
	}
	
	private Location getLocationFromID(String id) {
		RoomBean bean = roomSet.get(id);
		Room r = new Room();
		r.populateFromBean(bean);
		return r;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
