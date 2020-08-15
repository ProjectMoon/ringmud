package ring.events;

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

	public void setContext(EventContext ctx) {
		context = ctx;
	}
	
	public EventContext getContext() {
		return context;
	}

	public void setName(String eventName) {
		this.eventName = eventName;
	}

	public String getName() {
		return eventName;
	}
	
	public void invoke(Object target, Object ... args) {
		System.err.println("[FIXME] Event invocation on " + target + " with " + args + " is not implemented!");
	}
}
