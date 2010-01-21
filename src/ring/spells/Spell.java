package ring.spells;

/*
The Spell class represents a spell in the game. Depending on the type of spell (ROOM, SINGLE_TARGET, or MULTIPLE_TARGET),
the addEffect method of all applicable targets will be called. A Spell is comprised of the following things (encapsulated
in this class):
 * An Effect. This defines what the Spell does. Multiple Effect support to be added.
 * A Duration:
    -INSTANT: This signifies an effect that happens immediately and is removed.
    -TIMED: Passing a number to a spell's duration parameter makes it last that many ticks.
    -PERMANENT: This signifies an effect that cannot be removed.
 * A target. This can be a single target or the entire room. In the case of single, the target is stored in the Effect.
 * A name. This is a string representing the name of the spell.
 * A level. This signifies when a class gets the spell (if it does). NOTE THAT THIS CAN BE CHANGED BY AN EXTERNAL SOURCE (i.e. SpellList)
 * Various boolean flags. They should be self-explanatory; see below.
*/


import java.io.Serializable;
import ring.effects.*;

public class Spell implements Serializable {
    public static final long serialVersionUID = 1;
  //duration constants
  public static final int INSTANT = 0;
  public static final int PERMANENT = -1;

  private int targetType;
  private int level;
  private String name;
  private boolean targetsItems;
  private boolean targetsMobiles;
  private boolean targetsEntities;
  private int timer;//For the effect.
  private Effect effect;

  //targeting constants
  public static final int ROOM = 0;
  public static final int SINGLE_TARGET = 1;

  //Constructor method.
  public Spell(String name, int level, Effect eff) {
    this.name = name;
    this.level = level;
    effect = eff;
    targetType = SINGLE_TARGET;
  }

  //cast method.
  //This method takes care of casting the spell. It adds the effect to the spell's current target and then the effect
  //does its thing, and wears off by itself. It throws a NullPointerException if the target is null.
  public final void cast() throws NullPointerException {
    if (effect.getTarget() == null) throw(new NullPointerException("spell target can\'t be null!"));
    effect.getTarget().addEffect(effect); //probably make this less redundant.

  }

  public final int getLevel() {
    return level;
  }

  public final void setLevel(int lvl) {
    level = lvl;
  }

  public final String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public final int getTargetType() {
    return targetType;
  }

  public final int getSpellTimer() {
    return timer;
  }

  public final boolean targetsItems() {
    return targetsItems;
  }

  public final boolean targetsMobiles() {
    return targetsMobiles;
  }

  public final boolean targetsEntities() {
    return targetsEntities;
  }

  //generateEffect method.
  //This method simply returns the Effect the spell uses.
  public final Effect generateEffect() {
    return effect;
  }
}
