package ring.nrapi.mobiles;

import java.util.List;

/**
 * Represents the base model of a mobile in the game: its race, alignment,
 * class, etc. The values stored in this model are generally unchanging.
 * Other values are stored in different models, such as a combat model.
 * @author projectmoon
 */
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
	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }
	
	//Ability scores.
	private int strength;
	public int getStrength() { return strength;	}
	public void setStrength(int str) { strength = str; }
	
	private int dexterity;
	public int getDexterity() { return dexterity; }
	public void setDexterity(int dex) { dexterity = dex; }
	
	private int constitution;
	public int getConstitution() { return constitution; }
	public void setConstitution(int con) { constitution = con; }
	
	private int intelligence;
	public int getIntelligence() { return intelligence; }
	public void setIntelligence(int intel) { intelligence = intel; }
	
	private int wisdom;
	public int getWisdom() { return wisdom; }
	public void setWisdom(int wis) { wisdom = wis; }
	
	private int charisma;
	public int getCharisma() { return charisma; }
	public void setCharisma(int cha) { charisma = cha; }
	
	//Descriptions: name is stored in WorldObject.
	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	private String lastName;
	public String getLastName() { return lastName; }
	public void setLastName(String name) { lastName = name;	}	
	
	//Misc descriptive variables.
	private double height;
	public double getHeight() { return height; }
	public void setHeight(double height) { this.height = height; }
	
	private Gender gender;
	public Gender getGender() { return gender; }
	public void setGender(Gender gender) { this.gender = gender; }
	
	private Type type;
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	
	private double weightLimit; //total amt mob can carry.
	public double getWeightLimit() { return weightLimit; }
	public void setWeightLimit(double limit) { weightLimit = limit; }
	
	//Race of the mobile: human, elf, etc.
	private Race race;
	public Race getRace() { return race; }
	public void setRace(Race race) { this.race = race; }

	//Body of the mobile. This is set by race at first, but the body
	//can be changed by a polymorph, having a limb cut off, etc.
	private Body body;
	public Body getBody() { return body; }
	public void setBody(Body body) { this.body = body; }

	//Class of the mobile.
	//All PlayerCharacters have classes. Some mobiles have classes, but
	//not all.
	private MobileClass mobClass;
	public MobileClass getMobileClass() { return mobClass; }
	public void setMobileClass(MobileClass mobClass) { this.mobClass = mobClass; }
	
	//Alignment of the mob: LG, NG, CG, N, LE, NE, CE.
	private Alignment alignment;
	public Alignment getAlignment() { return alignment; }
	public void setAlignment(Alignment alignment) { this.alignment = alignment; }
	
	//Race-Aggressive list. This is the list of Races the mobile is aggressive
	//to. It overrides the isAggressive flag. NPC mobiles will automatically
	//attack ANY other mobile of the races on this list.
	private List<Race> raceAggressiveList;
	public List<Race> getRaceAggressiveList() { return raceAggressiveList; }
	public void setRaceAggressiveList(List<Race> list) { raceAggressiveList = list; }

	//Alignment-Agressive list. This is the list of alignments the mob is
	//agressive to. It overrides the isAggressive flag. NPC mobiles will
	//automatically attack ANY other mobile of the alignments on this list.
	private List<Alignment> alignmentAggressiveList;
	public List<Alignment> getAlignmentAggressiveList() { return alignmentAggressiveList; }
	public void setAlignmentAggressiveList(List<Alignment> list) { alignmentAggressiveList = list; }
	
	//Flags of the mobile. These are random things that are needed for system
	//use and can affect the way a mob works. They represent various states
	//that the a mobile can be in.
	private boolean isAggressive;//If mob will automatically attack players.
	public boolean isAggressive() { return isAggressive; }
	public void setAggressive(boolean val) { isAggressive = val; }
	
	private boolean isDead;// If mob is dead.
	public boolean isDead() { return isDead; }
	public void setDead(boolean val) { isDead = val; }
	
	private boolean isDeaf;// If mob is deaf.
	public boolean isDeaf() { return isDeaf; }
	public void setDeaf(boolean val) { isDeaf = val; }
	
	private boolean isBlind;// If mob is blind.
	public boolean isBlind() { return isBlind; }
	public void setBlind(boolean val) { isBlind = val; }
	
	private boolean isSilent;// If mob can speak or not.
	public boolean isSilent() { return isSilent; }
	public void setSilent(boolean val) { isSilent = val; }
	
	private boolean isFlying;// If mob is flying.
	public boolean isFlying() { return isFlying; }
	public void setFlying(boolean val) { isFlying = val; }
	
	private boolean isFloating;// If mob is floating with a levitate effect.
	public boolean isFloating() { return isFloating; }
	public void setFloating(boolean val) { isFloating = val; }
	
	private boolean isMounted;// If mob is mounted on a horse or something.
	public boolean isMounted() { return isMounted; }
	public void setMounted(boolean val) { isMounted = val; }
	
	private boolean isFighting;// If mob is fighting.
	public boolean isFighting() { return isFighting; }
	public void setFighting(boolean val) { isFighting = val; }
	
	private boolean isLeftHanded;// If mob is left-handed or right-handed.
	public boolean isLeftHanded() { return isLeftHanded; }
	public void setLeftHanded(boolean val) { isLeftHanded = val; }
	
	private boolean isProne;// If mob is prone.
	public boolean isProne() { return isProne; }
	public void setProne(boolean val) { isProne = val; }
	
	private boolean isLyingDown;// If mob is lying down.
	public boolean isLyingDown() { return isLyingDown; }
	public void setLyingDown(boolean val) { isLyingDown = val; }
	
	private boolean isSitting;// If mob is sitting.
	public boolean isSitting() { return isSitting; }
	public void setSitting(boolean val) { isSitting = val; }
		
	//Movement-related variables.
	//The current movement points (MV) of the mobile.
	private int currentMV;
	public int getCurrentMV() { return currentMV; }
	public void setCurrentMV(int mv) { currentMV = mv; }
	public void changeCurrentMV(int amount) { currentMV += amount; }
	
	//The maximum movement points (MV) of the mobile.
	//This number is calculated as follows:
	//BASE_MAXMV + (10 * con mod). BASE_MAXMV is currently 100.
	private int maxMV;
	public int getMaxMV() { return maxMV; }
	public void setMaxMV(int mv) { 
		maxMV = mv;
		if (maxMV < currentMV) {
			currentMV = maxMV;
		}
	}

	//subtract an extra amount of MV per room move equal to this * default move
	//amount.
	private int movementMultiplier;
	public int getMovementMultilplier() { return movementMultiplier; }
	public void setMovementMultiplier(int mov) { movementMultiplier = mov; }
	public void increaseMovementMultiplier(int mov) { movementMultiplier += mov; }
	public void resetMovementMultiplier() {	movementMultiplier = 0; }

	//The speed of the mobile in feet.
	private int speed;
	public int getSpeed() { return speed; }
	public void setSpeed(int speed) { this.speed = speed; }
	public void changeSpeed(int change) { speed += change; }
}
