package ring.events;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;

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

	public void setFunction(PyFunction func) {
		this.func = func;
	}

	public PyFunction getFunction() {
		return func;
	}
	
	public void invoke(Object target, Object ... args) {
		PyObject pyTarget = Py.java2py(target);
		
		PyObject[] eventArguments = new PyObject[args.length + 1];
		
		eventArguments[0] = pyTarget;
		
		int c = 1;
		for (Object arg : args) {
			PyObject pyArg = Py.java2py(arg);
			eventArguments[c] = pyArg;
			c++;
		}
		
		func.__call__(eventArguments, new String[0]);
	}
}
