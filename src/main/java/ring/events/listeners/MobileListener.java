package ring.events.listeners;

import java.util.EventListener;

/**
 * Mobiles can add MobileListeners to themselves in order to respond
 * to events concerning themselves, or objects contained within themselves,
 * such as Items. A MobileListener is reflexive. That is, its event-handling
 * methods are only called by the object(s) it is attached to.
 * <br/><br/>
 * It is possible to attach a MobileListener to more than one Mobile, but doing
 * so is not recommended most of the time. Using a MobileListener on multiple
 * Mobiles can cause abnormal behavior if the listener is not coded with multiple
 * Mobiles in mind. 
 * @author projectmoon
 *
 */
public interface MobileListener extends EventListener {
	/**
	 * Fired when the mobile moves in any given direction.
	 * The contextual object for this event is the Room entered.
	 * @param e
	 */
	public void mobileMoved(MobileEvent e);
	
	/**
	 * Fired when the mobile attacks something with a melee
	 * or ranged weapon. Hostile spells, items, and other 
	 * similar effects do not trigger this event. The contextual
	 * object for this event is the Affectable that was attacked.
	 * @param e
	 */
	public void mobileAttacked(MobileEvent e);
	
	/**
	 * Fired when the mobile is attacked by a melee or ranged
	 * weapon. Hostile spells, items, and other similar effects
	 * do not trigger this event. The contextual object for
	 * this event is the Mobile or other object attacking this
	 * Mobile.
	 * @param e
	 */
	public void mobileReceivedAttack(MobileEvent e);
	
	/**
	 * Fired when the mobile executes the flee command successfully.
	 * Success, in this case, is defined as the mobile successfully
	 * fleeing a battle into another room. The contextual object
	 * for this event is the Room that the mobile entered as a
	 * result of the flee.
	 * @param e
	 */
	public void mobileFleed(MobileEvent e);
	
	/**
	 * Fired when the mobile executes the look command successfully.
	 * Success, in this case, is deifned as the mobile being able
	 * to actually execute the command, not whether or not it
	 * can *see* anything. The contextual object for this event
	 * is the Room that the mobile is looking at.
	 * @param e
	 */
	public void mobileLooked(MobileEvent e);
	
	/**
	 * Fired when the mobile successfully equips an item. The contextual
	 * object for this event is the item equipped.
	 * @param e
	 */
	public void equippedItem(MobileEvent e);
	
	/**
	 * Fired when the mobile successuflly dequips an item. The contextual
	 * object for this event is the item dequipped.
	 * @param e
	 */
	public void dequippedItem(MobileEvent e);
	
	/**
	 * Fired when the mobile successfully picks up an item. The contextual
	 * object for this event is the item picked up.
	 * @param e
	 */
	public void pickedUpItem(MobileEvent e);
	
	/**
	 * Fired when the mobile successfully drops an itme. The contextual object
	 * for this event is the item dropped.
	 * @param e
	 */
	public void droppedItem(MobileEvent e);
}
