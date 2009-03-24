package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.entities.*;
import ring.movement.*;
import ring.effects.*;
import ring.players.*;
import ring.world.*;
import ring.commands.*;
import java.util.*;

import ring.spells.*;
import ring.skills.*;

//This is the class that all mobiles (NPC and PC) extend from. This class will hold basic
//information for a mobile such as body shape, HP, race, etc.
public class Mobile extends WorldObject implements CommandSender, TickerListener {
    public static final long serialVersionUID = 1;
	//CONSTANTS NEEDED BY ALL MOBILES
	//STAT CONSTANTS
	public static final int STRENGTH = 0;
	public static final int DEXTERITY = 1;
	public static final int CONSTITUTION = 2;
	public static final int INTELLIGENCE = 3;
	public static final int WISDOM = 4;
	public static final int CHARISMA = 5;
	//MONEY CONSONANTS
	public static final int COPPER = 0;
	public static final int SILVER = 1;
	public static final int GOLD = 2;
	public static final int PLATINUM = 3;
	//GENDER CONSTANTS
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public static final int IT = 2;
	//TYPE CONSTANTS
	public static final int MORTAL = 0;
	public static final int LESSER_GOD = 1;
	public static final int GOD = 2;
	public static final int FORGER = 3;
	//INVENTORY CONSTANTS
	public static final int MAX_ITEMS_IN_INV = 10;
	//MOVEMENT CONSTANTS
	public static final int BASE_MAXMV = 100;


	//Instance variables needed by all mobiles.

	//The maximum HP of the mobile. We don't need a maximum mana because the MUD has no mana
	//system. Spells are prepared and do not use mana.
	private int maxHP;

	//The current HP of the mobile. If it goes below -10, the mobile's isDead boolean value
	//is set to true. This, of course, will kill the mobile.
	private int currentHP;

	//The HP bonus of the mobile. This number is added on to the maxHP variable when HP is being
	//displayed. It is also added on to the currentHP variable...
	private int bonusHP;

	//The generic attack bonus of the mobile. This number is a composite of all modifiers applied
	//to the mobiles attacks, bonuses and penalties.
	private int attackBonus;

	//The stat instance variables are the BASE stats of the mobile. They are used in
	//determining a lot of things. There are an entire separate set of stats for stats
	//modified through the use of items and such.
	//
	//NOTE: The stats effects on mobiles are modified by the Race class as well. The stat
	//numbers themselves DO NOT change. However, the Race of a mob will determine how powerful
	//each point of the stat is to the mob.
	//
	//For example: The Illithid Race template will have an intelligence relative of 1.6
	//compared to the Ogre's .7. These relative numbers are used in determining things such as
	//memorizing times, max weight limit, base AC, base HP, and other things.
	private int[] stats = new int[6];

	//Temporary stat instance variables. BY DEFAULT, METHODS THAT RETURN A STAT VALUE WILL
	//RETURN THESE. At the beginning they are simply set to the same as the base stats. But
	//the wearing of certain items and certain spells can increase or decrease certain stats.
	//This is what these variables are for. The values should be automatically updated by
	//calls to item methods.
	private int[] tempStats = new int[6];

	//TO ACCESS STATS, CALL THE getStat(int stat) AND getBaseStat(int stat) METHODS.

	//Money-related variables.
	private int[] money = new int[4];
	private int[] bankBalance = new int[4];

	//Race of the mobile. This affects the stat relatives.
	private Race race;

	//Body of the mobile. This is generally created by the Race, but the body can be changed
	//by a polymorph or having a limb cut off.
	private Body body;

	//Class of the mobile. All PlayerCharacters will have a class. Some mobiles have classes,
	//but not all necessarily do. The class affects the skill list and level of the mob.
	private MobileClass mobClass;

	//Level of the mobile. The maximum level (for non-god players) is 20. This may be increased over time with epic
	//content.
	private int level;

	//Armor class of the mobile. This determines how hard the mobile is to hit.
	private int currentAC;
	
	//Current target of the mobile. This will be used as an automatic parameter during combat when using hostile
	//actions in combat if no parameter is actively specified. It is also the mobile's current auto-attack target.
	private Affectable currentTarget;

	//Inventory.
	Vector inventory;
	Vector tempInv;//May be needed for something so we'll put it in.

