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

public class HPChange extends EffectCreator {
	private static final long serialVersionUID = 1L;
	private int hpChange;

	public HPChange() {
		super();
	}

	public void effectLife(Affectable target) {
		if (!(target instanceof Mobile))
			return; // only mobiles are valid targets
		hpChange = super.params.getInt("amount");
		System.out.println("changing HP by " + hpChange);
		Mobile mob = (Mobile) target;
		mob.changeBonusHP(hpChange);
	}

	public void effectDeath(Affectable target) {
		if (!(target instanceof Mobile))
			return; // only mobiles are valid targets
		System.out.println("removing HP change of " + hpChange);
		Mobile mob = (Mobile) target;
		mob.changeBonusHP(hpChange * -1);
	}
}
