package ring.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import ring.mobiles.*;
import ring.resources.RingResource;
import ring.resources.beans.ItemBean;
import ring.effects.*;

public abstract class Item extends Entity implements RingResource<ItemBean> {
	public static final long serialVersionUID = 1;
	protected boolean wearable;
	protected BodyPart partWornOn;
	protected Effect passiveEffects;// Magical passive effects of this item.
	protected boolean cursed;

	public static int WEAPON = 0;
	public static int ARMOR = 1;
	public static int FOOD = 2;

	public Item() {
		
	}

	public final boolean isWearable() {
		return wearable;
	}

	public final boolean isArmor() {
		return this instanceof Armor;
	}

	public final boolean isWeapon() {
		return this instanceof Weapon;
	}

	public final Effect getPassiveEffects() {
		return passiveEffects;
	}
	
	public final void setPassiveEffects(Effect effects) {
		
	}
	
	public final BodyPart getPartWornOn() {
		return partWornOn;
	}

	public final void setPartWornOn(BodyPart part) {
		partWornOn = part;
	}

	public int getEntityType() {
		return NON_LIVING;
	}

	// This is meant to be overridden... It's always called when a Mobile wears
	// an item. However,
	// most of the time it does nothing since it is not overridden. When
	// overriden this method
	// will contain special code to make the item do something sexy...
	// Yes I realize that certain Items will have a .class file all to
	// themselves but that's ok...
	// I forsee an item scripting language in the future !
	public void specialCode(Affectable wearer) {
		
	}
	
	public final boolean isCursed() {
		return cursed;
	}
	
	public final void setCursed(boolean yes) {
		cursed = yes;
	}

	public String getBeanID() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Item is an abstract class, and thus all specific item types should 
	 * override this method. This method sets properties common to all
	 * items, and subclasses should call this method to make sure all
	 * properties are set.
	 */
	public void populateFromBean(ItemBean bean) {
		super.setName(bean.getName());
	}
}
