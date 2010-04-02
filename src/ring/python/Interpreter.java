package ring.python;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public enum Interpreter {
	INSTANCE;
	
	private PythonInterpreter interp;
	
	private Interpreter() {
		PySystemState.initialize();
		interp = new PythonInterpreter();
	}
	
	public PythonInterpreter getInterpreter() {
		return interp;
	}
}