	//Race-Aggressive list. This is the list of Races the mobile is aggressive to. For mobiles
	//that attack any PC that comes into their room, they are simply aggressive to all races.
	//NOTE: Agressiveness will only work against PCs anyway. Mobiles will not attack other
	//other mobiles on their own, at least not yet.
	private Vector<Race> raceAggressiveList;

	//Alignment-Agressive list. This is the list of alignments the mob is agressive to.
	//They will only attack PCs of a certain alignment. It is stored in a vector of Alignment
	//objects. See Alignment class for more details
	private Vector<Alignment> alignmentAggressiveList;

	//Alignment of the mob. The alignment is stored in a class called Alignment.
	private Alignment alignment;

	//Flags of the mobile. These are random things that are needed for system use and can
	//affect the way a mob works. They are all public variables because defining a bunch of
	//get and set methods is just superfluous and takes too much effort. You will note that
	//there are some get and set methods relating to these flags. This is from when they used
	//to be private. They are still there for legacy purposes to make older parts of the
	//code work. Eventually those parts will be converted to direct variable calls. If you are
	//modifying or adding code, please use the new way--i.e. "if (mob.isGod)", etc.
	public boolean isAggressive;//If mob is agressive. This is used for NPCs to determine if they attack players automatically.
	public boolean isDead;//If mob is dead.
	public boolean isGod;//If mob is a god/staff member on the MUD.
	public boolean isDeaf;//If mob is deaf.
	public boolean isBlind;//If mob is blind.
	public boolean isSilent;//If mob can cast spells with verbal components or not. Also if it can speak.
	public boolean isFlying;//If mob is flying.
	public boolean isFloating;//If mob is floating with a levitate effect.
	public boolean isMounted;//If mob is mounted on a horse or something.
	public boolean isFighting;//If mob is fighting.
	public boolean isLeftHanded;//If mob is left-handed or right-handed.
	public boolean isLocked;//If the mob is "locked."
	public boolean isProne;//If mob is prone.
	public boolean isLyingDown;//If mob is lying down.
	public boolean isSitting;//If mob is sitting.
	public boolean moves;//Used for NPCs to tell if they move from room to room or not.

	//Random other things.
	private double weight;//Weight of the mob.
	private double weightCarried;
	private double weightLimit;
	private double height;
	private int gender;
	private int type;
	
	//Movement-related variables.
	//The current movement points (MV) of the mobile. This is stored as a double because the system
	//for calculating movement point expenditure can return decimals. However, it is always
	//displayed as a ROUNDED UP integer.
	private int currentMV;

	//The maximum movement points (MV) of the mobile. This is stored as a double as well for the same
	//reasons as above. This number is calculated as follows:
	//BASE_MAXMV + (10 * con mod). BASE_MAXMV is currently 100.
	private int maxMV;
	
	//subtract an extra amount of MV per room move equal to this * default move amount.
	private int movementMultiplier;
	
	//The speed of the mobile in feet.
	private int speed;
        
	//Skill check variables:
	//These variables somewhat violate the extensible design philosophy of the MUD, but as far as I can see,
	//they are currently necessary. These are for things that need to be hardcoded into the game. Namely
	//search, hide, move silently, spot, and listen checks.
	//
	//LATER, the Skill and SkillList classes should be re-done to cover these variables. They will still
	//effectively be "hardcoded," but in a more extensible manner (yes, I know that makes no sense).
	public int hideCheck;
	public int moveSilentlyCheck;
	public int spotCheck;
	public int listenCheck;
	public int hiddenExitSearchCheck = 0; //current search check for viewing hidden exits. reset every room change.

	//Descriptions
	private String name;
	private String title;
	private String lastName;
	private String textOnLook;//aka the long text

	//CommandHandler. This CommandHandler is protected so it can drop down into the PlayerCharacter
	//class. It is transient because it is not serialized
	protected transient CommandHandler handler;
        
	//lockTimeRemaining: The time left before this mob can act again.
	protected int lockTimeRemaining;
	protected String lockMessage = "You are currently focused on an activity."; //message to display while this mob is locked.
	protected String lockFinishedMessage = "You become aware of the world again."; //message to display when this mob becomes unlocked.

	//Creates a generic mobile.
	public Mobile() {
		handler = new CommandHandler(this);
		this.name = "Generic Mobile";
		this.textOnLook = "";
		this.race = null;
		this.body = null;
		this.stats = new int[] {1, 1, 1, 1, 1, 1};
		tempStats = this.stats;
		this.money = new int[] {2};
		maxHP = 5;
		currentHP = 5;
		this.mobClass = null;
		this.level = 1;
		location = null;
		this.alignment = null;
		this.alignmentAggressiveList = null;
		this.raceAggressiveList = null;
		isAggressive = true;
		isFlying = true;
		effectsList = new Vector();
		maxMV = BASE_MAXMV;
		currentMV = BASE_MAXMV;
	}

