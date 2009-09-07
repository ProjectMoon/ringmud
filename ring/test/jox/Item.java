package ring.test.jox;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import ring.mobiles.*;
import ring.effects.*;
import java.util.*;

public class Item extends ring.entities.Entity {
    public static final long serialVersionUID = 1;
  private int uniqueID;
  protected int weight;
  protected boolean wearable;
  protected BodyPart partWornOn;
  protected Effect passiveEffects;//Magical passive effects of this item.

  public static int WEAPON = 0;
  public static int ARMOR = 1;
  public static int FOOD = 2;

  public Item() {
    super.gettable = true;
  }

  public final boolean wearable() {
    return wearable;
  }

  public final boolean isArmor() {
    return true;
  }

  public final boolean isWeapon() {
    return true;
  }

  public final Effect getPassiveEffects() {
    return passiveEffects;
  }

  public final BodyPart partWornOn() {
    return partWornOn;
  }

  public final void setPartWornOn(BodyPart part) {
    partWornOn = part;
  }

  public int getEntityType() {
    return NON_LIVING;
  }

  //This is meant to be overridden... It's always called when a Mobile wears an item. However,
  //most of the time it does nothing since it is not overridden. When overriden this method
  //will contain special code to make the item do something sexy...
  //Yes I realize that certain Items will have a .class file all to themselves but that's ok...
  //I forsee an item scripting language in the future !
  public void specialCode(Affectable wearer) {}
}
