package ring.events.listeners;

import java.util.EventObject;

import ring.mobiles.Mobile;

/**
 * Represents a reflexive event for a Mobile. A MobileEvent contains both
 * the Mobile that fired the event (as the source), and the context of that event.
 * The context of a MobileEvent varies depending on the event fired. The object
 * returned from the getContext() method is generally a WorldObject of some sort.
 * See the methods of {@link ring.events.listeners.MobileListener} for more information.
 * @author projectmoon
 *
 */
public class MobileEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private Object context;
	
	/**
	 * Create a new MobileEvent with the specified source
	 * Mobile and context object.
	 * @param source
	 * @param contextObj
	 */
	public MobileEvent(Mobile source, Object contextObj) {
		super(source);
		context = contextObj;
	}
	
	/**
	 * Returns the contextual object related to this event. The contextual 
	 * object is the Room, Item, Mobile, etc that the event is related to.
	 * For example, the contextual object for an itemDropped event would be the
	 * item that was dropped. The contextual object for a mobileMoved event
	 * would be the Room moved to.
	 * @return The contextual object.
	 */
	public Object getContext() {
		return context;
	}

}
