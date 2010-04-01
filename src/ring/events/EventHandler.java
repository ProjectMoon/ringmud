package ring.events;

import java.io.InputStream;
import java.util.List;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * Takes care of binding events to documents and IDs.
 * This class executes Python codebehind files and extracts
 * their event definitions, then stores the events in a
 * Map.
 * @author projectmoon
 *
 */
public class EventHandler {
	private static List<Event> events;
	private PythonInterpreter interp;
	
	public EventHandler() {
		PySystemState.initialize();
		interp = new PythonInterpreter();
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("ring/events/events.py");
		interp.execfile(stream);
	}
	
	public void initializeEvents(String filename) {
		interp.execfile(filename);
	}
	
	public void initializeEvents(InputStream pyStream) {
		interp.execfile(pyStream);
	}
	
	public static void main(String[] args) {
		/*
		PythonInterpreter interp = new PythonInterpreter();
		interp.exec("def test(stuff):\n\tprint stuff\n");
		interp.exec("def test2(otherstuff):\n\tprint 'hi'\n");
		PyFunction func = interp.get("test", PyFunction.class);
		PyStringMap locals = (PyStringMap)interp.getLocals();
		
		PyList keys = locals.values();
		for (Object key : keys) {
			if (key instanceof PyFunction)
				System.out.println(key);
		}
		*/
		
		EventHandler eh = new EventHandler();
		EventDispatcher.listEvents();
		
	}
	
}
