package ring.nrapi.magic;

import java.util.List;

public abstract class AbstractMagicSystem<M extends SpellMetadataImplementation> implements MagicSystem<M> {
	private SpellCaster caster;

	@Override
	public SpellCaster getSpellCaster() {
		return caster;
	}
	

	@Override
	public void setSpellCaster(SpellCaster caster) {
		if (isCompatibleWith(caster)) {
			this.caster = caster;
		}
		else {
			//TODO throw exception
		}
	}
	
	@Override public abstract boolean isCompatibleWith(SpellCaster caster);
	@Override public abstract List<Spell> getSpells();
	@Override public abstract void addSpell(Spell spell);
	@Override public abstract boolean canCast(Spell spell);
	@Override public abstract List<Spell> getCastableSpells();
	@Override public abstract M getMetadata(Spell spell);
	@Override public abstract void refreshSpells();
	@Override public abstract void refreshSpells(int amount);
	@Override public abstract void removeSpell(Spell spell);
	@Override public abstract void setSpells(List<Spell> spells);
	@Override public abstract void spellCast(Spell spell);
	@Override public abstract void spellFizzled(Spell spell);

}
