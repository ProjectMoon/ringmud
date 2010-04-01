package ring.events;

import java.util.ArrayList;
import java.util.List;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyJavaType;
import org.python.core.PyType;
import org.python.core.Py;

/**
 * Manages the storing and dispatching of events.
 * @author projectmoon
 *
 */
public class EventDispatcher {
	private static List<Event> events = new ArrayList<Event>();
	
	public EventDispatcher() {
		System.out.println("Hi from dispatcher");
	}
	
	public static void addEvent(Event event) {
		events.add(event);
	}
	
	public static void listEvents() {
		for (Event e : events) {
			System.out.println(e.getName() + ": " + e.getFunction());
			PyFunction func = e.getFunction();
			func.__call__(new PyObject[] { Py.java2py("sdf") }, new String[0]);
		}
	}
}
