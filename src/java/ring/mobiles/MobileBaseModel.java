package ring.mobiles;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.mobiles.mobclass.MobileClass;
import ring.persistence.RingConstants;

/**
 * Represents the base model of a mobile in the game: its race, alignment,
 * class, etc. The values stored in this model are generally unchanging.
 * Other values are stored in different models, such as a combat model.
 * @author projectmoon
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"level", "strength", "dexterity", "constitution", "intelligence", "wisdom", "charisma",
	"name", "title", "lastName", "description", "height", "gender", "type", "weightLimit", "race",
	"body",	"mobileClass", "alignment", "raceAggressiveList", "alignmentAggressiveList", 
	"aggressive", "dead", "silent", "flying", "floating", "mounted", "fighting",
	"leftHanded", "prone", "lyingDown", "sitting"
})
public class MobileBaseModel {
	//Constants
	public static final int BASE_MAXMV = 100;
	
	//Enum constants.
	public enum Type { 
		MORTAL("Mortal"), LESSER_GOD("Lesser God"), GOD("God"), FORGER("Forger");
		
		private String name;
		Type(String name) {
			this.name = name;
		}
		
		public boolean isGod() { return (this == LESSER_GOD || this == GOD || this == FORGER); }
		public String getName() { return name; }
		
		public String toString() { return getName(); }
	};
	
	public enum Gender {
		MALE("he", "him", "his", "male"),
		FEMALE("she", "her", "her", "female"),
		IT("it", "it", "its", "asexual");
		
		private String subject;
		private String object;
		private String possessive;
		private String noun;
		
		Gender(String subject, String object, String possessive, String noun) {
			this.subject = subject;
			this.object = object;
			this.possessive = possessive;
			this.noun = noun;
		}
		
		public String getNoun() { return noun; }
		public String getSubject() { return subject; }
		public String getObject() { return object; }
		public String getPossessive() { return possessive; }
	};
	
	//Level of the mobile. The maximum level for mortals is 20.
	private int level;
	@XmlElement	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }
	
	//Ability scores.
	private int strength;
	@XmlElement public int getStrength() { return strength;	}
	public void setStrength(int str) { strength = str; }
	
	private int dexterity;
	@XmlElement public int getDexterity() { return dexterity; }
	public void setDexterity(int dex) { dexterity = dex; }
	
	private int constitution;
	@XmlElement public int getConstitution() { return constitution; }
	public void setConstitution(int con) { constitution = con; }
	
	private int intelligence;
	@XmlElement public int getIntelligence() { return intelligence; }
	public void setIntelligence(int intel) { intelligence = intel; }
	
	private int wisdom;
	@XmlElement public int getWisdom() { return wisdom; }
	public void setWisdom(int wis) { wisdom = wis; }
	
	private int charisma;
	@XmlElement public int getCharisma() { return charisma; }
	public void setCharisma(int cha) { charisma = cha; }
	
	//Descriptions: name is stored in WorldObject.
	private String name;
	@XmlElement public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	private String title;
	@XmlElement public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	private String lastName;
	@XmlElement public String getLastName() { return lastName; }
	public void setLastName(String name) { lastName = name;	}
	
	private String lookDescription;
	@XmlElement public String getDescription() { return lookDescription; }
	public void setDescription(String desc) { lookDescription = desc; }
	
	//Misc descriptive variables.
	private double height;
	@XmlElement public double getHeight() { return height; }
	public void setHeight(double height) { this.height = height; }
	
	private Gender gender;
	@XmlElement public Gender getGender() { return gender; }
	public void setGender(Gender gender) { this.gender = gender; }
	
	private Type type;
	@XmlElement public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	
	private double weightLimit; //total amt mob can carry.
	@XmlElement public double getWeightLimit() { return weightLimit; }
	public void setWeightLimit(double limit) { weightLimit = limit; }
	
	//Race of the mobile: human, elf, etc.
	private Race race;
	@XmlElement public Race getRace() { return race; }
	public void setRace(Race race) { this.race = race; }

	//Body of the mobile. This is set by race at first, but the body
	//can be changed by a polymorph, having a limb cut off, etc.
	private Body body;
	@XmlElement public Body getBody() { return body; }
	public void setBody(Body body) { this.body = body; }

	//Class of the mobile.
	//All PlayerCharacters have classes. Some mobiles have classes, but
	//not all.
	private MobileClass mobClass;
	@XmlElement public MobileClass getMobileClass() { return mobClass; }
	public void setMobileClass(MobileClass mobClass) { this.mobClass = mobClass; }
	
	//Alignment of the mob: LG, NG, CG, N, LE, NE, CE.
	private Alignment alignment = new Alignment();
	@XmlElement public Alignment getAlignment() { return alignment; }
	public void setAlignment(Alignment alignment) { this.alignment = alignment; }
	
	//Race-Aggressive list. This is the list of Races the mobile is aggressive
	//to. It overrides the isAggressive flag. NPC mobiles will automatically
	//attack ANY other mobile of the races on this list.
	private List<Race> raceAggressiveList;
	@XmlElement public List<Race> getRaceAggressiveList() { return raceAggressiveList; }
	public void setRaceAggressiveList(List<Race> list) { raceAggressiveList = list; }

	//Alignment-Agressive list. This is the list of alignments the mob is
	//agressive to. It overrides the isAggressive flag. NPC mobiles will
	//automatically attack ANY other mobile of the alignments on this list.
	private List<Alignment> alignmentAggressiveList;
	@XmlElement public List<Alignment> getAlignmentAggressiveList() { return alignmentAggressiveList; }
	public void setAlignmentAggressiveList(List<Alignment> list) { alignmentAggressiveList = list; }
	
	//Flags of the mobile. These are random things that are needed for system
	//use and can affect the way a mob works. They represent various states
	//that the a mobile can be in.
	private boolean isAggressive;//If mob will automatically attack players.
	@XmlElement public boolean isAggressive() { return isAggressive; }
	public void setAggressive(boolean val) { isAggressive = val; }
	
	private boolean isDead;// If mob is dead.
	@XmlElement public boolean isDead() { return isDead; }
	public void setDead(boolean val) { isDead = val; }
	
	private boolean isSilent;// If mob can speak or not.
	@XmlElement public boolean isSilent() { return isSilent; }
	public void setSilent(boolean val) { isSilent = val; }
	
	private boolean isFlying;// If mob is flying.
	@XmlElement public boolean isFlying() { return isFlying; }
	public void setFlying(boolean val) { isFlying = val; }
	
	private boolean isFloating;// If mob is floating with a levitate effect.
	@XmlElement public boolean isFloating() { return isFloating; }
	public void setFloating(boolean val) { isFloating = val; }
	
	private boolean isMounted;// If mob is mounted on a horse or something.
	@XmlElement public boolean isMounted() { return isMounted; }
	public void setMounted(boolean val) { isMounted = val; }
	
	private boolean isFighting;// If mob is fighting.
	@XmlElement public boolean isFighting() { return isFighting; }
	public void setFighting(boolean val) { isFighting = val; }
	
	private boolean isLeftHanded;// If mob is left-handed or right-handed.
	@XmlElement public boolean isLeftHanded() { return isLeftHanded; }
	public void setLeftHanded(boolean val) { isLeftHanded = val; }
	
	private boolean isProne;// If mob is prone.
	@XmlElement public boolean isProne() { return isProne; }
	public void setProne(boolean val) { isProne = val; }
	
	private boolean isLyingDown;// If mob is lying down.
	@XmlElement public boolean isLyingDown() { return isLyingDown; }
	public void setLyingDown(boolean val) { isLyingDown = val; }
	
	private boolean isSitting;// If mob is sitting.
	@XmlElement public boolean isSitting() { return isSitting; }
	public void setSitting(boolean val) { isSitting = val; }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alignment == null) ? 0 : alignment.hashCode());
		result = prime
				* result
				+ ((alignmentAggressiveList == null) ? 0
						: alignmentAggressiveList.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + charisma;
		result = prime * result + constitution;
		result = prime * result + dexterity;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + intelligence;
		result = prime * result + (isAggressive ? 1231 : 1237);
		result = prime * result + (isDead ? 1231 : 1237);
		result = prime * result + (isFighting ? 1231 : 1237);
		result = prime * result + (isFloating ? 1231 : 1237);
		result = prime * result + (isFlying ? 1231 : 1237);
		result = prime * result + (isLeftHanded ? 1231 : 1237);
		result = prime * result + (isLyingDown ? 1231 : 1237);
		result = prime * result + (isMounted ? 1231 : 1237);
		result = prime * result + (isProne ? 1231 : 1237);
		result = prime * result + (isSilent ? 1231 : 1237);
		result = prime * result + (isSitting ? 1231 : 1237);
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + level;
		result = prime * result
				+ ((lookDescription == null) ? 0 : lookDescription.hashCode());
		result = prime * result
				+ ((mobClass == null) ? 0 : mobClass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		result = prime
				* result
				+ ((raceAggressiveList == null) ? 0 : raceAggressiveList
						.hashCode());
		result = prime * result + strength;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		temp = Double.doubleToLongBits(weightLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + wisdom;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileBaseModel other = (MobileBaseModel) obj;
		if (alignment == null) {
			if (other.alignment != null)
				return false;
		} else if (!alignment.equals(other.alignment))
			return false;
		if (alignmentAggressiveList == null) {
			if (other.alignmentAggressiveList != null)
				return false;
		} else if (!alignmentAggressiveList
				.equals(other.alignmentAggressiveList))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (charisma != other.charisma)
			return false;
		if (constitution != other.constitution)
			return false;
		if (dexterity != other.dexterity)
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (Double.doubleToLongBits(height) != Double
				.doubleToLongBits(other.height))
			return false;
		if (intelligence != other.intelligence)
			return false;
		if (isAggressive != other.isAggressive)
			return false;
		if (isDead != other.isDead)
			return false;
		if (isFighting != other.isFighting)
			return false;
		if (isFloating != other.isFloating)
			return false;
		if (isFlying != other.isFlying)
			return false;
		if (isLeftHanded != other.isLeftHanded)
			return false;
		if (isLyingDown != other.isLyingDown)
			return false;
		if (isMounted != other.isMounted)
			return false;
		if (isProne != other.isProne)
			return false;
		if (isSilent != other.isSilent)
			return false;
		if (isSitting != other.isSitting)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (level != other.level)
			return false;
		if (lookDescription == null) {
			if (other.lookDescription != null)
				return false;
		} else if (!lookDescription.equals(other.lookDescription))
			return false;
		if (mobClass == null) {
			if (other.mobClass != null)
				return false;
		} else if (!mobClass.equals(other.mobClass))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (race == null) {
			if (other.race != null)
				return false;
		} else if (!race.equals(other.race))
			return false;
		if (raceAggressiveList == null) {
			if (other.raceAggressiveList != null)
				return false;
		} else if (!raceAggressiveList.equals(other.raceAggressiveList))
			return false;
		if (strength != other.strength)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (Double.doubleToLongBits(weightLimit) != Double
				.doubleToLongBits(other.weightLimit))
			return false;
		if (wisdom != other.wisdom)
			return false;
		return true;
	}
}
