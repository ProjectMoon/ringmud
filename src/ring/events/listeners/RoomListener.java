package ring.events.listeners;

/**
 * The RoomListener interface specifies the different events that a Room object
 * can respond to. Like the MobileListener, the RoomEvent object associated with
 * this listener uses a context object. Similarly, it is possible to add one RoomListener
 * to multiple Rooms, though it is not recommended. This listener is reflexive like
 * all world object listeners: The source of the event is always the Room that
 * the listener is attached to.
 * @author projectmoon
 *
 */
public interface RoomListener {
	/**
	 * Fired when a mobile enters this room. The contextual object
	 * for this event is the Mobile that entered.
	 * @param e
	 */
	public void mobileEntered(RoomEvent e);
	
	/**
	 * Fired when a mobile exits this room. The contextual object
	 * for this event is the Mobile that exited.
	 * @param e
	 */
	public void mobileExited(RoomEvent e);
}
