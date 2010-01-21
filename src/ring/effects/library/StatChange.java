package ring.effects.library;

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
import ring.world.*;
import ring.effects.*;
import ring.mobiles.*;

public class StatChange extends EffectCreator {
  private int[] stats = new int[7];

  public StatChange() { super(); }

  public void effectLife(Affectable target) {
    stats[Mobile.STRENGTH] = super.params.getInt("strength");
    stats[Mobile.DEXTERITY] = super.params.getInt("dexterity");
    stats[Mobile.CONSTITUTION] = super.params.getInt("constitution");
    stats[Mobile.INTELLIGENCE] = super.params.getInt("intelligence");
    stats[Mobile.WISDOM] = super.params.getInt("wisdom");
    stats[Mobile.CHARISMA] = super.params.getInt("charisma");

    Mobile mob = (Mobile)target;
    mob.setStat(Mobile.STRENGTH, mob.getStat(Mobile.STRENGTH) + stats[0]);
    mob.setStat(Mobile.DEXTERITY, mob.getStat(Mobile.DEXTERITY) + stats[1]);
    mob.setStat(Mobile.CONSTITUTION, mob.getStat(Mobile.CONSTITUTION) + stats[2]);
    mob.changeBonusHP((stats[2] / 2) * mob.getLevel()); //change bonus HP to account for con increase.
    mob.setStat(Mobile.INTELLIGENCE, mob.getStat(Mobile.INTELLIGENCE) + stats[3]);
    mob.setStat(Mobile.WISDOM, mob.getStat(Mobile.WISDOM) + stats[4]);
    mob.setStat(Mobile.CHARISMA, mob.getStat(Mobile.CHARISMA) + stats[5]);
  }

  public void effectDeath(Affectable target) {
    Mobile mob = (Mobile)target;
    mob.setStat(Mobile.STRENGTH, mob.getStat(Mobile.STRENGTH) - stats[0]);
    mob.setStat(Mobile.DEXTERITY, mob.getStat(Mobile.DEXTERITY) - stats[1]);
    mob.setStat(Mobile.CONSTITUTION, mob.getStat(Mobile.CONSTITUTION) - stats[2]);
    mob.changeBonusHP(-1 * ((stats[2] / 2) * mob.getLevel()));
    mob.setStat(Mobile.INTELLIGENCE, mob.getStat(Mobile.INTELLIGENCE) - stats[3]);
    mob.setStat(Mobile.WISDOM, mob.getStat(Mobile.WISDOM) - stats[4]);
    mob.setStat(Mobile.CHARISMA, mob.getStat(Mobile.CHARISMA) - stats[5]);
  }
}
