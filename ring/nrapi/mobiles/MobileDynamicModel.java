package ring.nrapi.mobiles;

import ring.movement.Location;
import ring.nrapi.mobiles.backbone.Bank;
import ring.nrapi.mobiles.backbone.Equipment;
import ring.nrapi.mobiles.backbone.Inventory;

/**
 * This class represents data of a mobile that changes rather frequently,
 * but is categorized as combat data. Things such as money amount, inventory,
 * weight carried, etc go here.
 * @author projectmoon
 */
public class MobileDynamicModel {
	//The current location of the mobile.
	private Location currLocation;
	
	//Money on the mobile, and money in its bank account.
	private Bank money;
	private Bank bankAccount;
	
	//Inventory and equipment.
	private Inventory inventory;
	private Equipment equipment;

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
	
	public void setCurrLocation(Location currLocation) {
		this.currLocation = currLocation;
	}
	public Location getCurrLocation() {
		return currLocation;
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
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	public Equipment getEquipment() {
		return equipment;
	}
	public void setWeightCarried(double weightCarried) {
		this.weightCarried = weightCarried;
	}
	public double getWeightCarried() {
		return weightCarried;
	} 
}
