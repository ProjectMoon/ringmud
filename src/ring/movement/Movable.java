package ring.movement;

/**
 * The Movable interface defines something that moves in the game world.
 * This interface should not be confused with class Mobile (which implements it).
 * A Mobile refers more to an intelligent agent in the game world such as a player
 * or NPC. A Movable object could be anything, but is usually a Mobile. An example of
 * a non-Mobile Movable object would be an Entity that moves from room to room providing
 * ambience.
 * 
 * A Movable provides two methods: setLocation and getLocation. This allows the MUD to
 * keep track of this Movable's position.
 * @author projectmoon
 *
 */
public interface Movable {
	/**
	 * Sets this Movable's current location.
	 * @param loc
	 */
	public void setLocation(Room loc);
	
	/**
	 * Gets this movable's current location.
	 * @return
	 */
	public Room getLocation();
	
	/**
	 * Returns true if this Movable is allowed to move at the given moment when
	 * this method is executed.
	 * @return true or false
	 */
	public boolean canMove();
}
