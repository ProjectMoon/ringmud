package ring.magic;

import ring.effects.Affectable;

/**
 * Abstract which provides a method that allows implementation of targeting
 * rules for spells.
 * @author projectmoon
 *
 */
public abstract class TargetingRule {
	public abstract boolean isTargetAllowed(Affectable target);
}
