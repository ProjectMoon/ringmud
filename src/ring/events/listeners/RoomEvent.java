package ring.events.listeners;

import java.util.EventObject;

import ring.movement.Room;

/**
 * Class that represents an event that occurs in a Room. Like all
 * world object events, these events are reflexive. That is, the object
 * that fires the event also receives the event. A RoomEvent takes a
 * Room source, and a context. The context of the event is an object
 * related to the event. For example, when a mobile enters a Room,
 * the context of the event is the Mobile.
 * @author projectmoon
 *
 */
public class RoomEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private Object context;
	
	public RoomEvent(Room source, Object contextObj) {
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
