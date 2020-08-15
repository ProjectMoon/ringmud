package ring.aspects;

import ring.commands.interaction.RoomLookAction;
import ring.commands.inventory.Drop;
import ring.commands.inventory.Get;
import ring.effects.Affectable;
import ring.events.listeners.MobileEvent;
import ring.events.listeners.MobileListener;
import ring.items.Item;
import ring.mobiles.BodyPart;
import ring.mobiles.Mobile;
import ring.mobiles.npc.NPC;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.players.PlayerCharacter;

/**
 * Singleton aspect that contains wire-up code for the MobileListener. This 
 * aspect is responsible for routing events for Mobiles back to their listeners.
 * Instead of having event-firing code all over the codebase, this aspect centralizes
 * it all here. This aspect also handles adding of universal listeners to mobiles.
 * @author projectmoon
 *
 */
public aspect MobileEvents {
	pointcut constructorExecution(): (execution(public NPC.new(..)) || execution(public PlayerCharacter.new(..)));
	pointcut createMob(Mobile mob): constructorExecution() && this(mob);
	
	//Add/remove method pointcuts
	pointcut addListener(Mobile mob, MobileListener listener): execution(void Mobile.addMobileListener(MobileListener)) && args(listener) && this(mob);
	pointcut removeListener(Mobile mob, MobileListener listener): execution(void Mobile.removeMobileListener(MobileListener)) && args(listener) && this(mob);
	
	//Event pointcuts
	pointcut mobileMoved(Mobile mob, Portal destination): execution(static boolean LocationManager.move(Mobile, Portal)) && args(mob, destination);
	pointcut mobileAttacked(Mobile mob, Affectable target): execution(boolean Mobile.attack(Affectable, ..)) && args(target, ..) && this(mob);
	//pointcut mobileReceivedAttack(Affectable from):
	//pointcut mobileFleed(Portal to):
	pointcut mobileLooked(RoomLookAction action): execution(void RoomLookAction.doLook()) && this(action);
	pointcut equippedItem(Mobile mob, Item item): execution(boolean Mobile.equip(BodyPart, Item)) && args(BodyPart, item) && this(mob);
	pointcut dequippedItem(Mobile mob, BodyPart part): execution(Item Mobile.dequip(BodyPart)) && args(part) && this(mob);
	pointcut pickedUpItem(Mobile mob, Item item): call(boolean Mobile.addItemToInventory(Item)) && args(item) && withincode(void Get.execute(..)) && target(mob);
	pointcut droppedItem(Mobile mob, Item item): call(boolean Mobile.removeItemFromInventory(Item)) && args(item) && withincode(void Drop.execute(..)) && target(mob);
	
	after(Mobile mob) returning(): createMob(mob) {
		
	}
	
	after(Mobile mob, MobileListener listener): addListener(mob, listener) {
		System.out.println("Put listener on " + mob);
	}
	
	after(Mobile mobile, Portal destination) returning(boolean success): mobileMoved(mobile, destination) {
		if (success) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, destination.getDestination());
				listener.mobileMoved(e);
			}
		}
	}
	
	after(Mobile mobile, Affectable target) returning(boolean success): mobileAttacked(mobile, target) {
		if (success) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, target);
				listener.mobileAttacked(e);
			}
		}
	}
	
	after(RoomLookAction action): mobileLooked(action) {
		Mobile mobile = action.getMobile();
		for (MobileListener listener : mobile.getMobileListeners()) {			
			MobileEvent e = new MobileEvent(mobile, mobile.getLocation());
			listener.mobileLooked(e);
		}		
	}
	
	after(Mobile mobile, Item item) returning(boolean success): equippedItem(mobile, item) {
		if (success) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, item);
				listener.equippedItem(e);
			}
		}
	}
	
	after(Mobile mobile, BodyPart part) returning(Item item): dequippedItem(mobile, part) {
		if (item != null) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, item);
				listener.dequippedItem(e);
			}
		}		
	}
	
	after(Mobile mobile, Item item) returning(boolean success): pickedUpItem(mobile, item) {
		if (item != null) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, item);
				listener.pickedUpItem(e);
			}
		}	
	}
	
	after(Mobile mobile, Item item) returning(boolean success): droppedItem(mobile, item) {
		if (item != null) {
			for (MobileListener listener : mobile.getMobileListeners()) {			
				MobileEvent e = new MobileEvent(mobile, item);
				listener.droppedItem(e);
			}
		}	
	}
}
