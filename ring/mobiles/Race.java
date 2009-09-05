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

public class Race implements Serializable {
    public static final long serialVersionUID = 1;
  //This class define's the mobile's race. The Race class in itself is strictly a set of
  //numbers that determine a mobile's HP and other things. It also contains a few other things
  //such as a method that will return a proper body for a race. Basically this class's methods
  //will get called whenever anything relating to race needs to be used.

  //Instance variables.
  //Relatives. The relatives are the amount of power the race has RELATIVE to a certain stat.
  //For example: An illithid is extremely intelligent; moreso than all of the other races.
  //This gives them the highest relative for intelligence (+60). But the illithid is also
  //extremely weak, having a constitution relative of -30. The relative is added on to the stats of
  //the mobile internally. The player does not see it on their score screen though. They will only
  //see 0-100 as the possible stat number. If the illithid has 100 int visibily, he actually has 160
  //internally.
  //
  //Here is the complete listing of all racial relatives as of the latest compilation:
  //Human: Str +0, Con +0, Dex +0, Int +0, Wis +0, Agi +0, Cha +0
  //Drow Elf: Str +0, Con +6, Dex +10, Int +10, Wis -10, Agi +0, Cha -10
  //Moon Elf: Str +0, Con -10, Dex +15, Int +15, Wis +6, Agi +10, Cha +10
  //Ogre: Str +25, Con +30, Dex -15, Int -20, Wis -15, Agi -10, Cha -15
  //Dwarf: Str +10, Con +10, Dex -6, Int +0, Wis +0, Agi +0, Cha +0
  //Duergar: Str +15, Con +10, Dex +6, Int -6, Wis -6, Agi +0, Cha +0
  //Half-Elf: Str +0, Con -6, Dex +10, Int +0, Wis +0, Agi +10, Cha +0
  //Illithid: Str -20, Con -30, Dex +0, Int +60, Wis +20, Agi -25, Cha +10
  //Gnome: Str -10, Con +10, Dex +0, Int +15, Wis +0, Agi +0, Cha +10
  //Troll: Str +15, Con +15, Dex +10, Int -20, Wis -20, Agi +0, Cha -10
  //Aasimar: Str +10, Con +10, Dex +6, Int +10, Wis +0, Agi +0, Cha +15
  //Tiefling: Str +0, Con +0, Dex +15, Int +10, Wis +0, Agi +10, Cha -15

  private int[] relatives = new int[7];

  //Text to display when showing the race of a mobile.
  private String raceName;

  //Body of the race.
  private Body raceBody;

  //Classes this race can play.
  private MobileClass[] classesAllowed;

  //Miscellaneous flags.
  private boolean isPCRace;//Is this race playable by PCs?

  //This will create a default race with relatives of all 0.
  public Race() {

  }

  //determineRace method.
  //This method is used in character creation to create a PC race.
  public static Race determineRace(String choice) {
    choice = choice.toUpperCase();
    if(choice.equalsIgnoreCase("A")) return createHuman();

    if(choice.equalsIgnoreCase("B")) return createElf();

    if(choice.equalsIgnoreCase("C")) return createDwarf();

    if(choice.equalsIgnoreCase("D")) return createHalfElf();

    if(choice.equalsIgnoreCase("E")) return createGnome();

    if(choice.equalsIgnoreCase("F")) return createAasimar();

    if(choice.equalsIgnoreCase("G")) return createDrow();

    if(choice.equalsIgnoreCase("H")) return createOgre();

    if(choice.equalsIgnoreCase("I")) return createDuergar();

    if(choice.equalsIgnoreCase("J")) return createIllithid();

    if(choice.equalsIgnoreCase("K")) return createTroll();

    if(choice.equalsIgnoreCase("L")) return createTiefling();

    return null;
  }

  //This will construct a new race with all fields filled.
  public Race(String raceName, Body raceBody, int[] relatives, MobileClass[] classesAllowed,
              boolean isPCRace) {
    if ((relatives.length < 7) || (relatives.length > 7)) {
      System.out.println("Invalid relative array! Using default values...");
      int relatives2[] = {1, 1, 1, 1, 1, 1, 1};
      relatives = relatives2;
    }

    this.raceName = raceName;
    this.raceBody = raceBody;
    this.relatives = relatives;
    this.classesAllowed = classesAllowed;
    this.isPCRace = isPCRace;
  }

  //######################
  //END CONSTRUCTORS
  //######################

  //######################
  //GET & SET METHODS
  //######################

  //getName method.
  //Returns the full name of the race.
  public String getName() {
    return raceName;
  }

  //getShortName method.
  //Returns the first three letters of the race name.
  public String getShortName() {
    int startPos = raceName.lastIndexOf("]", raceName.length() - 10) + 1;
    return raceName.substring(startPos, startPos + 3);
  }

