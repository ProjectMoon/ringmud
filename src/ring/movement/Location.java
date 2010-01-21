package ring.movement;


/**
 * An abstract interface representing a place a Movable can go in the MUD.
 * It is typically another Room object, but it could be other things as
 * well.
 * @author jeff
 *
 */
public interface Location {
	/**
	 * Tells the LocationManager whether or not this Movable may enter this
	 * Destination. Can be used to block access to lower level players,
	 * control access based on MUD user level, etc.
	 * @param mob
	 * @return true or false. The method should also return false if mob is null.
	 */
	public boolean canEnter(Movable m);
	
	/**
	 * An event callback to implement the behavior that happens when a Movable
	 * enters this Location. Most of the time, this consists of sending information
	 * to other players in the area that the Movable has arrived.
	 * @param m
	 * @param from
	 */
	public void movableEnters(Movable m, Portal from);
	
	/**
	 * An event callback to implement the behavior that happens when a Movable
	 * leaves this Location. Most of the time, this consists of sending information
	 * to other players in the area that the Movable has left.
	 * @param m
	 * @param to
	 */
	public void movableLeaves(Movable m, Portal to);
	
	/**
	 * The Location should be able to find Portals (edges on the graph) in
	 * relation to itself. Most implementations of Location will rely on the
	 * LocationManager to store and manage this information, and a call to this
	 * method will be equivalent to calling the LocationManager's getPortal method.
	 * However, it is not required that it use the Location Manager.
	 * @param destination
	 * @return the Portal in the specified direction. null if the Portal does not exist.
	 * @throws PortalNotFoundException if the Portal was not found.
	 */
	public Portal getPortal(String destination) throws PortalNotFoundException;
}
