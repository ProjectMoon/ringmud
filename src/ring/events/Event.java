package ring.events;

import org.python.core.PyFunction;

/**
 * Represents a bindable (or bound) event in MUD code. An event has three
 * things: A name, a context, and a Python function. The name of the event
 * is the actual event to bind to. The context describes which documents and
 * IDs this event applies to. The Python function gets executed when this
 * event is fired.
 * @author projectmoon
 *
 */
public class Event {
	private EventContext context;
	private String eventName;
	private PyFunction func;
}
