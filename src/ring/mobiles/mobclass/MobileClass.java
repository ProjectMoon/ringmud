package ring.mobiles.mobclass;

import java.io.Serializable;
import ring.util.Dice;

public class MobileClass implements Serializable {
	public static final long serialVersionUID = 1;

	/**
	 * 1dX, where X is the value of hitDie, plus Con modifier is added for HP
	 * each levelup.
	 */
	private Dice hitDie;

	// A list of ClassFeatures this class has.

	// Save and Base Attack Bonus classification variables.
	private SaveType fortSaveType;
	private SaveType refSaveType;
	private SaveType willSaveType;
	private BaseAttackBonus baseAttackBonus;

	// Level variable... This is the level the mobile has in the class.
	private int level;

	// Miscellaneous variables.
	private String name; // The class's name.
	private String displayName; // How the class's name appears in-game.

	// Saving throw type. This enum describes if a classes specific saves are
	// good or poor.
	// To calculate a base save bonus, the formulas are as follows:
	// GOOD: trunc((level / 2) + 2)
	// POOR: trunc(level / 3)
	public enum SaveType {
		GOOD, POOR;
	}

	// Save enum.
	// This enum describes the three D&D 3.5 saving throw types.
	public enum Save {
		FORTITUDE, REFLEX, WILL
	}

	// Base Attack Bonus type.
	public enum BaseAttackBonus {
		GOOD(1), // Barbarian, Warrior, Paladin, Ranger
		AVERAGE(.75), // Rogue, Cleric, Druid, Shapeshifter
		POOR(.5); // CASTER, PSION, Occultist, Alchemist

		private double modifier;

		BaseAttackBonus(double modifier) {
			this.modifier = modifier;
		}

		public double getModifier() {
			return modifier;
		}
	}

	public MobileClass() {

	}

	public int getClassLevel() {
		return level;
	}

	public Dice getHitDie() {
		return hitDie;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public BaseAttackBonus getBaseAttackBonus() {
		return baseAttackBonus;
	}

	public SaveType getFortitudeSaveType() {
		return fortSaveType;
	}

	public SaveType getReflexSaveType() {
		return refSaveType;
	}

	public SaveType getWillSaveType() {
		return willSaveType;
	}

	public void setClassLevel(int level) {
		this.level = level;
	}

	public int getBaseSaveBonus(Save save) {
		if (save == Save.FORTITUDE) {
			if (fortSaveType == SaveType.POOR)
				return (int) (level / 3);
			if (fortSaveType == SaveType.GOOD)
				return (int) ((level / 2) + 2);
		}

		if (save == Save.REFLEX) {
			if (refSaveType == SaveType.POOR)
				return (int) (level / 3);
			if (refSaveType == SaveType.GOOD)
				return (int) ((level / 2) + 2);
		}

		if (save == Save.WILL) {
			if (willSaveType == SaveType.POOR)
				return (int) (level / 3);
			if (willSaveType == SaveType.GOOD)
				return (int) ((level / 2) + 2);
		}

		return -9999; // should never be hit, but keeps compiler happy
	}

	public void addClassFeature(String featureName) {
		//TODO reimplement afte rewrite of class features.
	}

	// getFeatureByName method.
	// Returns the class feature specified by the name parameter or null if
	// nothing
	// is found. This method is case insensitive.
	public ClassFeature getClassFeature(String name) {
		//TODO rewrite after implementation of class features.
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((baseAttackBonus == null) ? 0 : baseAttackBonus.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((fortSaveType == null) ? 0 : fortSaveType.hashCode());
		result = prime * result + ((hitDie == null) ? 0 : hitDie.hashCode());
		result = prime * result + level;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((refSaveType == null) ? 0 : refSaveType.hashCode());
		result = prime * result
				+ ((willSaveType == null) ? 0 : willSaveType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileClass other = (MobileClass) obj;
		if (baseAttackBonus == null) {
			if (other.baseAttackBonus != null)
				return false;
		} else if (!baseAttackBonus.equals(other.baseAttackBonus))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (fortSaveType == null) {
			if (other.fortSaveType != null)
				return false;
		} else if (!fortSaveType.equals(other.fortSaveType))
			return false;
		if (hitDie == null) {
			if (other.hitDie != null)
				return false;
		} else if (!hitDie.equals(other.hitDie))
			return false;
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (refSaveType == null) {
			if (other.refSaveType != null)
				return false;
		} else if (!refSaveType.equals(other.refSaveType))
			return false;
		if (willSaveType == null) {
			if (other.willSaveType != null)
				return false;
		} else if (!willSaveType.equals(other.willSaveType))
			return false;
		return true;
	}
}
