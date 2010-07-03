package ring.python;

import java.io.InputStream;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public enum Interpreter {
	INSTANCE;
	
	private PythonInterpreter interp;
	
	private Interpreter() {
		PySystemState.initialize();
		
		//Needed because Jython 2.5's standalone jar is
		//actually broken when it comes to reading the classpath.
		//Thus, we must add our jars manually...
		String dir = System.getenv("RING_LIB_DIR");
		
		if (dir != null) {
			PySystemState.add_extdir(dir);
		}
		
		interp = new PythonInterpreter();
	}
	
	/**
	 * Returns the Python interpreter for this VM.
	 * @return
	 */
	public PythonInterpreter getInterpreter() {
		return interp;
	}
	
	/**
	 * Gets an internal system Python script.
	 * @param name The python script filename.
	 * @return An <code>InputStream</code> pointing to the script, or null if it wasn't found.
	 */
	public InputStream getInternalScript(String name) {
		return this.getClass().getClassLoader().getResourceAsStream("ring/python/" + name);
	}
}
