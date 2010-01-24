package ring.skills;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.persistence.RingConstants;
import ring.util.Dice;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"description",
	"classSkill",
	"ranks",
	"abilityScore",
	"bonus"
})
/**
 * Represents a skill in the game. A skill is composed of several things:
 * number of ranks, the key ability, and any other miscellaneous
 * bonuses. It can then be rolled as a d20 check. Key ability score modifier
 * is transient to JAXB, classes using the skill must set the modifier based
 * on the key ability score. Skills that are considered class skills for a
 * Mobile receive full benefit from ranks. Non-class skills only receive
 * half benefit from those ranks.
 * @author projectmoon
 *
 */
public class Skill {
	@XmlType
	public static enum KeyAbilityScore {
		STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA;
	}
	
	private String name;
	private String description;
	
	private int ranks;
	private KeyAbilityScore abilityScore;
	private int abilityModifier;
	private int bonus;
	private boolean isClassSkill;
	
	@XmlElement
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		description = desc;
	}
	
	@XmlElement
	public int getRanks() {
		if (isClassSkill()) {
			return ranks;
		}
		else {
			return (ranks / 2);
		}
	}
	
	public void setRanks(int ranks) {
		this.ranks = ranks;
	}
	
	@XmlElement
	public KeyAbilityScore getAbilityScore() {
		return abilityScore;
	}
	
	public void setAbilityScore(KeyAbilityScore score) {
		abilityScore = score;
	}
	
	@XmlTransient
	public int getAbilityModifier() {
		return abilityModifier;
	}
	
	public void setAbilityModifier(int mod) {
		abilityModifier = mod;
	}
	
	@XmlElement
	public int getBonus() {
		return bonus;
	}
	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	@XmlElement
	public boolean isClassSkill() {
		return isClassSkill;
	}
	
	public void setClassSkill(boolean val) {
		isClassSkill = val;
	}
	
	public int roll() {
		int skillMod = getRanks() + getAbilityModifier() + getBonus();
		Dice dice = new Dice(1, 20, skillMod);
		return dice.roll();
	}
	
	public int take20() {
		return getRanks() + getAbilityModifier() + getBonus() + 20;
	}
	
	public int take10() {
		return getRanks() + getAbilityModifier() + getBonus() + 10;	
	}
}
