package ring.events;

/**
 * Interface for specifying event enums. Every event enum must implement
 * <code>getEventName()</code>. For an individual event in the enum, the
 * event name is the string name sent to the event dispatcher, and thus is
 * the name of the event function that should be used from Python in order to
 * subscribe to the event.
 * @author projectmoon
 *
 */
public interface EventEnum {
	public String getEventName();
}
