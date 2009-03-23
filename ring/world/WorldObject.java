package ring.world;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.entities.*;
import ring.movement.*;
import ring.effects.*;
import java.util.Vector;
import ring.mobiles.*;
import java.io.Serializable;

public abstract class WorldObject implements Affectable, Serializable {
    public static final long serialVersionUID = 1;
  //This class, as it is aptly named, represents an object in the world. "object" in this case is something physically
  //present in the world, not a java object (although each of these objects are java objects...). This class was
  //made to facilitate things such as getRoom() and Effects. EVERY WorldObject is considered Affectable and Serializable. This means
  //it can targeted with Effects. Individual Effects may have restrictions on what type of WorldObjects they affect, however.
  //For instance, most EffectCreators only deal with mobiles. Two common types of Affectable WorldObjects are Entities and
  //Mobiles.
  //various things.
  protected boolean gettable;
  protected int weight;
  
  //This object represents the world object's location in the world.
  protected transient ZoneCoordinate location;

  //This is a vector of current effects that must be looped through and dealt with each tick.
  //Each WorldObject (i.e. things that derive this class) is responsible for dealing with this list
  //in its own way. Therefore there is nothing enforced on classes below this one that MAKES them deal
  //with this list. Most WorldObjects that should be targetable by Effects deal with them in their
  //processTick method.
  protected Vector effectsList;

  public WorldObject() {}
  
  //getLocation method.
  //gets the location of this WorldObject.
  public final ZoneCoordinate getLocation() {
	  return location;
  }
  
  //setLocation method.
  //sets the location of this WorldObject. Returns true on success.
  public final boolean setLocation(ZoneCoordinate loc) {
		location = loc;
		return true;
  }
  
  //addEffect method.
  //Adds an Effect to the WorldObject.
  public final void addEffect(Effect e) {
    System.out.println("in addeffect");
    if (e.getTarget() != this) {
      System.out.println("***WARNING***\nInbound effect [" + e + "] is not targeting WorldObject [" + this + "]. Operation aborted.");
      return;
    }

    effectsList.addElement(e);
    System.out.println("added element/starting effect");
    e.startEffect();
    System.out.println("started effect");
  }

  public boolean gettable() {
    return gettable;
  }

  public abstract String getName();

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  //All of the following are meant to be overriden, but they don't necessarily have to be.
  public String getLongDescription() { return "[WorldObject] override this."; }
  public Object source() { return this; }
  public boolean isPlayer() { return false; }
  public boolean isNPC() { return false;}
  public int getEntityType() { return 0;}
  public int getCurrentHP() { return 0;}
  public int getMaxHP() { return 0; }
  public int getAC() { return 0; }
  public void sendData(String text) {}

}