	//Creates a mobile with all fields filled out.
	public Mobile(String name, String textOnLook, Race race, Body body, int[] stats,
			int[] money, int hp, MobileClass mobClass, int level, Alignment alignment, int gender,
			Vector<Alignment> alignmentAggressiveList, Vector<Race> raceAggresiveList, ZoneCoordinate location,
			boolean aggressive) {
		this.name = name;
		this.textOnLook = textOnLook;
		this.race = race;
		this.body = body;
		this.stats = stats;
		tempStats = stats;
		this.money = money;
		maxHP = hp;
		this.mobClass = mobClass;
		this.level = level;
		this.location = location;
		this.alignment = alignment;
		this.alignmentAggressiveList = alignmentAggressiveList;
		this.raceAggressiveList = raceAggressiveList;
		isAggressive = aggressive;
		effectsList = new Vector();
		maxMV = BASE_MAXMV + (10 * getModifier(Mobile.CONSTITUTION));
		currentMV = maxMV;
		//World.getWorld().notifyGods("A mobile has been created at zone " + zone.getName() +
		//		"(Index: " + zone.getIndex() + ") in Room #" +
		//		room.getIndex());
	}

	//END CONSTRUCTORS
	//#####################################################


        public void init() {
            handler = new CommandHandler(this);
        }
        

	//***********************
	//BEGIN FLAG AND GET METHODS

	//hasSkill method.
	//This method checks to see if the mob has a certain skill. It can be used either with a String
	//or a Skill object. It is case insensitive.
	public boolean hasSkill(String skillName) {
		MobileClass mobClass = this.getMobileClass();
		if (mobClass == null) return false;
		else {
			SkillList mobSkills = mobClass.getSkillList();
			Skill res = mobSkills.getSkillByName(name);
			if (res != null) return true;
			else return false;
		}
	}

	//hasSkill method.
	//Skill object version.
	public boolean hasSkill(Skill skill) {
		MobileClass mobClass = this.getMobileClass();
		if (mobClass == null) return false;
		else {
			SkillList mobSkills = mobClass.getSkillList();
			boolean res = mobSkills.hasSkill(skill);
			return res;
		}
	}

	//isPlayer method.
	//This checks to see if the mobile is a player. This is used in numerous method that only
	//or only need to affect the players.
	public boolean isPlayer() {
		return this instanceof PlayerCharacter;
	}

	//isNPC method.
	public boolean isNPC() {
		return this instanceof NPC;
	}


	//getName method.
	//Returns name.
	public String getName() {
		return name;
	}

	//getInventory method.
	//Returns the inventory vector.
	public Vector getInventory() {
		return inventory;
	}

	//getModifier method.
	//Returns the specified TEMPORARY STAT MODIFIER.
	public int getModifier(int stat) {
		return (tempStats[stat] - 10) / 2;
	}

	//getBaseModifier method.
	//Returns the specified BASE STAT MODIFIER.
	public int getBaseModifier(int stat) {
		return (stats[stat] - 10) / 2;
	}

	//getStat method.
	//Returns the specified TEMPORARY STAT.
	public int getStat(int stat) {
		if ((stat < STRENGTH) || ( stat > CHARISMA)) return tempStats[0];

		else return tempStats[stat];
	}

	//getBaseStat method.
	//Returns the specified BASE STAT.
	public int getBaseStat(int stat) {
		if ((stat < STRENGTH) || ( stat > CHARISMA)) return stats[0];

		else return stats[stat];
	}

	//getGender method.
	//Returns the INTEGER representation of the mobile's gender.
	public int getGender() {
		return gender;
	}

	//getHimHerIt method.
	//Returns "him," "her," or "it."
	public String getHimHerIt() {
		if (gender == MALE) return "him";
		if (gender == FEMALE) return "her";
		if (gender == IT) return "it";

		return "it";
	}

	//getHisHerIts method.
	//Returns the possessive form of the gender.
	public String getHisHerIts() {
		if (gender == MALE) return "his";
		if (gender == FEMALE) return "her";
		if (gender == IT) return "its";

		return "its";
	}

