package ring.mobiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.commands.CommandHandler;
import ring.commands.CommandSender;
import ring.effects.Affectable;
import ring.effects.Effect;
import ring.events.listeners.MobileListener;
import ring.items.Armor;
import ring.items.Item;
import ring.magic.SpellCaster;
import ring.mobiles.backbone.Equipment;
import ring.mobiles.npc.NPC;
import ring.movement.Movable;
import ring.movement.Room;
import ring.persistence.RingConstants;
import ring.players.PlayerCharacter;
import ring.world.TickerEvent;
import ring.world.TickerListener;
import ring.world.WorldObject;
import ring.world.WorldObjectMetadata;

/**
 * The main business object for Mobiles. Aggregates all the mobile data models and
 * provides business methods.
 * @author projectmoon
 *
 */

@XmlSeeAlso({ NPC.class, PlayerCharacter.class })
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"baseModel",
	"dynamicModel",
	"combatModel"
})
public abstract class Mobile extends WorldObject implements CommandSender, TickerListener, Movable, SpellCaster {
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
	
	//event listeners
	private List<MobileListener> listeners = new ArrayList<MobileListener>();
	
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
	
	public void addMobileListener(MobileListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeMobileListener(MobileListener listener) {
		return listeners.remove(listener);
	}
	
	public List<MobileListener> getMobileListeners() {
		return listeners;
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
		if (item instanceof Armor) {
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
		if (item instanceof Armor) {
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
		throw new UnsupportedOperationException("Leveling up not yet implemented");
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
		//TODO pending effects system changes
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
			//("[GREEN]You may not leave during combat![WHITE]");
			return false;
		}
		else if (getDynamicModel().getCurrentMV() - 1 <= 0) {
			//("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return true;
		}
		else {
			return true;
		}
	}

	@Override
	@XmlTransient
	public Room getLocation() {
		return getDynamicModel().getCurrLocation();
	}

	@Override
	public void setLocation(Room loc) {
		getDynamicModel().setCurrLocation(loc);
	}

	@Override
	public void doCommand(String cmd) {

	}

	@Override
	public WorldObjectMetadata getMetadata() {
		WorldObjectMetadata metadata = new WorldObjectMetadata();
		metadata.setName(getBaseModel().getName());
		return metadata;
	}
	
	/**
	 * Aggregates information from the base model to return a short
	 * description of this Mobile, used in at-a-glance descriptions,
	 * such as when a player looks into a room.
	 * @return A short description.
	 */
	@XmlTransient
	public String getShortDescription() {
		String res = getBaseModel().getName();
		String lastName = getBaseModel().getLastName();
		String title = getBaseModel().getTitle();
		String raceName = getBaseModel().getRace().getName();
		
		//Append these if they exist
		if (lastName != null && lastName.length() > 0) res += " " + lastName;
		if (title != null && title.length() > 0) res += " " + title;
		
		//Finallly add race and class name.
		res += " (" + raceName + ")";
		//TODO add class name.
		
		return res; 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((baseModel == null) ? 0 : baseModel.hashCode());
		result = prime * result
				+ ((combatModel == null) ? 0 : combatModel.hashCode());
		result = prime * result
				+ ((dynamicModel == null) ? 0 : dynamicModel.hashCode());
		result = prime * result + (isLocked ? 1231 : 1237);
		result = prime * result
				+ ((listeners == null) ? 0 : listeners.hashCode());
		result = prime
				* result
				+ ((lockFinishedMessage == null) ? 0 : lockFinishedMessage
						.hashCode());
		result = prime * result
				+ ((lockMessage == null) ? 0 : lockMessage.hashCode());
		result = prime * result + lockTimeRemaining;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mobile other = (Mobile) obj;
		if (baseModel == null) {
			if (other.baseModel != null)
				return false;
		} else if (!baseModel.equals(other.baseModel))
			return false;
		if (combatModel == null) {
			if (other.combatModel != null)
				return false;
		} else if (!combatModel.equals(other.combatModel))
			return false;
		if (dynamicModel == null) {
			if (other.dynamicModel != null)
				return false;
		} else if (!dynamicModel.equals(other.dynamicModel))
			return false;
		if (isLocked != other.isLocked)
			return false;
		if (listeners == null) {
			if (other.listeners != null)
				return false;
		} else if (!listeners.equals(other.listeners))
			return false;
		if (lockFinishedMessage == null) {
			if (other.lockFinishedMessage != null)
				return false;
		} else if (!lockFinishedMessage.equals(other.lockFinishedMessage))
			return false;
		if (lockMessage == null) {
			if (other.lockMessage != null)
				return false;
		} else if (!lockMessage.equals(other.lockMessage))
			return false;
		if (lockTimeRemaining != other.lockTimeRemaining)
			return false;
		return true;
	}
}
