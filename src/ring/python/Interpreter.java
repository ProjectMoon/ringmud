package ring.python;

import java.io.InputStream;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public enum Interpreter {
	INSTANCE;
	
	private PythonInterpreter interp;
	
	private Interpreter() {
		PySystemState.initialize();
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
