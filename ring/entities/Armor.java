package ring.entities;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import ring.mobiles.*;
import ring.effects.*;
import java.util.Vector;

public class Armor extends Item {
    public static final long serialVersionUID = 1;
  //This class represents a piece of armor in the world. The AC system is based off of 3rd edition,
  //so the higher the AC amount is, the better it protects you.

  private int AC;
  private Race[] racesAllowed;//Races that can wear this armor piece.
  private MobileClass[] classesAllowed;//Classes that can wear this armor piece.
  private Alignment[] alignmentsAllowed;//Alignments that can wear this armor piece.

  public Armor() {
    int AC = 0;
    super.passiveEffects = null;
    super.partWornOn = BodyPart.createUniquePart(Body.HEAD);
    super.name = "ERROR";
    super.wearable = true;
  }

  //Creates a basic piece of armor that anyone can use.
  public Armor(int AC, Effect effects, BodyPart bodyPart, String name, String indefiniteDescriptor,
               String idleDescriptor) {
    this.AC = AC;
    super.name = name;
    super.indefiniteDescriptor = indefiniteDescriptor;
    super.idleDescriptor = idleDescriptor;
    super.wearable = true;
    super.partWornOn = bodyPart;
    super.passiveEffects = effects;
  }

  //Most specific constructor... Creates a very specific piece of armor that factors in race,
  //class, and alignment.
  public Armor(int AC, Effect effects, BodyPart bodyPart, String name, String indefniteDescriptor,
               String idleDescriptor, Race[] racesAllowed, MobileClass[] classesAllowed,
               Alignment[] alignmentsAllowed) {
    this.AC = AC;
    super.partWornOn = bodyPart;
    super.name = name;
    super.indefiniteDescriptor = indefiniteDescriptor;
    super.idleDescriptor = idleDescriptor;
    this.racesAllowed = racesAllowed;
    this.classesAllowed = classesAllowed;
    this.alignmentsAllowed = alignmentsAllowed;
    super.passiveEffects = effects;
  }

  //Methods!

  //checkAlignment method.
  //This method returns a true or false depending on the alignment of the mobile that is passed
  //to it. It is used when seeing if a mobile can wear the armor or not.
  public boolean checkAlignment(Mobile mob) {
    //Is there even an alignment of the armor to check? If there isn't anyone can wear it.
    if (alignmentsAllowed == null) return true;

    //Else... continue on.
    Alignment mobAlignment = mob.getAlignment();

    for (int c = 0; c < alignmentsAllowed.length; c++) {
      if (mobAlignment.equals(alignmentsAllowed[c])) {
        return true;
      }
    }

    return false;
  }

  //checkRace method.
  //This method returns a true or false depending on the race of the mobile that is passed to it.
  //It is used when checking if a mobile can wear this armor piece or not.
  public boolean checkRace(Mobile mob) {
      //Is there even a race of the armor to check? If there isn't anyone can wear it.
      if (racesAllowed == null) return true;

      //Else... continue on.
      Race mobRace = mob.getRace();

      for (int c = 0; c < alignmentsAllowed.length; c++) {
        if (mobRace.equals(racesAllowed[c])) {
          return true;
        }
      }

      return false;
  }

  //checkClass method.
  //This method returns a true or false depending on the class of the mobile that is passed to it.
  //It is used when checking if a mobile can we1ar this armor piece or not.
  public boolean checkClass(Mobile mob) {
    //Is there even a class of the armor to check? If there isn't anyone can wear it.
    if (classesAllowed == null) return true;

    //Else... continue on.
    MobileClass mobClass = mob.getMobileClass();

    for (int c = 0; c < alignmentsAllowed.length; c++) {
      if (mobClass.equals(classesAllowed[c])) {
        return true;
      }
    }

    return false;
  }


  //getAC method.
  //This method returns the AC of the armor piece.
  public int getAC() {
    return AC;
  }

  //getName method.
  public String getName() {
    return super.name;
  }

}
