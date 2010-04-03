package ring.events;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.util.PythonInterpreter;

import ring.nrapi.business.AbstractBusinessObject;
import ring.python.Interpreter;

/**
 * Manages the storing and dispatching of events.
 * @author projectmoon
 *
 */
public class EventDispatcher {
	/**
	 * Map relationship defined as: canonical ID -> Map of (eventName, Event)
	 */
	private static Map<String, Map<String, Event>> events = new HashMap<String, Map<String, Event>>();
	private static boolean initialized = false;
	
	public static void initialize() {
		if (!initialized) {
			PythonInterpreter interp = Interpreter.INSTANCE.getInterpreter();
			InputStream stream = EventDispatcher.class.getClassLoader().getResourceAsStream("ring/events/events.py");
			interp.execfile(stream);
			initialized = true;
		}
	}
	
	public static void initializeEvents(String document, String filename) {
		PythonInterpreter interp = Interpreter.INSTANCE.getInterpreter();
		interp.exec("__document__ = '" + document + "'");
		interp.execfile(filename);
	}
	
	public static void initializeEvents(String document, InputStream pyStream) {
		PythonInterpreter interp = Interpreter.INSTANCE.getInterpreter();
		interp.exec("__document__ = '" + document + "'");
		interp.execfile(pyStream);
	}
	
	public static void addEvent(Event event) {
		System.out.println("Adding event: " + event);
		EventContext ctx = event.getContext();
		for (String document : ctx.getDocuments()) {
			for (String id : ctx.getIDs(document)) { 
				String canonicalID = document + ":" + id;
				Map<String, Event> eventMap = events.get(canonicalID);
				
				if (eventMap == null) {
					eventMap = new HashMap<String, Event>();
					events.put(canonicalID, eventMap);
				}
				
				assert(eventMap != null);
				
				eventMap.put(event.getName(), event);
			}
		}
	}
	
	public static List<Event> getEvents() {
		List<Event> evts = new ArrayList<Event>();
		
		for (Map<String, Event> eventMap : events.values()) {
			for (Event e : eventMap.values()) {
				evts.add(e);
			}
		}
		
		return evts;
	}
	
	public static void dispatch(String eventName, AbstractBusinessObject target) {
		if (target == null) {
			throw new IllegalArgumentException("target for event must not be null!");
		}
		
		Map<String, Event> eventMap = events.get(target.getCanonicalID());
		if (eventMap != null) {
			Event e = eventMap.get(eventName);
			if (e != null) {
				e.invoke(target);
			}
		}
	}
	
	public static void dispatch(SystemEvent event, AbstractBusinessObject target) {
		dispatch(event.getEventName(), target);
	}
}
