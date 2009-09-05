package ring.movement.nm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main class that implements the movement logic of the MUD. It stores
 * the adjacency list of Rooms and Portals and exposes methods for retrieving
 * Destination objects.
 * @author jeff
 *
 */
public class LocationManager {
	protected static HashMap<Room, HashMap<String, Portal>> worldGrid =
		new HashMap<Room, HashMap<String, Portal>>();

	private static Destination getDestinationFromPortal(Portal portal) {
		return portal.getDestination();
	}
	
	public static Destination getDestination(Room room, String portalName) {
		try {
			return getDestinationFromPortal(getPortal(room, portalName));
		}
		catch (PortalNotFoundException e) {
			System.err.println("Portal " + portalName + " not found.");
			return null;
		}
	}
	
	public static Portal getPortal(Room room, String portalName) {
		Portal p = worldGrid.get(room).get(portalName);
		if (p == null)
			throw new PortalNotFoundException(portalName + " not found");
		else
			return p;
		
	}
	
	public static List<Portal> getPortals(Room room) {
		HashMap<String, Portal> ports = worldGrid.get(room);
		if (ports == null || ports.isEmpty()) {
			return null;
		}
		
		List<Portal> portList = new ArrayList<Portal>();
		
		for (String key : ports.keySet()) {
			portList.add(ports.get(key));
		}
		
		return portList;
	}
	
	/**
	 * A very important method that forms the heart of constructing the world
	 * grid. It takes care of inserting new Room -> Portal -> Destination
	 * combinations into the adjacency list, or updating ones already present.
	 * @param room
	 * @param port
	 * @param dest
	 * @return true if the combination was added successfully, false if there was
	 * already a Destination at the specified Room and Portal "coordinate."
	 */
	public static boolean addToGrid(Room room, Portal port) {
		//If this is a new coordinate, we need to add it.
		if (getPortals(room) == null) {
			return putNewGridEntry(room, port);
		}
		else {
			return updateGridEntry(room, port);
		}
	}
	
	/**
	 * A variant of the standard grid creation method that attempts to link
	 * back the room that was just added to allow for two-way travel. This
	 * simplifies the internal logic needed to link most rooms together.
	 * The method will first add the grid entry as normal for the two
	 * parameters. It will then try to do the reverse. For this to work,
	 * the Destination of the specified Portal must be a Room and the Portal's
	 * interactive name must be one of the standard 6 directions.
	 * @param room
	 * @param port
	 * @param autolink
	 * @return
	 */
	public static boolean addToGrid(Room room, Portal port, boolean autolink) {
		if (!autolink)
			return addToGrid(room, port);
		else {
			//TODO implement this. Main problem is "how do i determine opposite direction?"
			throw new UnsupportedOperationException();
		}
	}
	
	private static boolean putNewGridEntry(Room room, Portal port) {
		HashMap<String, Portal> ports = new HashMap<String, Portal>();
		ports.put(port.getInteractiveName(), port);
		HashMap<String, Portal> old = worldGrid.put(room, ports);
		
		if (old != null)
			return false;
		else
			return true;
		
	}
	
	private static boolean updateGridEntry(Room room, Portal port) {
		HashMap<String, Portal> ports = worldGrid.get(room);
		Portal old = ports.put(port.getInteractiveName(), port);
		if (old != null) {
			worldGrid.put(room, ports);
			return true;
		}
		else {
			return false;
		}	
	}
}
