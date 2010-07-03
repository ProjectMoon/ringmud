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
import ring.effects.Affectable;
import ring.effects.EffectCreator;
import ring.mobiles.Mobile;

@SuppressWarnings("serial")
public class StatChange extends EffectCreator {
	private int str, dex, con, intel, wis, cha;
	private int oldStr, oldDex, oldCon, oldIntel, oldWis, oldCha;
	
	public StatChange() {
		super();
	}

	public void effectLife(Affectable target) {
		str = super.params.getInt("strength");
		dex = super.params.getInt("dexterity");
		con = super.params.getInt("constitution");
		intel = super.params.getInt("intelligence");
		wis = super.params.getInt("wisdom");
		cha = super.params.getInt("charisma");
		
		Mobile mob = (Mobile) target;
		
		oldStr = mob.getBaseModel().getStrength();
		oldDex = mob.getBaseModel().getDexterity();
		oldCon = mob.getBaseModel().getConstitution();
		oldIntel = mob.getBaseModel().getIntelligence();
		oldWis = mob.getBaseModel().getWisdom();
		oldCha = mob.getBaseModel().getCharisma();
		
		mob.getBaseModel().setStrength(str);
		mob.getBaseModel().setDexterity(dex);
		mob.getBaseModel().setConstitution(con);
		mob.getBaseModel().setIntelligence(intel);
		mob.getBaseModel().setWisdom(wis);
		mob.getBaseModel().setCharisma(cha);
	}

	public void effectDeath(Affectable target) {
		Mobile mob = (Mobile) target;

		mob.getBaseModel().setStrength(oldStr);
		mob.getBaseModel().setDexterity(oldDex);
		mob.getBaseModel().setConstitution(oldCon);
		mob.getBaseModel().setIntelligence(oldIntel);
		mob.getBaseModel().setWisdom(oldWis);
		mob.getBaseModel().setCharisma(oldCha);
	}
}
