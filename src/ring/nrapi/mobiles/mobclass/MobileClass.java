package ring.nrapi.mobiles.mobclass;

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
}
