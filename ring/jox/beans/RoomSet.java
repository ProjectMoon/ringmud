package ring.jox.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

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
	
	private static Logger log = Logger.getLogger(RoomSet.class.getName());
	private HashMap<String, RoomBean> roomSet;
	private HashMap<String, Room> cachedLocations;
	private String name;
	
	public RoomSet() {
		roomSet = new HashMap<String, RoomBean>();
		cachedLocations = new HashMap<String, Room>();
	}
	
	public void copyFrom(RoomSet otherSet) {
		roomSet.putAll(otherSet.roomSet);
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
		log.info("Building RoomSet \"" + this.getName() + "\"");
		for (RoomBean roomBean : roomSet.values()) {
			log.fine("Processing " + roomBean);
			
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
		RoomBean bean = roomSet.get(id);
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
