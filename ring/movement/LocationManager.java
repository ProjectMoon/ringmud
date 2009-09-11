package ring.movement;

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
	//Public constants for standard directions
	public static final String NORTH = "north";
	public static final String SOUTH = "south";
	public static final String EAST = "east";
	public static final String WEST = "west";
	public static final String UP = "up";
	public static final String DOWN = "down";
	
	protected static HashMap<Location, HashMap<String, Portal>> worldGrid =
		new HashMap<Location, HashMap<String, Portal>>();

	private static Location getDestinationFromPortal(Portal portal) {
		return portal.getDestination();
	}
	
	public static Location getOrigin() {
		for (Location l : worldGrid.keySet()) {
			return l;
		}
		
		return null;
	}
	
	public static Location getDestination(Location room, String portalName) {
		try {
			return getDestinationFromPortal(getPortal(room, portalName));
		}
		catch (PortalNotFoundException e) {
			System.err.println("Portal identifier " + portalName + " not found.");
			return null;
		}
	}
	
	public static Portal getPortal(Location room, String portalName) throws PortalNotFoundException {
		Portal p = worldGrid.get(room).get(portalName);
		if (p == null)
			throw new PortalNotFoundException("Portal identifier " + portalName + " not found");
		else
			return p;
		
	}
	
	public static List<Portal> getPortals(Location room) {
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
	 * The heart of the movement system. Given a Movable and a Portal, it
	 * implements the logic of the Movable going into that Portal and arriving
	 * at a Location. The movement is allowed if 
	 * @param mov
	 * @param port
	 * @return true if the move was successful, false otherwise.
	 * @throws MovementAssertionException if the Portal being used is not at the Movable's current Location.
	 */
	public static boolean move(Movable mov, Portal port) throws MovementAssertionException, PortalNotFoundException {
		if (port == null) {
			throw new PortalNotFoundException("can't move into a null portal!");
		}
		
		//Absolute first thing we must check is if the portal is hidden.
		if (port.isHidden()) {
			if (mov.getSearchCheck() < port.getSearchDC())
				return false;
		}
		
		//Now that we are sure the portal is not hidden, we can continue.
		Location locToMoveTo = port.getDestination();

		//If the Movable can enter this location, proceed.
		if (locToMoveTo.canEnter(mov)) {
			Location leavingFrom = mov.getLocation();
			//Assert that this is actually in the grid model.
			boolean possible = assertMovementPossible(leavingFrom, port);
			
			if (!possible) {
				throw new MovementAssertionException("The Location being left does not have the Portal being moved to.");
			}
			else {
				//Now that we know it's possible, perform the move.
				leavingFrom.movableLeaves(mov, port);
				mov.setLocation(locToMoveTo);
				locToMoveTo.movableEnters(mov, port);
				return true;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Tells the LocationManager whether or not the attempted move from the given
	 * Location to the given Portal is allowed. That is, if the Portal is in the 
	 * Location's list of Portal objects stored in the global grid adjacency list.
	 * @param from
	 * @param to
	 * @return true if the movement is allowed, false otherwise.
	 */
	private static boolean assertMovementPossible(Location from, Portal to) {
		List<Portal> exitPorts = getPortals(from);
		return exitPorts.contains(to);
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
	public static boolean addToGrid(Location room, Portal port) throws WorldConstructionException {
		if (port == null) {
			throw new WorldConstructionException("Specified portal was null!");
		}
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
	public static boolean addToGrid(Location room, Portal port, boolean autolink) throws WorldConstructionException {
		if (autolink) {
			//First add the current combination to the grid
			boolean success = addToGrid(room, port);
		
			//Now add an ad-hoc Portal in the opposite direction to the original room.
			if (success) {
				System.out.println("Creating reverse link  for " + port.getDestination() + " to " + room);
				String direction = null;
				try {
					direction = getOppositeDirection(port.getInteractiveName());
				}
				catch (PortalNotFoundException e) {
					throw new WorldConstructionException("Cannot auto-link on non-standard directions!");
				}
				
				Location departFrom = port.getDestination();
				
				Portal reversePort = new Portal(room, direction);
				if (addToGrid(departFrom, reversePort)) {
					return true;
				}
				else {
					throw new WorldConstructionException("There was an error auto-linking " + room + " and " + port.getDestination());
				}
			}
			else {
				return false;
			}
		}
		else {
			return addToGrid(room, port);
		}
	}
	
	private static boolean putNewGridEntry(Location room, Portal port) {
		HashMap<String, Portal> ports = new HashMap<String, Portal>();
		ports.put(port.getInteractiveName(), port);
		HashMap<String, Portal> old = worldGrid.put(room, ports);
		
		if (old != null)
			return false;
		else
			return true;
		
	}
	
	private static boolean updateGridEntry(Location room, Portal port) {
		HashMap<String, Portal> ports = worldGrid.get(room);
		worldGrid.put(room, ports);
		return true;	
	}
	
	/**
	 * Utility method that returns the opposite direction of a standard direction.
	 * @param direction
	 * @return the opposite direction (north from south, etc)
	 * @throws PortalNotFoundException if a non-standard direction is specified.
	 */
	public static String getOppositeDirection(String direction) throws PortalNotFoundException {
		if (direction.equalsIgnoreCase(NORTH)) return SOUTH;
		else if (direction.equalsIgnoreCase(SOUTH)) return NORTH;
		else if (direction.equalsIgnoreCase(EAST)) return WEST;
		else if (direction.equalsIgnoreCase(WEST)) return EAST;
		else if (direction.equalsIgnoreCase(UP)) return DOWN;
		else if (direction.equalsIgnoreCase(DOWN)) return UP;
		
		throw new PortalNotFoundException("incorrect standard direction");
	}
}
