package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.util.*;

import ring.nrapi.entities.*;
import ring.commands.CommandHandler;
import ring.commands.CommandSender;

//This is the class that all mobiles (NPC and PC) extend from. This class will hold basic
//information for a mobile such as body shape, HP, race, etc.
public class Mobile extends AbstractBusinessObject implements CommandSender, TickerListener, Movable {
	public static final long serialVersionUID = 1;

	//Model variables: store various aspects of this Mobile's information.
	private MobileBaseModel baseModel = new MobileBaseModel();
	private MobileDynamicModel dynamicModel = new MobileDynamicModel();
	private MobileCombatModel combatModel = new MobileCombatModel();

	//If the mob is "locked."
	private boolean isLocked;
	
	//CommandHandler. This CommandHandler is protected so it can drop down into
	//the PlayerCharacter class.
	protected transient CommandHandler handler = new CommandHandler(this);

	// lockTimeRemaining: The time left before this mob can act again.
	protected int lockTimeRemaining;
	
	// defaultmessages to display while the mob is locked, and when it is finished.
	protected String lockMessage = "You are currently focused on an activity."; 
	protected String lockFinishedMessage = "You become aware of the world again."; 
	
	public Mobile() {

	}

	// Creates a mobile from the given models.
	public Mobile(MobileBaseModel base, MobileDynamicModel dynamics, MobileCombatModel combat) {
		baseModel = base;
		dynamicModel = dynamics;
		combatModel = combat;
	}

	// Meant to be overridden.
	public void sendData(String text) {

	}

	// isPlayer method.
	// This checks to see if the mobile is a player. This is used in numerous
	// method that only
	// or only need to affect the players.
	@Override
	public boolean isPlayer() {
		return this instanceof PlayerCharacter;
	}

	// isNPC method.
	@Override
	public boolean isNPC() {
		return this instanceof NPC;
	}
	
	public void isLocked() {
		return isLocked;
	}

	// getEntityType method.
	// This method returns the entity type of the mobile... Which is living.
	public int getEntityType() {
		return Affectable.LIVING;
	}

	// getLockTimeRemaining method.
	// Returns the time left on this mob's locked status.
	public int getLockTimeRemaining() {
		return lockTimeRemaining;
	}

	// setLocked method.
	// This method will set if the mob is locked or not.
	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	// ########################
	// Combat methods.
	// ########################

	// attack method.
	// This method is the main one used for attacks. It quite literally makes an
	// attack roll
	// and returns a true or false depending on if it exceeded the target's AC
	// or not. It
	// calculates a modifier of the mob's class BAB (if it has one), The
	// Strength or Dexterity
	// modifier (depending on melee or ranged), and any other bonuses given to
	// the mobile by
	// its changeAttackBonus method.
	// TODO:
	// The melee variable will eventually be changed to an enum from the
	// ring.combat package... i.e.
	// Combat.MELEE or Combat.RANGED. this works for now though
	public boolean attack(Affectable target, boolean melee) {
		// start calculations by finding the mobile's base attack bonus.
		int attackModifier = (int) (getMobileClass().getBaseAttackBonus()
				.getModifier() * level);

		// now add everything else
		if (melee)
			attackModifier += getModifier(STRENGTH);
		else
			attackModifier += getModifier(DEXTERITY);
		attackModifier += attackBonus;

		// now do the actual attack roll
		Random gen = new Random(System.nanoTime());
		int opponentAC = target.getAC();
		int baseRoll = gen.nextInt(20);

		// results:
		// always fail on a natural 1, always hit on a natural 20.
		if (baseRoll == 1)
			return false;
		if (baseRoll == 20)
			return true;

		// otherwise we compare.
		if ((baseRoll + opponentAC) >= opponentAC)
			return true;
		else
			return false;
	}

	// calculateXPGiven method.
	// This method calculates an XP amount to give based on this Mobile's level,
	// racial hit dice, and other
	// various things.
	public int calculateXP() {
		return 1;
	}

	// gainXP method.
	// This method increases the current XP of the mobile. If the XP is great
	// enough for a level up,
	// then call the level up methods.
	public void gainXP(int amount) {

	}

	/**
	 * Convenience method for adding an item to a Mobile's inventory.
	 * 
	 * @param item
	 *            the item to add
	 * @return true or false based on whether item adding was successful.
	 */
	public boolean addItemToInventory(Item item) {
		return inventory.addItem(item);
	}

	/**
	 * Convenience method that removes an item from a Mobile's inventory.
	 * 
	 * @param item
	 * @return true or false if the removal was successful or not.
	 */
	public boolean removeItemFromInventory(Item item) {
		return inventory.removeItem(item);
	}

