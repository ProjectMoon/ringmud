package ring.util;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.mobiles.*;

public class Damage {
  //This class outlines damage. Now you might be wondering why we chose to wrap up damage in a
  //class of its own. The reason we put the damage in a class is because damage on this MUD is
  //not simply doubles being subtracted and added. This class handles elemental damage, mobile
  //resistance, and other things.

  //Instance variables.
  //
  //Amount of damage to actually do.
  private double damage;

  //Elemental type of damage.
  private Element element;

  //Default constructor.
  //This shouldn't be called. Sets the damage value to 1.
  public Damage() {
    damage = 1.0;
  }

  //Damage only constructor.
  //This constructor allows the damage amount to be set, the element
  //is none.
  public Damage(double damage) {
    this.damage = damage;
  }

  //Damage and Element constructor.
  //This constructor allows the damage amount and type of damage to be set.
  public Damage(double damage, Element element) {
    this.damage = damage;
    this.element = element;
  }

  //###################################
  //METHODS
  //THIS IS THE METHOD SECTION OF THE CLASS
  //

  //dealDamage method.
  //This method deals damage to the mobile in the parameter. Resistance are applied here. This
  //is is the only method that really needs to be called. It isn't called on the constructor
  //because it may be required for the damage to not actually happen (Saving Throw, etc).
  public boolean dealDamage(Mobile mobile) {
    double mobHP = mobile.getCurrentHP();
    return true;
  }
}
