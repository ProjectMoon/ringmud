package ring.nrapi.magic;

import java.util.List;

/**
 * Interface for magic systems. A magic system is a programmatic
 * implementation of game rules regarding spellcasting. This interface
 * responds to events such as a spell being cast, fizzling, etc. The
 * system is responsible for maintaining its own internal state.
 * @author projectmoon
 *
 */
public interface MagicSystem<M extends SpellMetadataImplementation> {
	/**
	 * Determines if this MagicSystem works with the specified type of
	 * SpellCaster. For example, a magic system might only be compatible
	 * with Mobiles.
	 * @param caster
	 * @return true or false
	 */
	public boolean isCompatibleWith(SpellCaster caster);
	
	/**
	 * Gets the SpellCaster object for this MagicSystem.
	 * @return
	 */
	public SpellCaster getSpellCaster();
	
	/**
	 * Sets the SpellCaster object for this MagicSystem.
	 * @param caster
	 */
	public void setSpellCaster(SpellCaster caster);
	
	/**
	 * Whether or not the specified spell can be cast by this MagicSystem.
	 * Reasons for failure include not being on the spell list, not having
	 * enough of the spellcasting resource, etc.
	 * @param spell
	 * @return
	 */
	public boolean canCast(Spell spell);
	
	/**
	 * Spell casted event.
	 * @param spell
	 */
	public void spellCast(Spell spell);
	
	/**
	 * Spell fizzled event.
	 * @param spell
	 */
	public void spellFizzled(Spell spell);
	
	/**
	 * Refills the supply of spells to maximum levels. This could be called
	 * by a command that allows the caster to memorize all of their spells,
	 * or it could refill mana.
	 */
	public void refreshSpells();
	
	/**
	 * Refreshes the supply of spells by the given amount. Amount in this case
	 * refers to a level that is implementation specific. It could be mana, a
	 * number of spells to refresh, or something else.
	 * @param amount
	 */
	public void refreshSpells(int amount);

	/**
	 * Returns the list of spells that, at this moment, this
	 * MagicSystem is capable of casting.
	 * @return
	 */
	public List<Spell> getCastableSpells();
	
	/**
	 * Returns the list of spells in this MagicSystem.
	 * @return
	 */
	public List<Spell> getSpells();
	
	/**
	 * Sets the list of spells in this MagicSystem.
	 * @param spells
	 */
	public void setSpells(List<Spell> spells);
	
	/**
	 * Gets the metadata implementation for the specified Spell.
	 * @param spell
	 * @return
	 */
	public M getMetadata(Spell spell);
	
	public void addSpell(Spell spell);
	
	public void removeSpell(Spell spell);
}
