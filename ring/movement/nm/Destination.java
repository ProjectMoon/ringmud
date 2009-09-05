package ring.movement.nm;

import ring.mobiles.Mobile;

/**
 * An abstract interface representing a place a mobile can go in the MUD.
 * It is typically another Room object, but it could be other things as
 * well.
 * @author jeff
 *
 */
public interface Destination {
	/**
	 * Tells the LocationManager whether or not this Mobile may enter this
	 * Destination. Can be used to block access to lower level players,
	 * control access based on MUD user level, etc.
	 * @param mob
	 * @return true or false. The method should also return false if mob is null.
	 */
	public boolean canEnter(Mobile mob);
}
