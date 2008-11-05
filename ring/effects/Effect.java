package ring.effects;

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

import java.util.Vector;
public class Effect {
  //This class represents an "effect". This effect could be a spell, a skill, or any other thing.
  //It does NOT contain what the effect does however. The main purpose of this class is to allow
  //and facilitate the implementation of timers on spells, items, etc. For example, a spell that
  //increases armor for 30 minutes would create an Effect with a 30 minute timer on it. Once these
  //30 minutes are up, the Effect will die and the decast(...) method of the spell will be called.
  //
  //Spells and the like are linked to this Effect class through EffectCreator. In the EffectCreator
  //interface, there is the effectLife(...) and effectDeath(...) method. When the instance of Effect
  //is created, it calls the EffectCreator's effectLife method. Spells should ONLY instantiate an effect;
  //the rest is handled by the system.
  //
  //Once the Effect's timer reaches zero (It is generally controlled through the Mobile class),
  //The effectDeath method is called.
  //A spell will call decast(...) when an effect dies (Through the effectDeath(...) method).

  //Target of this effect.
  Affectable target;

  //EffectCreator vector.
  //This vector holds a list of EffectCreators that this effect uses. Simple enough.
  private Vector<EffectCreator> effectCreators;

  //Duration variable.
  private Duration duration;

  //Timer variable... Measured in ticks.
  private int timer;

  //Is this effect supposed to be removed?
  private boolean dead;

  //Duration enum: Helps measure ... duration.
  public enum Duration {
    INSTANT, //this effect is instant
    TIMED, //this effect has a timed duration: see private int timer
    PERIODIC_TIMED, //this effect has a timed duration, but it calls effectLife() every time the effect is processed.
    PERMANENT; //this effect is permanent until removed through external means. usually used by items.
  }

  private int numStarts; //keeps track of # times startEffect was called. used to properly remove periodic effects.

  //this boolean allows setting and checking of some attributes to further help describe this effect
  //certain attributes are set automatically depending on the constructor:
  // * if target is not specified, INIT_TARGET_LATER is set.
  // * if timer is set to zero on a TIMED effect, INIT_TIMER_LATER is set.
  // * REMOVE_ON_DEATH is set automatically unless specified otherwise.

  public boolean INIT_TARGET_LATER = false;
  public boolean INIT_TIMER_LATER = false;
  public boolean INIT_EFFECT_LATER = false;
  public static final int SET_TIMER_LATER = 0;

  public Effect(Duration dur, int timer, Affectable target, EffectCreator ... efcs) {
    effectCreators = new Vector<EffectCreator>();
    for (EffectCreator ef : efcs) effectCreators.addElement(ef);
    this.timer = timer;
    this.target = target;
    duration = dur;
    dead = false;
    INIT_TARGET_LATER = false;
    numStarts = 0;
    if ( (dur == Duration.TIMED) && (timer == 0)) INIT_TIMER_LATER = true;
    System.out.println("Effect generated for: " + this +
                       ". Status: [INACTIVE]");
  }

  public Effect(Duration dur, int timer, EffectCreator ... efcs) {
    effectCreators = new Vector<EffectCreator>();
    for (EffectCreator ef : efcs) effectCreators.addElement(ef);
    this.timer = timer;
    target = null;
    duration = dur;
    dead = false;
    INIT_TARGET_LATER = true;
    numStarts = 0;
    if ( (dur == Duration.TIMED) && (timer == 0)) INIT_TIMER_LATER = true;
    System.out.println("Effect generated for: " + this +
                       ". Status: [INACTIVE]");
  }

  //This copy constructor is used when starting up effects in order to create a new, unique
  //instance. This prevents strange timing issues when applying the same effect multiple times
  //at once. It is up to the Effect-encapsulating classes (Spell, ClassFeature, Item, etc) to deal
  //with this issue. They call uniqueInstance() to do this.
  private Effect(Effect other) {
    effectCreators = other.effectCreators;
    timer = other.timer;
    target = other.target;
    duration = other.duration;
    dead = other.dead;
    INIT_TARGET_LATER = other.INIT_TARGET_LATER;
    INIT_EFFECT_LATER = other.INIT_EFFECT_LATER;
    INIT_TIMER_LATER = other.INIT_TIMER_LATER;
    numStarts = 0; //this is a new effect, so numStarts is still zero.
  }

  public void decrementTimer() {
    if (duration.equals(Duration.PERMANENT))return; //ignore permanent duration effects. they are removed other ways.
    if (timer <= 0) {
      endEffect();
      return;
    }
    else {
      if (duration == Duration.PERIODIC_TIMED) startEffect(); //if this is a periodic effect, make it happen again!
      timer--;
    }
  }

  //setTimer method.
  //This allows the timer to be set to some postive number. It can only be set if INIT_TIME_LATER is set to true.
  //After it is set, INIT_TIME_LATER goes false and can no longer be set. Returns true if successful,
  //false otherwise.
  public boolean setTimer(int n) {
    timer = n;
    return true;
  }

  public boolean isDead() {
    return dead;
  }

  public void startEffect() {
    numStarts++;
    if (duration == Duration.INSTANT) dead = true; //dead on arrival?
    for (EffectCreator ef : effectCreators) ef.effectLife(target);
  }

  public void endEffect() {
    dead = true;
    for (int c = 0; c < numStarts; c++) //call effectDeath a number of times equal to the number of effectLife calls.
      for (EffectCreator ef : effectCreators) ef.effectDeath(target);

    numStarts = 0; //reset the numStarts counter for the next time this Effect is enacted.
  }

  //uniqueInstance method.
  //This returns a unique instance of this Effect. Gets rid of timing issues and many other things.
  public Effect uniqueInstance() {
    return new Effect(this);
  }

  //passParameters method.
  //This method passes parameters to all EffectCreators in this Effect. Returns true
  //if succesful in adding parameters to all EffectCreators, but false if one fails.
  public boolean passParameters(EffectCreatorParameters params) {
    boolean success = true;
    for (EffectCreator ef : effectCreators) {
      if (success == false)
        ef.setParameters(params);
      else
        success = ef.setParameters(params);
    }
    return success;
  }

  public Affectable getTarget() {
    return target;
  }

  public void setTarget(Affectable target) {
    this.target = target;
  }

  public Duration getDuration() {
    return duration;
  }

  public String toString() {
    String text = "[Effect]: ";
    for (EffectCreator ef : effectCreators) {
      text += ef.toString() + ";";
    }

    return text;
  }
}