	/**
	 * Convenience method that equips an item on a body part for the mobile.
	 * 
	 * @param part
	 * @param item
	 * @return true if equipping was successful, false otherwise.
	 */
	public boolean equip(BodyPart part, Item item) {
		if (!equipment.hasItem(part)) {
			equipment.setItem(part, item);
			applyEffectsFromItem(item);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Convenience method that dequips an item.
	 * 
	 * @param part
	 * @return the Item removed, if there was one. null otherwise.
	 */
	public Item dequip(BodyPart part) {
		Item i = equipment.removeItem(part);

		// Now we need to unapply all effects........
		unapplyEffectsFromItem(i);

		// Return the item for some use later or something.
		return i;
	}

	// applyEffectsFromItem method.
	// This method applies all effects from an item to the mobile (AC, Stat
	// boosts, etc).
	public void applyEffectsFromItem(Item item) {
		System.out.println("Checking AC...");
		// First: AC.
		if (item.isArmor()) {
			System.out.println("It's an armor... Applying AC bonus...");
			Armor a = (Armor) item;
			currentAC += a.getAC();
		}

		// Second: Loop through the SpellList and cast everything. EVERY ITEM
		// HAS A SpellList!!
		// Many SpellLists just have no spells in them :)
		System.out.println("Applying the spells...");
		Effect itemEffects = item.getPassiveEffects();
		itemEffects.setTarget(this);
		super.addEffect(itemEffects);

		System.out.println("Spells applied...");

		// Third: Call the item's special code block in case it does something
		// requiring
		// special programming.
		item.specialCode(this);
		System.out.println("Special code applied...");
	}

	// unapplyEffectsFromItem method.
	// This method unapplies all effects from an item to the mobile... It's the
	// inverse of the
	// above.
	public void unapplyEffectsFromItem(Item item) {
		System.out.println("Whoa param: " + item);
		// First: AC.
		if (item.isArmor()) {
			System.out.println("YEP!");
			Armor a = (Armor) item;
			currentAC -= a.getAC();
		}

		// Second: Loop through the SpellList and decast everything.
		Effect effects = item.getPassiveEffects();
		effects.endEffect();

		// Third: ... There is no third since the special programming is one
		// time. If necessary
		// Make the un-specialness code in your specialCode(...) method....
	}

	// regenHP method.
	// This method regenerates HP every tick that the mobile is below max HP
	// based on
	// a regeneration rate determined by race.
	// TODO: implement the actual regen rate. Currently it heals at 2 hp per
	// sec.
	// TODO: investigate issues with bonus HP and regen.
	private void regenHP() {
		if (currentHP + 2 > maxHP)
			currentHP = maxHP; // make sure we don't go over.
		else
			currentHP += 2;
	}

	// regenMV method.
	// This method regenerates 5 MV every 3 ticks. It does this if the mobile is
	// below max MV.
	private void regenMV() {
		if (currentMV + 5 > maxMV)
			currentMV = maxMV; // make sure we don't go over...
		else
			currentMV += 1;
	}

	// resetChecks method.
	// This method resets spot, listen, and search checks. Used for when a
	// mobile enters a new room.
	public void resetChecks() {
		spotCheck = 0;
		listenCheck = 0;
		hiddenExitSearchCheck = 0;
	}

	// levelUp method.
	// This method levels the Mobile up!
	public void levelup() {
		int hitDie = mobClass.getHitDie();
		System.out.println("Got hit die...: " + hitDie);
		Random gen = new Random(System.nanoTime());
		System.out.println("Instantiated generator...");
		int newHP = gen.nextInt(hitDie) + 0;/* will eventually be con modifier! */
		System.out.println("Updated HP...");
		currentHP += newHP;
		maxHP += newHP;
		level++;
		mobClass.setClassLevel(level);// There's no multiclassing in RingMUD.
		System.out.println("Updated level itself...");

		if (this.isPlayer()) {
			System.out.println("Sending to player...");
			PlayerCharacter pc = (PlayerCharacter) this;
			pc.sendData("[B][GREEN]You have leveled up to level " + level
					+ "![R][WHITE]");
		}

		// Some for loops dealing with spells and skills or something later...
	}

	// increaseLockTime method.
	// Increases the current amount of time the mob is locked for.
	public void increaseLockTime(int time) {
		lockTimeRemaining += time;
		if (lockTimeRemaining > 0)
			this.setLocked(true);
	}

	public void decrementLockTime() {
		lockTimeRemaining--;
	}

	// setLockFinishedMessage method.
	// This method sets the message to display when the mobile is able to send
	// commands again.
	public void setLockFinishedMessage(String msg) {
		lockFinishedMessage = msg;
	}

	// setLockMessage method.
	// This method sets the message to display if it is locked.
	public void setLockMessage(String msg) {
		lockMessage = msg;
	}

	// processTick method.
	// This method processes Effects for this mobile and deals with command
	// locking.
	// PlayerCharacters and NPCs should override this method to deal things in
	// their
	// own unique fashion. However, they MUST call super.processTick()
	// or there will be problems... big problems.
	public void processTick(TickerEvent e) {
		super.removeDeadEffects();		
		
		// Deal with locking.
		if (this.isLocked)
			this.decrementLockTime();
		if (this.getLockTimeRemaining() <= 0) {
			this.setLocked(false);
			lockMessage = "You are currently focused on an activity.";
			lockFinishedMessage = "You become aware of the world again.";
		}

		// Regenerate some movement and HP.
		if ((getCurrentMV() < getMaxMV()) && (e.getCurrentTick() % 3 == 0))
			regenMV();
		if (getCurrentHP() < getMaxHP())
			regenHP();

	}

	public boolean canMove() {
		if (this.isFighting) {
			sendData("[GREEN]You may not leave during combat![WHITE]");
			return false;
		}
		else if (getCurrentMV() - 1 <= 0) {
			sendData("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return true;
		}
		else {
			return true;
		}
	}

	/**
	 * Convenience method that wraps around the location manager's move method.
	 * @param direction
	 * @return true if the Mobile was able to move in the specified direction, false otherwise.
	 */
	public final boolean move(String direction) {
		try {
			return LocationManager.move(this, LocationManager.getPortal(
					currLocation, direction));
		} catch (MovementAssertionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (PortalNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public boolean isEntity() {
		return false;
	}

	@Override
	public boolean isItem() {
		return false;
	}
}
