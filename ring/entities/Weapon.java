package ring.entities;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.damage.WeaponDamage;
import ring.effects.Effect;
import ring.mobiles.BodyPart;

/**
 * This class represents a weapon in the game. A weapon is generally a pointy
 * thing you apply to enemy creatures that makes them less alive, and with
 * repeated application, dead. The class encapsulates weapon type, as well as
 * the type of damage it deals, and its damage dice.
 * 
 * @author projectmoon
 * 
 */
public class Weapon extends Item {
	public static final long serialVersionUID = 1;
	public static int SWORD = 0;
	public static int DAGGER = 1;
	public static int STAFF = 2;
	public static int BOW = 3;
	public static int AXE = 4;
	public static int MACE = 5;
	public static int HAMMER = 6;
	public static int WAND = 8;

	// Certain necessary things.
	private WeaponDamage damage;
	private int type;
	private Effect procEffects;

	// Weapon flags!
	private boolean twoHanded;
	private boolean cursed;

	//Empty constructor required for bean-ness.
	public Weapon() {

	}

	public Weapon(String name, String indefiniteDescriptor, BodyPart bodyPart,
			String idleDescriptor, WeaponDamage damage, int type,
			Effect passiveEffects, Effect procEffects) {
		super.name = name;
		super.idleDescriptor = idleDescriptor;
		this.damage = damage;
		if ((type < SWORD) || (type > WAND))
			type = SWORD;
		this.type = type;
		super.passiveEffects = passiveEffects;
		this.procEffects = procEffects;
		super.partWornOn = bodyPart;
		super.wearable = true;
	}

	public WeaponDamage getDamage() {
		return damage;
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public Effect getProcEffects() {
		return procEffects;
	}
	
	public void setProcEffects(Effect effects) {
		procEffects = effects;
	}
	
	public boolean isTwoHanded() {
		return twoHanded;
	}
	
	public boolean isCursed() {
		return cursed;
	}
	
	public void setTwoHanded(boolean yes) {
		twoHanded = yes;
	}
	
	public void setCursed(boolean yes) {
		cursed = yes;
	}

}
