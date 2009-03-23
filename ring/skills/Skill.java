package ring.skills;

/**
 * <p>Title: RingMUD Codebase</p>
 *
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar
 * to DikuMUD</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: RaiSoft/Thermetics</p>
 *
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import java.util.*;

public class Skill implements Serializable {
    public static final long serialVersionUID = 1;
  //This class represents a skill that a MobileClass has. It is a concrete class that holds a few
  //different variables that are used to calculate checks. These checks are used with certain
  //commands and other things. The Skills themselves are presently hardcoded into the game but will
  //eventually be parsed from the MobileClass files. An example: Ride is a skill the fighter has.
  //It allows handling of animals and such. It doesn't have a command, but is passively used at times
  //to control mounts and such things.

  private String name;
  private String description;
  private int keyAbility;
  private int keyAbilityScore;
  private int keyAbilityModifier;
  private int ranks;
  private int miscModifier; //a composite number that adds up all other bonuses and penalties to this skill.

  //This constructor is used to initialize a generic skill. See the bottom one for
  //the one commonly used for players and npcs.
  public Skill(String name, String description, int keyAbility) {
    this.name = name;
    this.description = description;
    this.keyAbility = keyAbility;
    this.keyAbilityModifier = 0;
    this.ranks = 0;
  }

  //This constructor is used for new player/npc initialization as well as setting up the player's
  //skill list when they log in.
  public Skill(String name, String description, int keyAbility, int keyAbilityScore, int ranks) {
    this.name = name;
    this.description = description;
    this.keyAbility = keyAbility;
    this.keyAbilityScore = keyAbilityScore;
    this.keyAbilityModifier = (int)((keyAbilityScore - 10) / 2);
    this.ranks = ranks;
}

  public int makeCheck() {
    //Replace this stuff with the dice roller...
    Random gen = new Random(System.nanoTime());
    return gen.nextInt(20) + keyAbilityModifier + ranks + miscModifier;
  }

  public final String getName(){
    return name;
  }

  public final String getKeyAbilityAsString() {
    return "finish this";
  }

  public final int getKeyAbility() {
    return keyAbility;
  }

  public final int getKeyAbilityModifier() {
    return keyAbilityModifier;
  }

  public final int getRanks() {
    return ranks;
  }

  //incrementRanks method.
  //Increases the ranks of this skill. Called when a player levels up and assigns
  //new skill points (automatically or otherwise).
  public final void incrementRanks() {
    ranks++;
  }

  //changeMiscModifier method.
  //Adds (or subtracts) a number to the misc modifier.
  public final void changeMiscModifier(int num) {
    miscModifier += num;
  }

}