	//getHeSheIt method.
	//Returns the subject form of the gender.
	public String getHeSheIt() {
		if (gender == MALE) return "he";
		if (gender == FEMALE) return "she";
		if (gender == IT) return "it";

		return "it";
	}

	//getMaleFemaleIt method.
	//Returns the noun form of the gender.
	public String getMaleFemaleIt() {
		if (gender == MALE) return "male";
		if (gender == FEMALE) return "female";
		if (gender == IT) return "asexual";

		return "asexual";
	}

	//getType method.
	//Returns the type of the mobile.
	public int getType() {
		return type;
	}

	//getLongDescription method.
	//Returns the long description of this mobile. DO LTER!
	public String getLongDescription() {
		return textOnLook;
	}

	//setLongDescription method.
	public void setLongDescription(String text) {
		textOnLook = text;
	}

	//source method.
	public Object source() {
		return this;
	}

	//getTypeString method.
	//Returns a string representation of the mobile.
	public String getTypeString() {
		if (type == MORTAL) return "MORTAL";
		if (type == LESSER_GOD) return "LESSER DEITY";
		if (type == GOD) return "DEITY";
		if (type == FORGER) return "FORGER";

		return "NONE";
	}

	//getAvailableSkills method.
	//Returns the list of available skills of this mobile. It is simply a shortcut.
	public SkillList getSkillList() {
		return mobClass.getSkillList();
	}

	//getSpeed method.
	//gets the current speed of this mobile.
	public int getSpeed() {
		return speed;
	}
	
	//setSpeed method.
	//set the speed of this mobile.
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	//getCurrentMV method.
	//gets the current MV points of the mobile.
	public int getCurrentMV() {
		return currentMV;
	}
	
	//getMaxMV method.
	//gets the max MV points of the mobile.
	public int getMaxMV() {
		return maxMV;
	}
	
	//getCurrentMVString method.
	//This method returns the current MV as a string, rounded up.
	public String getCurrentMVString() {
		return String.valueOf(currentMV);
	}
	
	//changeCurrentMV method.
	//changes the current MV points of the mobile. Send negative #s for deductions.
	public void changeCurrentMV(int change) {
		currentMV += change;
	}

	//################################
	//END GET METHODS
	//BEGIN SET METHODS
	//################################

	//setDescription method.
	//This method sets the description of the mobile when looked at.
	public void setDescription(String desc) {
		textOnLook = desc;
	}

	//setName method.
	//This method sets the name of the mobile.
	public void setName(String name) {
		this.name = name;
	}

	//setLastName method.
	//This method sets the last name of the mobile.
	public void setLastName(String name) {
		lastName = name;
	}

	//setTitle method.
	//This method sets the title of the mobile.
	public void setTitle(String title) {
		this.title = title;
	}

	//setBody method.
	//This sets the bodily features of the mobile. This is called generally from a
	//shapechanging spell such as polymorph. However, it may also be called if the
	//mobile loses a body part.
	public void setBody(Body body) {
		this.body = body;
	}

	//setRace method.
	//This method sets the Race of the mobile. Changing the race of a mobile can change
	//the relatives of all the stats. This can make for some possible problems so be
	//wary of calling this method.
	public void setRace(Race race) {
		this.race = race;
	}

	//setMobileClass method.
	//Sets the MobileClass of the mobile.
	public void setMobileClass(MobileClass mc) {
		mobClass = mc;
	}

	//setAlignment method.
	//This method sets the alignment of the mobile.
	public void setAlignment(Alignment a) {
		alignment = a;
	}

	//changeBonusHP method.
	//This method sets the HP bonus of the mobile. NOTE THAT WHENEVER THIS IS CALLED... THE
	//CURRENT HP IS CHANGED!!!!!!!!!
	//This method can also accept negative numbers. It wouldn't exactly be a bonus but... :)
	//The bonusHP is a compilation of all buffs and debuffs applied to the mobile (hence why it
	//has a += instead of just an = on the assignment statement).
	public void changeBonusHP(int HP) {
		bonusHP += HP;
		currentHP += HP;
	}

	//changeAttackBonus method.
	//This method adds (or subtracts) a number to the mobile's attack bonus. The attack bonus
	//is a composite bonus comprised of all modifiers to the mob's attack rolls EXCEPT its base
	//attack bonus.
	public void changeAttackBonus (int bonus) {
		attackBonus += bonus;
	}

