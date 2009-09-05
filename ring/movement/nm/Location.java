package ring.movement.nm;

import java.util.List;

import ring.mobiles.Mobile;

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
	
	public void movableEnters(Movable m, Portal from);
	
	public void movableLeaves(Movable m, Portal to);
}
