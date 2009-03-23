package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import java.util.*;
import ring.spells.*;
import ring.skills.*;

public class MobileClass implements Serializable {
    public static final long serialVersionUID = 1;
  //This class is for the actual class of the mobile.
  //Internally, MobileClass holds a few different things that make up the class. It holds a list
  //of skills and/or spells, the HD (hit die) type of the class, the classification of the class,
  //the exp adjustment of the class, and so on.

  //Instance variables.

  //hitDie variable.
  //The hitDie instance variable is an int that determines what kind of die is rolled when the mobile
  //levels up in this class. When the mobile leves up, the dice roller rolls 1d(hitDice) and adds
  //the mobile's Con modifier to determine the new amount of HP they get.
  private int hitDie;

  //classification instance variable.
  //This instance variable determines what type of class the MobileClass is. Basically, this
  //tells the MUD what kind of equipment the mobile can use. There are a few different types
  //of classifications. They are described in the constants section of the class below.
  private Classification classification;

  //Spells known, spells available, and spells memorized.
  private SpellList spellsAvailable; //This is a list of spells available to the class.
  private SpellList spellsKnown; //This is the list of spells in the current spellbooks.
  private SpellList spellsMemorized; //This is a list of spells the mobile can cast.

  //A list of ClassFeatures this class has.
  private Vector<ClassFeature> classFeatures;

  //These variables hold the class skill informationfor this MobileClass.
  private int skillPointsPerLevel;
  private SkillList skills;

  //Save and Base Attack Bonus classification variables.
  private SaveType fortSaveType;
  private SaveType refSaveType;
  private SaveType willSaveType;
  private BaseAttackBonus baseAttackBonus;

  //Level variable... This is the level the mobile has in the class.
  private int level;

  //Miscellaneous variables.
  private String name; //The class's name.
  private String displayName; //How the class's name appears in-game.

  //Constants.
  //Classification Constants: These constants are used for determining the type of class that
  //this MobileClass is and what kind of equipment it can use.
  //Please note that these are not the only classes within the game. They are simply a GROUPING
  //of classes.
  //Yes it uses enum! Hooha!
  public enum Classification {
    PHYSICAL("[R][WHITE]Physical Fighter"), //Barbarian, Fighter, Monk, Rogue (HD: 12, 10, 8, 6)
    DIVINE("[R][WHITE]Divine Caster"), //Cleric, Druid (HD: 8)
    ARCANE("[R][WHITE]Arcane Caster"), //Wizard (and specialists), Sorcerer (HD: 4)
    HYBRID("[R][WHITE]Hybrid Class"), //Paladin, Ranger, Bard (HD: 10, 8, 6)
    PSIONIC("[R][WHITE]Psionic"); //Psions (HD: 4)

    private String displayName;

    private Classification(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }

  //Saving throw type. This enum describes if a classes specific saves are good or poor.
  //To calculate a base save bonus, the formulas are as follows:
  //GOOD: trunc((level / 2) + 2)
  //POOR: trunc(level / 3)
  public enum SaveType { GOOD, POOR; }

  //Save enum.
  //This enum describes the three D&D 3.5 saving throw types.
  public enum Save {
    FORTITUDE,
        REFLEX,
        WILL
  }

  //Base Attack Bonus type.
  public enum BaseAttackBonus {
    GOOD(1), //Barbarian, Warrior, Paladin, Ranger
    AVERAGE(.75), //Rogue, Cleric, Druid, Shapeshifter
    POOR(.5); //CASTER, PSION, Occultist, Alchemist

    private double modifier;
    BaseAttackBonus(double modifier) { this.modifier = modifier; }
    public double getModifier() { return modifier; }
  }

  //This constructor is going to be used to make classless mobiles. This means that they have no
  //skills, but their attack bonuses and saves may vary.
  public MobileClass() {
    spellsAvailable = new SpellList();
    spellsKnown = new SpellList();
    spellsMemorized = new SpellList();
    classFeatures = new Vector<ClassFeature>();
  }

  public MobileClass(String name, String displayName, int hitDie, int skillPointsPerLevel,
                     SpellList spells, SkillList skills,
                     SaveType fortSaveType, SaveType refSaveType,
                     SaveType willSaveType,
                     BaseAttackBonus bab, Classification classification) {
    this.name = name;
    this.displayName = displayName;
    this.hitDie = hitDie;
    spellsAvailable = spells;
    spellsKnown = new SpellList();
    spellsMemorized = new SpellList();
    this.skills = skills;
    this.fortSaveType = fortSaveType;
    this.refSaveType = refSaveType;
    this.willSaveType = willSaveType;
    baseAttackBonus = bab;
    this.classification = classification;
    this.skillPointsPerLevel = skillPointsPerLevel;
    classFeatures = new Vector<ClassFeature>();
  }

  public void addClassFeature(ClassFeature feature) {
    classFeatures.addElement(feature);
  }

  public int getHitDie() {
    return hitDie;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getName() {
    return name;
  }

  public SpellList getAvailableSpells() {
    return spellsAvailable;
  }

  public SpellList getKnownSpells() {
    return spellsKnown;
  }

  public SpellList getMemorizedSpells() {
    return spellsMemorized;
  }

  public SkillList getSkillList() {
    return skills;
  }

  public void setSkills(SkillList skills) {
    this.skills = skills;
  }

  public void setSpellsMemorized(SpellList list) {
    spellsMemorized = list;
  }

  public void setSpellsKnown(SpellList list) {
    spellsKnown = list;
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

  public Classification getClassification() {
    return classification;
  }

  public void setClassLevel(int level) {
    this.level = level;
  }

  public int getBaseSaveBonus(Save save) {
    if (save == Save.FORTITUDE) {
      if (fortSaveType == SaveType.POOR) return (int) (level / 3);
      if (fortSaveType == SaveType.GOOD) return (int)((level / 2) + 2);
    }

    if (save == Save.REFLEX) {
      if (refSaveType == SaveType.POOR) return (int)(level / 3);
      if (refSaveType == SaveType.GOOD) return (int)((level / 2) + 2);
    }

    if (save == Save.WILL) {
      if (willSaveType == SaveType.POOR) return (int)(level / 3);
      if (willSaveType == SaveType.GOOD) return (int)((level / 2) + 2);
    }

    return -9999; //should never be hit, but keeps compiler happy
  }

  //registerClassFeature method.
  //adds a class feature to this class. TODO: Add error checking for duplicity
  public void registerClassFeature(ClassFeature feature) {
    classFeatures.addElement(feature);
  }

  //getFeatureByName method.
  //Returns the class feature specified by the name parameter or null if nothing
  //is found. This method is case insensitive.
  public ClassFeature getFeatureByName(String name) {
    System.out.println("in getfeaturebyname");
    ClassFeature ret = null;
    System.out.println("CF SIZE: " + classFeatures.size());
    for (int c = 0; c < classFeatures.size(); c++) {
      ClassFeature cf = classFeatures.get(c);
      if (cf.getName().equalsIgnoreCase(name)) ret = cf;
    }

    return new ClassFeature(ret);
  }
}