	//changeAC method.
	//This method changes the AC of the mobile by the amount passed to it.
	//It can accept positive or negative numbers.
	public void changeAC(int n) {
		currentAC += n;
	}

	//increaseMovementMultiplier method.
	//This method increases the mobile's movement multiplier. The movement multiplier is an extra amount
	//of move points (MV) that get taken off when the mobile goes from room to room. This is useful for hide,
	//move silently, rough terrain, and other things.
	public void increaseMovementMultiplier(int mov) {
		movementMultiplier += mov;
	}

	//setMovementMultiplier method.
	//This sets the movement multiplier.
	public void setMovementMultiplier(int mov) {
		movementMultiplier = mov;
	}

	//resetMovementMultilplier.
	//This clears the movement multiplier.
	public void resetMovementMultiplier() {
		movementMultiplier = 0;
	}

	//getMovementMultilplier.
	//This gets the movement multiplier.
	public int getMovementMultilplier() {
		return movementMultiplier;
	}

	//setStat method.
	//This method changes one of the TEMPORARY STATS of a mobile.
	public void setStat(int stat, int value) {
		tempStats[stat] = value;
	}

	//setBaseStat method.
	//This method changes one of the BASE STATS of a mobile.
	public void setBaseStat(int stat, int value) {
		stats[stat] = value;
	}

	//This returns the race NAME of the mobile. Used when displaying the mobile's race... duh.
	public String getRaceString() {
		return race.getName();
	}

	//getRace method.
	//This method returns the race of the mobile.
	public Race getRace() {
		return race;
	}

	//getMobileClass method.
	//This returns the MobileClass of the mobile.
	public MobileClass getMobileClass() {
		return mobClass;
	}

	//getLevel method.
	//This method gets the level of the mobile.
	public int getLevel() {
		return level;
	}

	//This returns the alignment of the mobile.
	public Alignment getAlignment() {
		return alignment;
	}

	//This returns the mobile's armor class.
	public int getAC() {
		return currentAC;
	}

	//getBody method.
	//This method returns the body of the mobile.
	public Body getBody() {
		return body;
	}

	//getShortDescription method.
	//This method is used for displaying what the player sees on a look command.
	//It needs to be overriden in the PlayerCharacter class so that the race of the
	//player is also shown in parentheses beside their name.
	public String getShortDescription() {
		return this.getName();
	}

	//getCurrentHP method.
	//This method returns the current HP + bonus HP of the mobile.
	public int getCurrentHP() {
		return currentHP + bonusHP;
	}

	//getCurrentBaseHP method.
	//This method returns the current HP of the mobile.
	public int getCurrentBaseHP() {
		return currentHP;
	}
	
	//getMaxHP method.
	//This method gets the maximum HP + bonus HP of the mobile.
	public int getMaxHP() {
		return maxHP + bonusHP;
	}

	//getMaxBaseHP method.
	//This method returns the maximum HP of the mobile.
	public int getMaxBaseHP() {
		return maxHP;
	}

	//getCurrentHPString method.
	//This method returns the current HP of the mobile as a String.
	public String getCurrentHPString() {
		String res = "";
		int hp = (int)currentHP;
		res = res.valueOf(hp);
		return res;
	}

	//getMaxHPString method.
	//This method returns the maximum HP of the mobile as a String.
	public String getMaxHPString() {
		String res = "";
		int hp = (int)(maxHP + bonusHP);
		res = res.valueOf(hp);
		return res;
	}

	//getEquipment method.
	//This method returns an array of Items that comes from the equipment.
	public Vector getEquipment() {
		Body body = this.getBody();
		Vector partList = body.getBodyParts();
		Vector theEquipment = new Vector();

		for (int c = 0; c < partList.size(); c++) {
			try {
				BodyPart part = (BodyPart)partList.get(c);
				Item i = (Item)part.getItem();
				if (i != null) theEquipment.addElement(i);
			}
			catch(Exception e) {}
		}

		return theEquipment;

	}

	//getEntityType method.
	//This method returns the entity type of the mobile... Which is living.
	public int getEntityType() {
		return Affectable.LIVING;
	}

	//getLockTimeRemaining method.
	//Returns the time left on this mob's locked status.
	public int getLockTimeRemaining() {
		return lockTimeRemaining;
	}

