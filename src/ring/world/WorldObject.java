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
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import ring.effects.Affectable;
import ring.effects.Effect;

/**
 * This class defines things common to all objects that exist within the game
 * world. Things like weight, location, name, description, and effects are 
 * centralized in this highest class of the game data heirarchy.
 * 
 * @author projectmoon
 * 
 */
public abstract class WorldObject implements Affectable, Serializable {
	public static final long serialVersionUID = 1;

	protected String name;
	protected int weight;
	protected String longDescription;

	/**
	 * The list of Effects on this world object. World objects must take care of calling
	 * the WorldObject's removeDeadEffects() method in order to get rid of dead effects each tick.
	 */
	protected List<Effect> effectsList = Collections.synchronizedList(new ArrayList<Effect>());

	public WorldObject() {

	}

	// addEffect method.
	// Adds an Effect to the WorldObject.
	public final void addEffect(Effect e) {
		// This should make sure that effects added to a WorldObject are always
		// unique.

		e = e.uniqueInstance();
		assert (e.getTarget() == this);
		
		if (e.getTarget() != this) {
			System.out.println("***WARNING***\nInbound effect [" + e
					+ "] is not targeting WorldObject [" + this
					+ "]. Operation aborted.");
			return;
		}

		effectsList.add(e);
		e.begin();
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

	protected void removeDeadEffects() {
		ArrayList<Effect> effectsToRemove = new ArrayList<Effect>();
		for (Effect effect : effectsList) {
			if (effect.isDead()) {
				System.out.println("***Removing Effect: " + effect);
				effectsToRemove.add(effect);
			}
		}
		
		if (effectsToRemove.size() > 0) {
			effectsList.removeAll(effectsToRemove);
		}
	}
}
