package ring.events;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.util.PythonInterpreter;

import ring.persistence.Persistable;
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
	
	/**
	 * A simpler map for special global events such as command execution.
	 */
	private static Map<String, Event> globalEvents = new HashMap<String, Event>();
	
	private static boolean initialized = false;
	
	public static void initialize() {
		if (!initialized) {
			PythonInterpreter interp = Interpreter.INSTANCE.getInterpreter();
			InputStream stream = Interpreter.INSTANCE.getInternalScript("events.py");
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
	
	public static void addGlobalEvent(Event event) {
		throw new UnsupportedOperationException("Global events not yet supported.");
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
	
	public static void dispatch(String eventName, Persistable target, Object ... args) {
		if (target == null) {
			throw new IllegalArgumentException("target for event must not be null!");
		}
		
		Map<String, Event> eventMap = events.get(target.getCanonicalID());
		if (eventMap != null) {
			Event e = eventMap.get(eventName);
			if (e != null) {
				e.invoke(target, args);
			}
		}
	}
	
	public static void dispatch(EventEnum event, Persistable target, Object ... args) {
		dispatch(event.getEventName(), target, args);
	}
	
	public static void dispatchGlobal(String eventName, Object target, Object ... args) {
		Event e = globalEvents.get(eventName);
		if (e != null) {
			e.invoke(target, args);
		}
	}
	
	public static void dispatchGlobal(EventEnum event, Object target, Object ... args) {
		dispatchGlobal(event.getEventName(), target, args);
	}
}