	//setType method.
	//This method sets the status of the mobile on the MUD. It should always be mortal
	//unless a player of GOD status or higher has changed it. Only players can be
	//elevated to divine status. All other mobiles are always mortal.
	public void setType(int type) {
		if ((type != MORTAL) && (type != LESSER_GOD) && (type != GOD) && (type != FORGER))
			type = MORTAL;

		if (!isPlayer()) {
			type = MORTAL;
		}

		this.type = type;
	}

	//setGender method.
	//This method will set the gender of the mobile. It automatically checks for invalid
	//numbers if they should some how get passed to it.
	public void setGender(int gender) {
		if ((gender < MALE) || (gender > IT)) this.gender = IT;

		else this.gender = gender;
	}

	//setLocked method.
	//This method will set if the mob is locked or not.
	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	//########################
	//Combat methods.
	//########################

	//attack method.
	//This method is the main one used for attacks. It quite literally makes an attack roll
	//and returns a true or false depending on if it exceeded the target's AC or not. It
	//calculates a modifier of the mob's class BAB (if it has one), The Strength or Dexterity
	//modifier (depending on melee or ranged), and any other bonuses given to the mobile by
	//its changeAttackBonus method.
	//TODO:
	//The melee variable will eventually be changed to an enum from the ring.combat package... i.e.
	//Combat.MELEE or Combat.RANGED. this works for now though
	public boolean attack(Affectable target, boolean melee) {
		//start calculations by finding the mobile's base attack bonus.
		int attackModifier = (int)(getMobileClass().getBaseAttackBonus().getModifier() * level);

		//now add everything else
		if (melee) attackModifier += getModifier(STRENGTH);
		else attackModifier += getModifier(DEXTERITY);
		attackModifier += attackBonus;

		//now do the actual attack roll
		Random gen = new Random(System.nanoTime());
		int opponentAC = target.getAC();
		int baseRoll = gen.nextInt(20);

		//results:
		//always fail on a natural 1, always hit on a natural 20.
		if (baseRoll == 1) return false;
		if (baseRoll == 20) return true;

		//otherwise we compare.
		if ((baseRoll + opponentAC) >= opponentAC) return true;
		else return false;
	}
	
	//currentTarget method.
	//Returns this Mobile's current auto-attack target.
	public Affectable currentTarget() {
		return currentTarget;
	}
	
	//setCurrentTarget method.
	//Sets the mobile's current target to "t".
	public void setCurrentTarget(Affectable t) {
		currentTarget = t;
	}
	
	//calculateXPGiven method.
	//This method calculates an XP amount to give based on this Mobile's level, racial hit dice, and other
	//various things.
	public int calculateXP() {
		return 1;
	}
	
	//gainXP method.
	//This method increases the current XP of the mobile. If the XP is great enough for a level up, 
	//then call the level up methods.
	public void gainXP(int amount) {
		
	}
	
	//########################
	//Item methods.
	//########################

	//addItemToInventory method.
	//This method adds an item to a mobile's inventory. The boolean returned depends on if the
	//adding was successful or not. THIS MEANS THAT SOME OF THE "ERROR" CHECKING FOR ADDING TO
	//INVENTORY IS DONE HERE! HOWEVER, IT ASSUMES THAT THE OBJECT IS AN ITEM AND IS GETTABLE!
	public boolean addItemToInventory(Item item) {
		//check weight of thing vs. weight limit of getter.
		if (!(item.getWeight() <= weightLimit)) return false;

		//If yes, check room in inventory of getter, WITH THE NEW ITEM FACTORED IN.
		if (inventory == null) inventory = new Vector();
		if (!((inventory.size() + 1) <= MAX_ITEMS_IN_INV)) return false;

		//If both of those are yes, we can add it to the inventory.
		inventory.addElement(item);

		return true;
	}

	public boolean removeItemFromInventory(Item item) {
		//This method assumes the mobile has the item..............
		return inventory.remove(item);
		//return true;
	}

	//equip method.
	//THIS METHOD EQUIPS AN ITEM WITHOUT CHECKING FOR ANYTHING ELSE--I.E. IT JUST SETS THE ITEM OF
	//THE PART!!!!!!!! USE ONLY FROM THE EQUIP COMMAND METHOD!!!!
	public boolean equip(BodyPart part, Item item) {
		System.out.println("Entering equip method...");
		if (part.hasItem()) return false;

		else {
			System.out.println("They have an item...");
			part.addItem(item);
			item.setPartWornOn(part);
			System.out.println("We added it...");
			applyEffectsFromItem(item);
			System.out.println("And applied the effects...");
			return true;
		}

	}

