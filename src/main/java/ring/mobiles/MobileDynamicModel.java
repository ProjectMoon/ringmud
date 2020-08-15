package ring.mobiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.mobiles.backbone.Bank;
import ring.mobiles.backbone.Equipment;
import ring.mobiles.backbone.Inventory;
import ring.mobiles.senses.SensesGroup;
import ring.movement.Room;
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
	//Where the mobile is.
	private Room currLocation;
	
	//Money on the mobile, and money in its bank account.
	private Bank money = new Bank();
	private Bank bankAccount = new Bank();
	
	//Inventory and equipment.
	private Inventory inventory = new Inventory();
	private Equipment equipment = new Equipment();
	private double weightCarried;
	
	//Senses for this mobile: How the mobile interacts with and perceives the world
	private SensesGroup sensesGroup;
	public void setSensesGroup(SensesGroup group) { sensesGroup = group; }
	@XmlTransient public SensesGroup getSensesGroup() { return sensesGroup; }
	
	public void setCurrLocation(Room room) {
		currLocation = room;
	}
	
	@XmlTransient
	public Room getCurrLocation() {
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bankAccount == null) ? 0 : bankAccount.hashCode());
		result = prime * result + currentMV;
		result = prime * result
				+ ((equipment == null) ? 0 : equipment.hashCode());
		result = prime * result
				+ ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + maxMV;
		result = prime * result + ((money == null) ? 0 : money.hashCode());
		result = prime * result + movementMultiplier;
		result = prime * result
				+ ((sensesGroup == null) ? 0 : sensesGroup.hashCode());
		result = prime * result + speed;
		long temp;
		temp = Double.doubleToLongBits(weightCarried);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		MobileDynamicModel other = (MobileDynamicModel) obj;
		if (bankAccount == null) {
			if (other.bankAccount != null)
				return false;
		} else if (!bankAccount.equals(other.bankAccount))
			return false;
		if (currentMV != other.currentMV)
			return false;
		if (equipment == null) {
			if (other.equipment != null)
				return false;
		} else if (!equipment.equals(other.equipment))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (maxMV != other.maxMV)
			return false;
		if (money == null) {
			if (other.money != null)
				return false;
		} else if (!money.equals(other.money))
			return false;
		if (movementMultiplier != other.movementMultiplier)
			return false;
		if (sensesGroup == null) {
			if (other.sensesGroup != null)
				return false;
		} else if (!sensesGroup.equals(other.sensesGroup))
			return false;
		if (speed != other.speed)
			return false;
		if (Double.doubleToLongBits(weightCarried) != Double
				.doubleToLongBits(other.weightCarried))
			return false;
		return true;
	}
	
}
