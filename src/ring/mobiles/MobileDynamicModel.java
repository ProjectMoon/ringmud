package ring.nrapi.mobiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.movement.Location;
import ring.nrapi.mobiles.backbone.Bank;
import ring.nrapi.mobiles.backbone.Equipment;
import ring.nrapi.mobiles.backbone.Inventory;
import ring.persistence.RingConstants;

/**
 * This class represents data of a mobile that changes rather frequently,
 * but is categorized as combat data. Things such as money amount, inventory,
 * weight carried, etc go here.
 * @author projectmoon
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"money", "bankAccount", "inventory", "equipment", "weightCarried", "currentMV",
	"maxMV", "movementMultiplier", "speed"
})
public class MobileDynamicModel {
	//The current location of the mobile.
	private Location currLocation;
	
	//Money on the mobile, and money in its bank account.
	private Bank money = new Bank();
	private Bank bankAccount = new Bank();
	
	//Inventory and equipment.
	private Inventory inventory = new Inventory();
	private Equipment equipment = new Equipment();

	private double weightCarried;
	
	//Skill check variables:
	//These variables somewhat violate the extensible design philosophy of the
	//MUD, but as far as I can see,
	//they are currently necessary. These are for things that need to be
	//hardcoded into the game. Namely
	//search, hide, move silently, spot, and listen checks.
	private int hideCheck;
	private int moveSilentlyCheck;
	private int spotCheck;
	private int listenCheck;
	//current search check for viewing hidden exits. reset every room change.	
	private int hiddenExitSearchCheck = 0;
	
	// resetChecks method.
	// This method resets spot, listen, and search checks. Used for when a
	// mobile enters a new room.
	public void resetChecks() {
		spotCheck = 0;
		listenCheck = 0;
		hiddenExitSearchCheck = 0;
	}
	
	public void setCurrLocation(Location currLocation) {
		this.currLocation = currLocation;
	}
	
	@XmlTransient
	public Location getCurrLocation() {
		return currLocation;
	}
	
	public void setMoney(Bank money) {
		this.money = money;
	}
	
	@XmlElement
	public Bank getMoney() {
		return money;
	}
	
	public void setBankAccount(Bank bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	@XmlElement
	public Bank getBankAccount() {
		return bankAccount;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	@XmlElement
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	
	@XmlElement
	public Equipment getEquipment() {
		return equipment;
	}
	
	public void setWeightCarried(double weightCarried) {
		this.weightCarried = weightCarried;
	}
	
	@XmlElement	
	public double getWeightCarried() {
		return weightCarried;
	} 
	
	//Movement-related variables.
	//The current movement points (MV) of the mobile.
	private int currentMV;
	@XmlElement public int getCurrentMV() { return currentMV; }
	public void setCurrentMV(int mv) { currentMV = mv; }
	public void changeCurrentMV(int amount) { currentMV += amount; }
	
	//The maximum movement points (MV) of the mobile.
	//This number is calculated as follows:
	//BASE_MAXMV + (10 * con mod). BASE_MAXMV is currently 100.
	private int maxMV;
	@XmlElement public int getMaxMV() { return maxMV; }
	public void setMaxMV(int mv) { 
		maxMV = mv;
		if (maxMV < currentMV) {
			currentMV = maxMV;
		}
	}

	//subtract an extra amount of MV per room move equal to this * default move
	//amount.
	private int movementMultiplier;
	@XmlElement public int getMovementMultiplier() { return movementMultiplier; }
	public void setMovementMultiplier(int mov) { movementMultiplier = mov; }
	public void increaseMovementMultiplier(int mov) { movementMultiplier += mov; }
	public void resetMovementMultiplier() {	movementMultiplier = 0; }

	//The speed of the mobile in feet.
	private int speed;
	@XmlElement public int getSpeed() { return speed; }
	public void setSpeed(int speed) { this.speed = speed; }
	public void changeSpeed(int change) { speed += change; }	
}
