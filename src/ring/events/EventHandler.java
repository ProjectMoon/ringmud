package ring.events;

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
	
	public static void initializeFromFile(String filename) {
		
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
		
		EventContext ctx = new EventContext();
		//ctx.addDocument("test");
		ctx.addID("test", "test");
		ctx.addID("test2", "someID");
		
		System.out.println(ctx);
		ctx.unbindID("someID");
		System.out.println("---------");
		System.out.println(ctx);
	}
	
}
