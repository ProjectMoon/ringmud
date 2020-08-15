package ring.movement;

import ring.nrapi.business.BusinessObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to group Rooms and Portals together for use by the
 * LocationManager and storage in the database. It is not typically declared
 * in game code. The only place it will generally be found in user code is in
 * Python data files.
 * @author projectmoon
 *
 */
public class Location extends BusinessObject {
	private Room room;
	private List<Portal> exits = new ArrayList<Portal>();

	public Location() {}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public List<Portal> getExits() {
		return exits;
	}
	
	public void setExits(List<Portal> exits) {
		this.exits = exits;
	}
	
	public void addExit(Portal port) {
		exits.add(port);
	}
}
