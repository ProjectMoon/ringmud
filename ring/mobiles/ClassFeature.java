package ring.mobiles;

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

import ring.effects.*;
import java.util.*;
public class ClassFeature implements Comparable {
  //This class defines a class feature (as per D&D 3.5) for a MobileClass. A class feature is a unique ability that a class
  //gets that is derived from levels. For example, a Barbarian's rage is a class feature. Class features in the D&D 3.5 system
  //do many things; they could not all possibly be covered by a computer system. A class feature in RingMUD holds the following
  //information:
  // * Name of the class feature
  // * Description of the class feature
  // * Type of class feature: SINGLE or SCALING. SINGLE types are gotten once and that's it. SCALING types improve as a character
  //       levels up. Other types may be implemented later.
  // * Duration type: ACTIVE or PASSIVE. ACTIVE requires a command to be executed; PASSIVE is always on.
  // * A Vector<Effect> that holds all of the effects of this class feature. The feature is defined by the Effects, which are
  //       in turn defined by EffectCreators.
  //
  //PASSIVE class features are PERMANENT duration Effects that get applied as soon as the character logs in or gains a level
  //and the appropriate class feature. ACTIVE class features are usually TIMED duration Effects, but sometimes INSTANT.

  //The two enum definitions for the types (see above)
  public enum Type {
    SINGLE, SCALING;
  }

  public enum DurationType {
    ACTIVE, PASSIVE;
  }

  private String name; //the name of this class feature
  private String description; //the extended description of this class feature
  private Type type; //the type of class feature
  private DurationType durationType; //the duration type of this class feature
  private int timer = 0; //the duration of an active feature. zero if it's instant or permanent.
  private Effect featureEffects; //the effects (i.e. "rules") of this class feature
  private int levelAcquired; //the level this class feature is first gained.
  private int levelScalePattern; //the amount of levels it takes before a SCALING class feature improves.
  private Affectable target; //this is the target for the next use of this class feature; often its owner.

  //There are three separate constructors, each with varying parameters. The following parameters are required for all versions:
  //Name, description, type, duration type.
  //Please see each individual constructors for the different parameters they take.
  public ClassFeature(ClassFeature other) {
    name = other.name;
    description = other.description;
    type = other.type;
    durationType = other.durationType;
    featureEffects = other.featureEffects;
    levelAcquired = other.levelAcquired;
    levelScalePattern = other.levelScalePattern;
    target = other.target;
  }

  public ClassFeature(String name, String description, Type type,
                      DurationType durationType, int levelAcquired,
                      int levelScalePattern) {
    this.name = name;
    this.description = description;
    this.levelAcquired = levelAcquired;
    this.levelScalePattern = levelScalePattern;
    this.type = type;
    this.durationType = durationType;
  }

  public ClassFeature(String name, String description, Effect effects,
                      Type type, DurationType durationType, int levelAcquired,
                      int levelScalePattern) {
    this.name = name;
    this.description = description;
    this.levelAcquired = levelAcquired;
    this.featureEffects = effects;
    this.levelScalePattern = levelScalePattern;
    this.type = type;
    this.durationType = durationType;
  }

  public ClassFeature(String name, String description, Effect effects,
                      Type type, DurationType durationType) {
    this.name = name;
    this.description = description;
    featureEffects = effects;
    this.levelAcquired = 1;
    this.levelScalePattern = 0;
    this.type = type;
    this.durationType = durationType;
  }

  //addEffect method.
  //This method, while bearing the same name as WorldObject.addEffect, is not quite the same. It has some error checking and
  //throws exceptions if the conditions are not met. These are the conditions of addEffect:
  //If this class feature is PASSIVE, only PERMANENT duration Effects can be added.
  //If it is ACTIVE, any type of Effect can be added.
  public void setEffect(Effect e) throws IllegalArgumentException {
    if ( (durationType == DurationType.PASSIVE) &&
        (e.getDuration() != Effect.Duration.PERMANENT))throw new
        IllegalArgumentException(
        "passive class feature can only accept permanent effects.");
    else featureEffects = e;
  }

  //activate method.
  //This method activates the class feature, regardless of duration type. The caveat is that PASSIVE class features cannot be
  //turned off once activated. A PASSIVE feature will usually be activated on level up or login.
  public void activate() {
      //This uniqueInstance() call here is necessary to create a unique instantiation of this effect.
      //If this is not done, strange timing issues happen when applying the same effect twice.
      Effect ef = featureEffects.uniqueInstance();
      ef.setTarget(target);
      ef.setTimer(timer);
      target.addEffect(ef);
  }

  //This method is currently commented out because it does not seem necessary. Plus, it runs into problems with the
  //unique instance nature of the activate() method. While this may be a flaw in the design of the Effects system,
  //it will be dealt with for now.
  //deactivate method.
  //This method turns a class feature off. It throws IllegalArgumentException if this feature is PASSIVE.
  /*
  public void deactivate() throws IllegalArgumentException {
    if (durationType == DurationType.PASSIVE) throw(new IllegalArgumentException("cannot deactivate passive class features!"));
    else {
      for (Effect e : featureEffects) {
        e.endEffect();
      }
    }
  }
  */

  //registerTarget method.
  //This sets the target for the next use of this class feature. It's usually the class feature's owner.
  public void registerTarget(Affectable t) {
    target = t;
  }

  //registerDuration method.
  //This sets the timer for the next use of this class feature.
  public void registerTimer(int n) {
    timer = n;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getLevelAcquired() {
    return levelAcquired;
  }

  public int getLevelScalingPattern() {
    return levelScalePattern;
  }

  private Effect getEffects() {
    return featureEffects;
  }

  //compareTo method.
  //This method compares a ClassFeature to another object. It returns -1 if the ClassFeature is not identical.
  //Otherwise it returns 0.
  //Details:
  //-1 if other is not a ClassFeature
  //-1 if other is a ClassFeature but does not hold identical information as this
  //0 if all conditions evaluate to true.
  public int compareTo(Object other) {
    if (!(other instanceof ClassFeature)) return -1;
    ClassFeature otherFeature = (ClassFeature)other;
    if ((this.name.equals(otherFeature.name)) && (this.description.equals(otherFeature.description)) &&
        (this.featureEffects.equals(otherFeature.featureEffects)) && (this.type == otherFeature.type) &&
        (this.durationType == otherFeature.durationType) && (this.levelAcquired == otherFeature.levelAcquired) &&
        (this.levelScalePattern == otherFeature.levelScalePattern)) return 0;
    else return -1;
  }

}
