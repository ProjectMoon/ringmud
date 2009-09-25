package ring.commands.nc;

import ring.commands.nc.*;
import org.python.util.PythonInterpreter; 
import org.python.core.*; 
import java.io.*;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class JythonIndexer implements CommandIndexer {
	private String filePath;
	private ArrayList<Command> cmds = new ArrayList<Command>();
	private static final PythonInterpreter interp = new PythonInterpreter();
	private static final Pattern JYTHON_PATTERN = Pattern.compile("^class\\s+(\\w+)\\s*\\([\\w,_\\s]*Command[\\w,_\\s]*\\):$");
	
	public JythonIndexer(String directory) {
		filePath = directory;
	}
	
	public void index() {
		System.out.println("Entering index");
		File dir = new File(filePath);
		if (dir.isDirectory()) {
			File[] pythonFiles = dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".py");
				}
			});
			
			for (File file : pythonFiles) {
				System.out.println("Finding commands in " + file.getAbsolutePath());
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line = "";
					while ((line = reader.readLine()) != null) {
						System.out.println("   Testing line: " + line);
						String className = findClassName(line);
						if (className != null) {
							instantiateCommand(file.getAbsolutePath().toString(), className);
						}
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void instantiateCommand(String filename, String name) {
		System.out.println("Adding command " + name + " to the list");
		interp.execfile(filename);
		interp.exec("_ring_obj = " + name + "()");
		Command cmd = (Command)interp.get("_ring_obj", Command.class);
		cmds.add(cmd);
	}
	
	//Finds string in the form of:
	//^class\s\w+\(TheInterface\):$
	//Pulls out the \w+
	private String findClassName(String python) {
		Matcher m = JYTHON_PATTERN.matcher(python);
		if (m.matches()) {
			return m.group(1);
		}
		else {
			return null;
		}
	}
	
	public List<Command> getCommands() {
		return cmds;
	}
	
	public static void main(String[] args) {
		System.out.println("Beginning jython indexer test");
		JythonIndexer indexer = new JythonIndexer("/home/PDSINFO/jhair/sandbox/jythonIndexer/");
		indexer.index();
		for (Command c : indexer.getCommands()) {
			c.execute(null, null);
		}
		System.out.println("Test done");
	}
}