  //getBody method.
  //Returns the Body object of this Race object.
  public Body getBody() {
    return raceBody;
  }

  //getClassesAllowed method.
  //Returns the available classes this race can have.
  public MobileClass[] getClassesAllowed() {
    return classesAllowed;
  }

  //isPCRace method.
  //Returns if the race is available to players or not.
  public boolean isPCRace() {
    return isPCRace;
  }

  //getStrengthRelative.
  //Returns the strength relative for this race.
  public int getStrengthRelative() {
    return relatives[Mobile.STRENGTH];
  }

  //getDexterityRelative.
  //Returns the dexterity relative for this race.
  public int getDexterityRelative() {
    return relatives[Mobile.DEXTERITY];
  }

  //getConstitutionRelative.
  //Returns the constitution relative for this race.
  public int getConstitutionRelative() {
    return relatives[Mobile.CONSTITUTION];
  }

  //getIntelligenceRelative.
  //Returns the intelligence relative for this race.
  public double getIntelligenceRelative() {
    return relatives[Mobile.INTELLIGENCE];
  }

  //getWisdomRelative.
  //Returns the wisdom relative for this race.
  public double getWisdomRelative() {
    return relatives[Mobile.WISDOM];
  }

  //getCharismaRelative.
  //Returns the charisma relative for this race.
  public double getCharismaRelative() {
    return relatives[Mobile.CHARISMA];
  }

  public void setName(String name) {
    raceName = name;
  }

  //#################################
  //Race Creation Methods
  //These methods create races with a set of relatives and classes allowed.
  //

  //#################################
  //THE METHODS BELOW ARE ALL PC RACE CREATION METHODS.
  //

  //createHuman method.
  //This returns the Human PC race.
  public static Race createHuman() {
    int[] relatives = {0, 0, 0, 0, 0, 0, 0};
    return new Race("[B][YELLOW]Human[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createDrow method.
  //This returns the Drow PC race.
  public static Race createDrow() {
    int[] relatives = {0, 6, 10, 10, -10, 0, -10};
    return new Race("[MAGENTA]Drow[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createElf method.
  //This returns the Elf PC race.
  public static Race createElf() {
    int[] relatives = {0, -10, 15, 15, 6, 10, 10};
    return new Race("[CYAN]Moon Elf[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createOgre method.
  //This returns the Ogre PC race.
  public static Race createOgre() {
    int[] relatives = {25, 30, -15, -20, -15, -10, -15};
    return new Race("[B][BLUE]Ogre[R][WHITE]", Body.createLargeHumanoidBody(), relatives,
                    null, true);
  }

  //createDwarf method.
  //This returns the Dwarf PC race.
  public static Race createDwarf() {
    int[] relatives = {10, 10, -6, 0, 0, 0, 0};
    return new Race("[B][GREEN]Dwarf[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createDuergar method.
  //This returns the Duergar PC race.
  public static Race createDuergar() {
    int[] relatives = {15, 10, 6, -6, -6, 0, 0};
    return new Race("[RED]Duergar[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createHalfElf method.
  //This returns the Half-Elf PC race.
  public static Race createHalfElf() {
    int[] relatives = {0, -6, 10, 0, 0, 10, 0};
    return new Race("[GREEN]Half-Elf[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createIllithid method.
  //This method returns the Illithid PC race.
  public static Race createIllithid() {
    int[] relatives = {-20, -30, 0, 60, 20, -25, 10};
    return new Race("[B][MAGENTA]Illithid[R][WHITE]", Body.createIllithidBody(), relatives,
                    null, true);
  }

  //createGnome method.
  //This method returns the Gnome PC race.
  //Tiefling: Str +0, Con +0, Dex +15, Int +10, Wis +0, Agi +10, Cha -15
  public static Race createGnome() {
    int[] relatives = {0, 0, 15, 10, 0, 10, -15};
    return new Race("[B][RED]Gnome[R][WHITE]", Body.createSmallHumanoidBody(), relatives,
                    null, true);
  }

  //createTroll method.
  //This method returns the Troll PC race.
  public static Race createTroll() {
    int[] relatives = {15, 15, 10, -20, -20, 0, -10};
    return new Race("[GREEN]Troll[R][WHITE]", Body.createLargeHumanoidBody(), relatives,
                    null, true);
  }

  //createAasimar method.
  //This method returns the Aasimar PC race.
  public static Race createAasimar() {
    int[] relatives = {10, 10, 6, 10, 0, 0, 15};
    return new Race("[B][WHITE]Aasimar[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }

  //createTiefling method.
  //This method returns the Tiefling PC race.
  public static Race createTiefling() {
    int[] relatives = {15, 15, 10, -20, -20, 0, -10};
    return new Race("[R][YELLOW]Tiefling[R][WHITE]", Body.createMediumHumanoidBody(), relatives,
                    null, true);
  }
}
