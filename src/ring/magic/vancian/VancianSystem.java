package ring.magic.vancian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ring.magic.AbstractMagicSystem;
import ring.magic.Spell;
import ring.magic.SpellCaster;
import ring.mobiles.Mobile;

public class VancianSystem extends AbstractMagicSystem<VancianMetadata> {
	private List<Spell> spellList = new ArrayList<Spell>();
	private Map<Spell, VancianMetadata> metadata = new HashMap<Spell, VancianMetadata>();
	private List<Spell> memorizedSpells = new ArrayList<Spell>();
	private Mobile caster;
	
	@Override
	public void setSpellCaster(SpellCaster caster) {
		if (isCompatibleWith(caster)) {
			this.caster = (Mobile)caster;
		}
	}
	
	@Override
	public boolean isCompatibleWith(SpellCaster caster) {
		if (caster instanceof Mobile) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void addSpell(Spell spell) {
		spellList.add(spell);
		VancianMetadata md = new VancianMetadata();
		md.transform(spell.getMetadata());
		metadata.put(spell, md);
	}

	@Override
	public boolean canCast(Spell spell) {
		//Check if spell is on list
		if (!spellList.contains(spell)) {
			return false;
		}
		
		//Check if the spell is memorized
		if (!memorizedSpells.contains(spell)) {
			return false;
		}
		
		//Make sure that the caster has 10 + the level of the spell
		//as the key ability score.
		int score = 0;
		if (spell.getSource().equalsIgnoreCase("arcane")) {
			score = caster.getBaseModel().getIntelligence();
		}
		else if (spell.getSource().equalsIgnoreCase("divine")) {
			score = caster.getBaseModel().getWisdom();
		}
		
		VancianMetadata md = getMetadata(spell);
		if (score < md.getSpellLevel()) {
			return false;
		}
		
		//Get level of spell from metadata
		return true;
	}

	@Override
	public List<Spell> getCastableSpells() {
		return memorizedSpells;
	}

	@Override
	public VancianMetadata getMetadata(Spell spell) {
		return metadata.get(spell);
	}

	@Override
	public List<Spell> getSpells() {
		return spellList;
	}

	@Override
	public void refreshSpells() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshSpells(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSpell(Spell spell) {
		//remove all memorized versions first.
		while (memorizedSpells.contains(spell)) {
			memorizedSpells.remove(spell);
		}
		
		//now remove from spell list
		spellList.remove(spell);
	}

	@Override
	public void setSpells(List<Spell> spells) {
		spellList = spells;
	}

	@Override
	public void spellCast(Spell spell) {
		//Remove from memorized list.
		
	}

	@Override
	public void spellFizzled(Spell spell) {
		//Maybe remove from memorized list?
	}

}