	//dequip method.
	//THIS METHOD DEUQIPS AN ITEM WITHOUT CHECKING FOR ANYTHING ELSE--USE ONLY FROM THE REMOVE
	//COMMAND!!!!!!!!!
	public Item dequip(BodyPart part) {
		Item i = part.removeItem();

		//Now we need to unapply all effects........
		unapplyEffectsFromItem(i);

		//Return the item for some use later or something.
		return i;
	}

	//applyEffectsFromItem method.
	//This method applies all effects from an item to the mobile (AC, Stat boosts, etc).
	public void applyEffectsFromItem(Item item) {
		System.out.println("Checking AC...");
		//First: AC.
		if (item.isArmor()) {
			System.out.println("It's an armor... Applying AC bonus...");
			Armor a = (Armor) item;
			currentAC += a.getAC();
		}

		//Second: Loop through the SpellList and cast everything. EVERY ITEM HAS A SpellList!!
		//Many SpellLists just have no spells in them :)
		System.out.println("Applying the spells...");
		Effect itemEffects = item.getPassiveEffects();
		itemEffects.setTarget(this);
		super.addEffect(itemEffects);


		System.out.println("Spells applied...");

		//Third: Call the item's special code block in case it does something requiring
		//special programming.
		item.specialCode(this);
		System.out.println("Special code applied...");
	}

	//unapplyEffectsFromItem method.
	//This method unapplies all effects from an item to the mobile... It's the inverse of the
	//above.
	public void unapplyEffectsFromItem(Item item) {
		System.out.println("Whoa param: " + item);
		//First: AC.
		if (item.isArmor()) {
			System.out.println("YEP!");
			Armor a = (Armor)item;
			currentAC -= a.getAC();
		}

		//Second: Loop through the SpellList and decast everything.
		Effect effects = item.getPassiveEffects();
		effects.endEffect();

		//Third: ... There is no third since the special programming is one time. If necessary
		//Make the un-specialness code in your specialCode(...) method....
	}

	//########################
	//Other methods. (Misc crap)
	//########################

	//regenHP method.
	//This method regenerates HP every tick that the mobile is below max HP based on
	//a regeneration rate determined by race.
	//TODO: implement the actual regen rate. Currently it heals at 2 hp per sec.
	//TODO: investigate issues with bonus HP and regen.
	private void regenHP() {
		if (currentHP + 2 > maxHP) currentHP = maxHP; //make sure we don't go over.
		else currentHP += 2;
	}
	
	//regenMV method.
	//This method regenerates 5 MV every 3 ticks. It does this if the mobile is below max MV.
	private void regenMV() {
		if (currentMV + 5 > maxMV) currentMV = maxMV; //make sure we don't go over...
		else currentMV += 1;
	}
	
	//resetChecks method.
	//This method resets spot, listen, and search checks. Used for when a mobile enters a new room.
	public void resetChecks() {
		spotCheck = 0;
		listenCheck = 0;
		hiddenExitSearchCheck = 0;
	}

	//levelUp method.
	//This method levels the Mobile up!
	public void levelup() {
		int hitDie = mobClass.getHitDie();
		System.out.println("Got hit die...: " + hitDie);
		Random gen = new Random(System.nanoTime());
		System.out.println("Instantiated generator...");
		int newHP = gen.nextInt(hitDie) + 0;/*will eventually be con modifier!*/
		System.out.println("Updated HP...");
		currentHP += newHP;
		maxHP += newHP;
		level++;
		mobClass.setClassLevel(level);//There's no multiclassing in RingMUD.
		System.out.println("Updated level itself...");

		if (this.isPlayer()) {
			System.out.println("Sending to player...");
			PlayerCharacter pc = (PlayerCharacter)this;
			pc.sendData("[B][GREEN]You have leveled up to level " + level + "![R][WHITE]");
		}

		//Some for loops dealing with spells and skills or something later...
	}

	//increaseLockTime method.
	//Increases the current amount of time the mob is locked for.
	public void increaseLockTime(int time) {
		lockTimeRemaining += time;
		if (lockTimeRemaining > 0) this.setLocked(true);
	}

	public void decrementLockTime() {
		lockTimeRemaining--;
	}

	//setLockFinishedMessage method.
	//This method sets the message to display when the mobile is able to send commands again.
	public void setLockFinishedMessage(String msg) {
		lockFinishedMessage = msg;
	}

	//setLockMessage method.
	//This method sets the message to display if it is locked.
	public void setLockMessage(String msg) {
		lockMessage = msg;
	}

