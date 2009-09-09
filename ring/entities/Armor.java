package ring.entities;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.util.List;

import ring.effects.Effect;
import ring.mobiles.Alignment;
import ring.mobiles.Body;
import ring.mobiles.BodyPart;
import ring.mobiles.Mobile;
import ring.mobiles.MobileClass;
import ring.mobiles.Race;

/**
 * This class represents a piece of armor in the world. It carries several properties,
 * the most important of which is armor class (AC). The higher the AC bonus, the more
 * protected the wearer is.
 * @author projectmoon
 *
 */
public final class Armor extends Item {
	public static final long serialVersionUID = 1;
	
	//The AC bonus of this armor piece
	private int acBonus;
	
	// Races that can wear this armor piece.
	private List<Race> racesAllowed;
	
	// Classes that can wear this armor piece.
	private List<MobileClass> classesAllowed;
	
	//Alignments that can wear this armor piece.
	private List<Alignment> alignmentsAllowed;

	public Armor() {
		acBonus = 0;
		super.passiveEffects = null;
		super.partWornOn = Body.HEAD;
		super.name = "default armor piece";
		super.wearable = true;
	}

	// Creates a basic piece of armor that anyone can use.
	public Armor(int AC, Effect effects, BodyPart bodyPart, String name,
			String indefiniteDescriptor, String idleDescriptor) {
		this.acBonus = AC;
		super.name = name;
		super.indefiniteDescriptor = indefiniteDescriptor;
		super.idleDescriptor = idleDescriptor;
		super.wearable = true;
		super.partWornOn = bodyPart;
		super.passiveEffects = effects;
	}

	// Most specific constructor... Creates a very specific piece of armor that
	// factors in race,
	// class, and alignment.
	public Armor(int AC, Effect effects, BodyPart bodyPart, String name,
			String indefniteDescriptor, String idleDescriptor,
			List<Race> racesAllowed, List<MobileClass> classesAllowed,
			List<Alignment> alignmentsAllowed) {
		this.acBonus = AC;
		super.partWornOn = bodyPart;
		super.name = name;
		super.indefiniteDescriptor = indefiniteDescriptor;
		super.idleDescriptor = idleDescriptor;
		this.racesAllowed = racesAllowed;
		this.classesAllowed = classesAllowed;
		this.alignmentsAllowed = alignmentsAllowed;
		super.passiveEffects = effects;
	}

	// Methods!

	// checkAlignment method.
	// This method returns a true or false depending on the alignment of the
	// mobile that is passed
	// to it. It is used when seeing if a mobile can wear the armor or not.
	public boolean checkAlignment(Mobile mob) {
		// Is there even an alignment of the armor to check? If there isn't
		// anyone can wear it.
		if (alignmentsAllowed == null || alignmentsAllowed.size() < 1) {
			return true;
		}

		// Else... continue on.
		Alignment mobAlignment = mob.getAlignment();

		for (Alignment alignment : alignmentsAllowed) {
			if (mobAlignment.equals(alignment)) {
				return true;
			}
		}

		return false;
	}

	// checkRace method.
	// This method returns a true or false depending on the race of the mobile
	// that is passed to it.
	// It is used when checking if a mobile can wear this armor piece or not.
	public boolean checkRace(Mobile mob) {
		// Is there even a race of the armor to check? If there isn't anyone can
		// wear it.
		if (racesAllowed == null)
			return true;

		// Else... continue on.
		Race mobRace = mob.getRace();

		for (Race race : racesAllowed) {
			if (mobRace.equals(race)) {
				return true;
			}
		}

		return false;
	}

	// checkClass method.
	// This method returns a true or false depending on the class of the mobile
	// that is passed to it.
	// It is used when checking if a mobile can we1ar this armor piece or not.
	public boolean checkClass(Mobile mob) {
		// Is there even a class of the armor to check? If there isn't anyone
		// can wear it.
		if (classesAllowed == null)
			return true;

		// Else... continue on.
		MobileClass mobClass = mob.getMobileClass();

		for (MobileClass mc : classesAllowed) {
			if (mobClass.equals(mc)) {
				return true;
			}
		}

		return false;
	}

	// getAC method.
	// This method returns the AC of the armor piece.
	public int getAC() {
		return acBonus;
	}
	
	public void setACBonus(int ac) {
		acBonus = ac;
	}
}
