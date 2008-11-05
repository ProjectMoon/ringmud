package ring.entities;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.spells.*;
import ring.damage.*;
import ring.mobiles.*;
import ring.effects.*;
import java.util.*;

public class Weapon extends Item {
  public static int SWORD = 0;
  public static int DAGGER = 1;
  public static int STAFF = 2;
  public static int BOW = 3;
  public static int AXE = 4;
  public static int MACE = 5;
  public static int HAMMER = 6;
  public static int WAND = 8;

  //Certain necessary things.
  private WeaponDamage damage;
  private int type;
  private Effect procEffects;

  //Weapon flags!
  private boolean twoHanded;
  private boolean cursed;

  public Weapon(String name, String indefiniteDescriptor, BodyPart bodyPart,
                String idleDescriptor, WeaponDamage damage, int type,
                Effect passiveEffects, Effect procEffects) {
    super.name = name;
    super.idleDescriptor = idleDescriptor;
    this.damage = damage;
    if ((type < SWORD) || (type > WAND)) type = SWORD;
    this.type = type;
    super.passiveEffects = passiveEffects;
    this.procEffects = procEffects;
    super.partWornOn = bodyPart;
    super.wearable = true;
  }

  public WeaponDamage getDamage() {
    return damage;
  }

  public String getName() {
    return super.name;
  }

  public int getType() {
    return type;
  }

  public Effect getProcEffects() {
    return procEffects;
  }

}
