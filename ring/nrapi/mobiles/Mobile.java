package ring.nrapi.mobiles;

import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.commands.CommandHandler;
import ring.commands.CommandSender;
import ring.effects.Affectable;
import ring.effects.Effect;
import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.RingConstants;
import ring.nrapi.entities.Armor;
import ring.nrapi.entities.Item;
import ring.nrapi.mobiles.backbone.Equipment;
import ring.world.TickerEvent;
import ring.world.TickerListener;

/**
 * The main business object for Mobiles. Aggregates all the mobile data models and
 * provides business methods.
 * @author projectmoon
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"baseModel",
	"dynamicModel",
	"combatModel"
})
public class Mobile extends AbstractBusinessObject implements CommandSender, TickerListener {
	public static final long serialVersionUID = 1;

	//Model variables: store various aspects of this Mobile's information.
	private MobileBaseModel baseModel = new MobileBaseModel();
	private MobileDynamicModel dynamicModel = new MobileDynamicModel();
	private MobileCombatModel combatModel = new MobileCombatModel();

	//If the mob is locked: if so, they cannot take actions.
	private boolean isLocked;
	
	//CommandHandler. This CommandHandler is protected so it can drop down into
	//the PlayerCharacter class.
	protected transient CommandHandler handler = new CommandHandler(this);

	//The time left before this mob can act again.
	protected int lockTimeRemaining;
	
	//default messages to display while the mob is locked, and when it is finished.
	protected String lockMessage = "You are currently focused on an activity."; 
	protected String lockFinishedMessage = "You become aware of the world again."; 
	
	public Mobile() {
		baseModel = new MobileBaseModel();
		dynamicModel = new MobileDynamicModel();
		combatModel = new MobileCombatModel();
	}

	// Creates a mobile from the given models.
	public Mobile(MobileBaseModel base, MobileDynamicModel dynamics, MobileCombatModel combat) {
		baseModel = base;
		dynamicModel = dynamics;
		combatModel = combat;
	}
	
	@Override
	/**
	 * Sets parent relationships for items in inventory and equipment.
	 */
	public void createChildRelationships() {
		for (Item i : getDynamicModel().getInventory()) {
			i.setParent(this);
		}
		
		for (Item i : getDynamicModel().getEquipment()) {
			i.setParent(this);
		}
	}
	
	@XmlElement
	public MobileBaseModel getBaseModel() {
		return baseModel;
	}
	
	public void setBaseModel(MobileBaseModel model) {
		baseModel = model;
	}
	
	@XmlElement
	public MobileDynamicModel getDynamicModel() {
		return dynamicModel;
	}
	
	public void setDynamicModel(MobileDynamicModel model) {
		dynamicModel = model;
	}
	
	@XmlElement
	public MobileCombatModel getCombatModel() {
		return combatModel;
	}
	
	public void setCombatModel(MobileCombatModel model) {
		combatModel = model;
	}
	
	/**
	 * Gets the modifier for a given score based on the standard
	 * formula: (score - 10) / 2, rounded down.
	 * @param score
	 * @return the modifier
	 */
	public int getModifier(int score) {
		return (score - 10) / 2;
	}

	// Meant to be overridden.
	public void sendData(String text) {

	}

	/**
	 * Checks to see if this Mobile is a player.
	 * @return true or false
	 */
	@XmlTransient
	public boolean isPlayer() {
		//TODO implement isPlayer
		//return this instanceof PlayerCharacter;
		return false;
	}

	/**
	 * Checks to see if this Mobile is an NPC.
	 * @return true or false
	 */
	@XmlTransient
	public boolean isNPC() {
		//TODO implement isNPC
		//return this instanceof NPC;
		return false;
	}
	
	@XmlTransient
	public boolean isLocked() {
		return isLocked;
	}
 
	// getLockTimeRemaining method.
	// Returns the time left on this mob's locked status.
	@XmlTransient
	public int getLockTimeRemaining() {
		return lockTimeRemaining;
	}

	// setLocked method.
	// This method will set if the mob is locked or not.
	public void setLocked(boolean locked) {
		isLocked = locked;
	}
	
	public boolean attack(Affectable target, boolean melee) {
		//start calculations by finding the mobile's base attack bonus.
		int attackModifier = (int) (getBaseModel().getMobileClass().getBaseAttackBonus()
				.getModifier() * getBaseModel().getLevel());

		//handle ranged vs melee modifiers
		if (melee) {
			attackModifier += getModifier(getBaseModel().getStrength());
		}
		else {
			attackModifier += getModifier(getBaseModel().getDexterity());
		}
		
		//add any other generic bonuses
		attackModifier += getCombatModel().getAttackBonus();

		//now do the actual attack roll
		Random gen = new Random(System.nanoTime());
		int opponentAC = target.getAC();
		int baseRoll = gen.nextInt(20);

		//results:
		// always fail on a natural 1, always hit on a natural 20.
		if (baseRoll == 1)
			return false;
		if (baseRoll == 20)
			return true;

		//otherwise we compare.
		if ((baseRoll + opponentAC) >= opponentAC)
			return true;
		else
			return false;
	}

	/**
	 * This method gives this Mobile XP. If the XP is enough for a
	 * levelup, a levelup is automatically performed.
	 * @param amount
	 */
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
		return getDynamicModel().getInventory().addItem(item);
	}

	/**
	 * Convenience method that removes an item from a Mobile's inventory.
	 * 
	 * @param item
	 * @return true or false if the removal was successful or not.
	 */
	public boolean removeItemFromInventory(Item item) {
		return getDynamicModel().getInventory().removeItem(item);
	}

	/**
	 * Convenience method that equips an item on a body part for the mobile.
	 * 
	 * @param part
	 * @param item
	 * @return true if equipping was successful, false otherwise.
	 */
	public boolean equip(BodyPart part, Item item) {
		Equipment equipment = getDynamicModel().getEquipment();
		if (!equipment.hasItem(part)) {
			equipment.putItem(part, item);
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
		Item i = getDynamicModel().getEquipment().removeItem(part);

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
			getCombatModel().changeCurrentAC(a.getAC());
		}

		//TODO implement apply effects from item.
		//System.out.println("Applying the spells...");
		//Effect itemEffects = item.getPassiveEffects();
		//itemEffects.setTarget(this);
		//super.addEffect(itemEffects);
	}

	// unapplyEffectsFromItem method.
	// This method unapplies all effects from an item to the mobile... It's the
	// inverse of the
	// above.
	public void unapplyEffectsFromItem(Item item) {
		// First: AC.
		if (item.isArmor()) {
			Armor a = (Armor) item;
			getCombatModel().changeCurrentAC(-1 * a.getAC());
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
		MobileCombatModel model = getCombatModel();
		if (model.getCurrentHP() + 2 > model.getMaxHP())
			model.setCurrentHP(model.getMaxHP()); // make sure we don't go over.
		else
			model.changeCurrentHP(2);
	}

	// regenMV method.
	// This method regenerates 5 MV every 3 ticks. It does this if the mobile is
	// below max MV.
	private void regenMV() {
		MobileDynamicModel model = getDynamicModel();
		if (model.getCurrentMV() + 1 > model.getMaxMV())
			model.setCurrentMV(model.getMaxMV());
		else
			model.changeCurrentMV(1);
	}

	// levelUp method.
	// This method levels the Mobile up!
	public void levelup() {
		//TODO implement levelup
		/*
		int hitDie = mobClass.getHitDie();
		System.out.println("Got hit die...: " + hitDie);
		Random gen = new Random(System.nanoTime());
		System.out.println("Instantiated generator...");
		int newHP = gen.nextInt(hitDie) + 0;
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
		*/
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

	/**
	 * This is the main processing method for Mobiles, both NPC and PC. Any class
	 * that extends Mobile should call this method in order to get basic command
	 * locking and stat regen facilities.
	 */
	public void processTick(TickerEvent e) {
		//super.removeDeadEffects();		
		
		// Deal with locking.
		if (this.isLocked)
			this.decrementLockTime();
		if (this.getLockTimeRemaining() <= 0) {
			this.setLocked(false);
			lockMessage = "You are currently focused on an activity.";
			lockFinishedMessage = "You become aware of the world again.";
		}

		// Regenerate some movement and HP.
		if ((getDynamicModel().getCurrentMV() < getDynamicModel().getMaxMV()) && (e.getCurrentTick() % 3 == 0))
			regenMV();
		
		if (getCombatModel().getCurrentHP() < getCombatModel().getMaxHP())
			regenHP();

	}

	public boolean canMove() {
		if (getBaseModel().isFighting()) {
			sendData("[GREEN]You may not leave during combat![WHITE]");
			return false;
		}
		else if (getDynamicModel().getCurrentMV() - 1 <= 0) {
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
		/*
		TODO implement movement for mobiles
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
		*/
		return false;
	}
	
	public static void main(String[] args) {
		Mobile m = new Mobile();
		m.setID("an id");
		m.equip(Body.FACE, new Item());
		m.getDynamicModel().getInventory().addItem(new Item());
		m.setReferential(true);
		String xml = m.toXML();
		System.out.println(xml);
	}
}
