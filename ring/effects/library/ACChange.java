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

import ring.effects.*;
import ring.mobiles.*;

public class ACChange extends EffectCreator {
  private int acChange;

  public ACChange() { super(); }

  public void effectLife(Affectable target) {
    acChange = super.params.getInt("ACChange:amount");
    Mobile mob = (Mobile)target;
    mob.changeAC(acChange);
  }

  public void effectDeath(Affectable target) {
    Mobile mob = (Mobile)target;
    mob.changeAC(acChange * -1);
  }
}
