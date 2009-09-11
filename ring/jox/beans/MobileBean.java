package ring.jox.beans;

import java.io.Serializable;
import java.util.ArrayList;

import ring.mobiles.Alignment;
import ring.mobiles.backbone.Bank;
import ring.mobiles.backbone.Inventory;

/**
 * A bean representing a given Mobile (NPC or player). This is a stripped down
 * version of the Mobile class, which is still rather large.
 * 
 * @author projectmoon
 * 
 */
public class MobileBean implements Serializable {
	public static final long serialVersionUID = 1;
	
	private String name;
	private String longDescription;
	private String lastName;
	private String title;

	private int level;
	private Bank money;
	private Bank bankAccount;

	private int maxHP;
	private int currentHP;
	private int naturalArmorBonus;
	private int deflectionArmorBonus;
	private int otherArmorBonus;
	private int attackBonus;
	private int maxMV;
	private int currentMV;
	private int speed;

	private int strength;
	private int dexterity;
	private int constitution;
	private int intelligence;
	private int wisdom;
	private int charisma;

	private int gender;
	private RaceBean race;
	private BodyBean body;
	private Alignment alignment;

	private Inventory inventory;
	private ArrayList<RaceBean> raceAggressiveList;
	private ArrayList<Alignment> alignmentAggressiveList;

	private boolean isAggressive;
	private boolean isDead;
	private boolean isGod;
	private boolean isDeaf;
	private boolean isBlind;
	private boolean isSilent;
	private boolean isFlying;
	private boolean isFloating;
	private boolean isMounted;
	private boolean isFighting;
	private boolean isLeftHanded;
	private boolean isProne;
	private boolean isLyingDown;
	private boolean isSitting;
	private boolean moves;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setMoney(Bank money) {
		this.money = money;
	}

	public Bank getMoney() {
		return money;
	}

	public void setBankAccount(Bank bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Bank getBankAccount() {
		return bankAccount;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setNaturalArmorBonus(int naturalArmorBonus) {
		this.naturalArmorBonus = naturalArmorBonus;
	}

	public int getNaturalArmorBonus() {
		return naturalArmorBonus;
	}

	public void setDeflectionArmorBonus(int deflectionArmorBonus) {
		this.deflectionArmorBonus = deflectionArmorBonus;
	}

	public int getDeflectionArmorBonus() {
		return deflectionArmorBonus;
	}

	public void setOtherArmorBonus(int otherArmorBonus) {
		this.otherArmorBonus = otherArmorBonus;
	}

	public int getOtherArmorBonus() {
		return otherArmorBonus;
	}

	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public void setMaxMV(int maxMV) {
		this.maxMV = maxMV;
	}

	public int getMaxMV() {
		return maxMV;
	}

	public void setCurrentMV(int currentMV) {
		this.currentMV = currentMV;
	}

	public int getCurrentMV() {
		return currentMV;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getStrength() {
		return strength;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	public int getConstitution() {
		return constitution;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setWisdom(int wisdom) {
		this.wisdom = wisdom;
	}

	public int getWisdom() {
		return wisdom;
	}

	public void setCharisma(int charisma) {
		this.charisma = charisma;
	}

	public int getCharisma() {
		return charisma;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

	public void setRace(RaceBean race) {
		this.race = race;
	}

	public RaceBean getRace() {
		return race;
	}

	public void setBody(BodyBean body) {
		this.body = body;
	}

	public BodyBean getBody() {
		return body;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setRaceAggressiveList(ArrayList<RaceBean> raceAggressiveList) {
		this.raceAggressiveList = raceAggressiveList;
	}

	public ArrayList<RaceBean> getRaceAggressiveList() {
		return raceAggressiveList;
	}

	public void setAlignmentAggressiveList(
			ArrayList<Alignment> alignmentAggressiveList) {
		this.alignmentAggressiveList = alignmentAggressiveList;
	}

	public ArrayList<Alignment> getAlignmentAggressiveList() {
		return alignmentAggressiveList;
	}

	public void setAggressive(boolean isAggressive) {
		this.isAggressive = isAggressive;
	}

	public boolean isAggressive() {
		return isAggressive;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setGod(boolean isGod) {
		this.isGod = isGod;
	}

	public boolean isGod() {
		return isGod;
	}

	public void setDeaf(boolean isDeaf) {
		this.isDeaf = isDeaf;
	}

	public boolean isDeaf() {
		return isDeaf;
	}

	public void setBlind(boolean isBlind) {
		this.isBlind = isBlind;
	}

	public boolean isBlind() {
		return isBlind;
	}

	public void setSilent(boolean isSilent) {
		this.isSilent = isSilent;
	}

	public boolean isSilent() {
		return isSilent;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFloating(boolean isFloating) {
		this.isFloating = isFloating;
	}

	public boolean isFloating() {
		return isFloating;
	}

	public void setMounted(boolean isMounted) {
		this.isMounted = isMounted;
	}

	public boolean isMounted() {
		return isMounted;
	}

	public void setFighting(boolean isFighting) {
		this.isFighting = isFighting;
	}

	public boolean isFighting() {
		return isFighting;
	}

	public void setLeftHanded(boolean isLeftHanded) {
		this.isLeftHanded = isLeftHanded;
	}

	public boolean isLeftHanded() {
		return isLeftHanded;
	}

	public void setProne(boolean isProne) {
		this.isProne = isProne;
	}

	public boolean isProne() {
		return isProne;
	}

	public void setLyingDown(boolean isLyingDown) {
		this.isLyingDown = isLyingDown;
	}

	public boolean isLyingDown() {
		return isLyingDown;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
	}

	public boolean isSitting() {
		return isSitting;
	}

	public void setMoves(boolean moves) {
		this.moves = moves;
	}

	public boolean isMoves() {
		return moves;
	}
}