	//processTick method.
	//This method processes Effects for this mobile and deals with command locking.
	//PlayerCharacters and NPCs should override this method to deal things in their
	//own unique fashion. However, they MUST call super.processTick()
	//or there will be problems... big problems.
	public void processTick(TickerEvent e) {
		for (int c = 0;  c < super.effectsList.size(); c++) {
			Effect effect = (Effect)super.effectsList.get(c);
			if (effect.isDead()) { System.out.println("***Removing Effect: " + effect); super.effectsList.removeElement(effect); }
			else effect.decrementTimer();
		}
		
		//Deal with locking.
		if (this.isLocked) this.decrementLockTime();
		if (this.getLockTimeRemaining() <= 0) { 
			this.setLocked(false);
			lockMessage = "You are currently focused on an activity.";
			lockFinishedMessage = "You become aware of the world again.";
		}
		
		//Regenerate some movement and HP.
		if ((getCurrentMV() < getMaxMV()) && (e.getCurrentTick() % 3 == 0)) regenMV();
		if (getCurrentHP() < getMaxHP()) regenHP();

	}
	
	//move method.
	//This method handles the movement of this mobile from ZoneCoordinate to ZoneCoordinate (really room-to-room).
	//It attempts to cover all bases with regards to movement.  
	public final String move(ZoneCoordinate zc) {
            //Check to see if this mobile is fighting. That means they can't leave.
            if (this.isFighting) {
                sendData("[GREEN]You may not leave during combat![WHITE]");
                return null;
            }

            //set up the variables we need to use
            Room roomToMoveTo;

            //now we first check the current room's local exits
            roomToMoveTo = location.getRoom().getLocalExit(zc);

            //if the local exit is null, we shouldlook globally.
            if (roomToMoveTo == null) {
                roomToMoveTo = location.offset3D(zc).getRoom();
            }

            //now we can see if this room exists/is not an exit.
            //isNoExit is kind of a misnomer here. isNoExit means that this "room"
            //is really blocking off exiting the current room in this direction.
            if (roomToMoveTo == null || roomToMoveTo.isNoExit()) {
                    return null;
            }

            //Else the location must be valid. Move the mobile to the new Room.
            else {
                ZoneCoordinate locToMoveTo = location.offset3D(zc);
                System.out.println("CURRLOC: " + getLocation());
                Room currRoom = getLocation().getRoom();

                //Is the location hidden? If so, can the mobile actually locate it?
                if (roomToMoveTo.getSearchDC() > 0) {
                        if (this.hiddenExitSearchCheck < roomToMoveTo.getSearchDC()) return null;
                }

                //default arrive and leave text to broadcast to others			
                String leaveText = this.getName() + " [R][WHITE]leaves " + ZoneCoordinate.getDirectionString(zc) + ".";
                String arriveText = this.getName() + " [R][WHITE]arrives from " + ZoneCoordinate.getOppositeDirectionString(zc) + ".";

                //is our mobile hiding? if so, make some vague leave/arrive text.			
                if (this.hideCheck > 0) {
                        leaveText = "You catch someone (or something) leaving " + ZoneCoordinate.getDirectionString(zc) + " out of the corner of your eye.";
                        arriveText = "In the corner of your eye, you see someone (or something) arrive from " + ZoneCoordinate.getOppositeDirectionString(zc) + " and disappear.";
                }

                //change the character's location.
                World.roomArriveLeaveToLocation(this, leaveText, "[R][WHITE]You hear the shuffling of someone leaving.");
                this.resetChecks(); //reset the character's search, listen, and spot checks.
                currRoom.removeMobile(this);
                this.setLocation(location.offset3D(zc)); //make sure we're actually AT the new location...
                roomToMoveTo.addMobile(this);
                World.roomArriveLeaveToLocation(this, arriveText, "[R][WHITE]You hear the sounds of someone arriving.");

                //subtract the right amount of move points.
                this.changeCurrentMV(-1);

                //display the new information of the room
                if (this.isBlind) return "You stumble into a new area, but you cannot see anything!";
                return roomToMoveTo.getTitle() + "\n" +
                                roomToMoveTo.getDescription() + "\n" +
                                roomToMoveTo.getExitsString(hiddenExitSearchCheck) + "\n" +
                                roomToMoveTo.getMobileList(this, spotCheck) + roomToMoveTo.getEntityList();
            }
        }
}
