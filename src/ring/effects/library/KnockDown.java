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

@SuppressWarnings("serial")
public class KnockDown extends EffectCreator {
  //This EffectCreator defines an effect that knocks its target it down. This sets it
  //into a prone status. The target must then stand back up on its own.

  public KnockDown() { super(); }

  public void effectLife(Affectable target) {
    if (!(target instanceof Mobile)) return;
   else {
     Mobile mob = (Mobile) target;
     mob.getBaseModel().setProne(true);
   }
  }

  public void effectDeath(Affectable target) {
    //by the effect's nature it is instant. therefore, the death
    //method does nothing because the mob can get up on its own.
    //this may be changed later to enact some sort of "prone lock"
    //to support effects that force a mob to stay prone for X amount of time.
  }
}
