package ring.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import ring.world.*;
import ring.movement.Room;

/**
 * This class represents something non-living in the world. This includes items,
 * props in a room, doors, and other similar things. It should be noted that a
 * Room is not considered an entity.
 * @author projectmoon
 *
 */
public class Entity extends WorldObject {	
	public static final long serialVersionUID = 1;

	protected Room currentRoom;
	protected String idleDescriptor;
	protected String indefiniteDescriptor;
	protected int currentHP;
	protected int maxHP;
	protected int currentAC;

	public Entity() {
		idleDescriptor = "lies here.";
	}

	public Room getRoom() {
		return currentRoom;
	}

	public final String getIdleDescriptor() {
		return idleDescriptor;
	}
	
	public final void setIdleDescriptor(String desc) {
		idleDescriptor = desc;
	}

	public final String getIndefiniteDescriptor() {
		return indefiniteDescriptor;
	}
	
	public final void setIndefiniteDescriptor(String desc) {
		indefiniteDescriptor = desc;
	}

	//Not final because some weapons may need to override to be
	//intelligent. They are considered "NPCs."
	@Override
	public boolean isNPC() {
		return false;
	}

	@Override
	public final boolean isPlayer() {
		return false;
	}

	@Override
	public final boolean isEntity() {
		return true;
	}	
	
	@Override
	public final boolean isItem() {
		return this instanceof Item;
	}

	public int getEntityType() {
		return 0;
		// Meant to be overriden.
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getAC() {
		return currentAC;
	}
	
	@Override
	public boolean isAttackable() {
		return false;
	}
}
