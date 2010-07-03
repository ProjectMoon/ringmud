package ring.commands.parser;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import ring.commands.annotations.Template;


public class PyDriver {
	public static void main(String[] args) {
		PythonInterpreter interp = new PythonInterpreter();
		
		interp.execfile("/Users/projectmoon/Programs/git/ringmud/src/python/ring/commands/annotations/pybridge.py");

		PyObject blah = interp.get("Blah");

		CommandParser parser;
		try {
			parser = new CommandParser(blah);
			for (CommandForm form : parser.getForms()) {
				System.out.println(form.getId());
			}
		}
		catch (CommandParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
