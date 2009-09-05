package ring.movement.nm;

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
	public void setLocation(Location loc);
	public Location getLocation();
	
	/**
	 * Tells whether this Movable needs to use a search check to find hidden exits.
	 * If not, it is assumed that the Movable can enter hidden exits without searching
	 * for them.
	 * @return true or false
	 */
	public boolean usesSearchSkill();
	
	/**
	 * Returns a search check number, if the Movable supports it. The method should return a number
	 * less than zero otherwise. The method can return a new number each time this method is called
	 * (i.e. internally roll dice), or depend on another method to roll checks for it. In that case,
	 * it is expected that the mehtod will return the same number until the other method that changes
	 * the check result is called.
	 * @return
	 */
	public int getSearchCheck();
}
