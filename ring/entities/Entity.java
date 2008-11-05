package ring.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import ring.world.*;
import ring.movement.Room;

public class Entity extends WorldObject {
  //This class is for all non-living objects including items, props in a room, etc.
  //Any changes made to this class will affect all things extending from it.

  protected Room currentRoom;
  protected String name;
  protected String idleDescriptor;
  protected String indefiniteDescriptor;
  protected int currentHP;
  protected int maxHP;
  protected int currentAC;

  public Entity() {
    idleDescriptor = "lies here.";
  }

  public String getName() {
    return name;
  }

  public Room getRoom() {
    return currentRoom;
  }

  public String getIdleDescriptor() {
    return idleDescriptor;
  }

  public String getLongDescription() {
    return "Override this!";
  }

  public String getIndefiniteDescriptor() {
    return indefiniteDescriptor;
  }

  public boolean isNPC() {
    return false;
  }

  public boolean isPlayer() {
    return false;
  }

  public Object source() {
    return this;
  }

  public int getEntityType() {
    return 0;
    //Meant to be overriden.
  }

  public int getCurrentHP() { return currentHP; }
  public int getMaxHP() { return maxHP; }
  public int getAC() { return currentAC; }

}
