package ring.world;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import ring.effects.Affectable;
import ring.effects.Effect;

/**
 * This class defines things common to all objects that exist within the game
 * world. Things like weight, location, name, description, and effects are 
 * centralized in this highest bean of the game data heirarchy.
 * 
 * @author projectmoon
 * 
 */
public abstract class WorldObject implements Affectable, Serializable {
	public static final long serialVersionUID = 1;

	protected String name;
	protected int weight;
	protected String longDescription;

	// This is a vector of current effects that must be looped through and dealt
	// with each tick.
	// Each WorldObject (i.e. things that derive this class) is responsible for
	// dealing with this list
	// in its own way. Therefore there is nothing enforced on classes below this
	// one that MAKES them deal
	// with this list. Most WorldObjects that should be targetable by Effects
	// deal with them in their
	// processTick method.
	protected ArrayList<Effect> effectsList;

	public WorldObject() {
		effectsList = new ArrayList<Effect>();
	}

	// addEffect method.
	// Adds an Effect to the WorldObject.
	public final void addEffect(Effect e) {
		// This should make sure that effects added to a WorldObject are always
		// unique.

		e = e.uniqueInstance();
		System.out.println("in addeffect");
		if (e.getTarget() != this) {
			System.out.println("***WARNING***\nInbound effect [" + e
					+ "] is not targeting WorldObject [" + this
					+ "]. Operation aborted.");
			return;
		}

		effectsList.add(e);
		System.out.println("added element/starting effect");
		e.begin();
		System.out.println("started effect");
	}

	public final String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
	}

	public final int getWeight() {
		return weight;
	}

	public final void setWeight(int weight) {
		this.weight = weight;
	}

	public final String getLongDescription() {
		return longDescription;
	}
	
	public final void setLongDescription(String desc) {
		longDescription = desc;
	}
	
	public final Object source() {
		return this;
	}
	
	public abstract boolean isItem();
	public abstract boolean isEntity();
	public abstract boolean isPlayer();
	public abstract boolean isNPC();
	public abstract boolean isAttackable();
}
