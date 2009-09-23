package ring.commands.nc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-level helper class that indexes Command objects from a given Java package.
 * @author projectmoon
 *
 */
class CommandIndexer {
	private String packageName;
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private boolean indexed = false;
	
	/**
	 * Creates new CommandIndexer without a package specified.
	 */
	public CommandIndexer() {
		
	}
	
	/**
	 * Creates a new CommandIndexer to index the specified package.
	 * @param pkg
	 */
	public CommandIndexer(String pkg) {
		setPackage(pkg);
	}
	
	/**
	 * Sets the package to index.
	 * @param pkg
	 */
	public void setPackage(String pkg) {
		packageName = pkg;
	}
	
	/**
	 * Gets the package to be indexed.
	 * @return
	 */
	public String getPackage() {
		return packageName;
	}
	
	/**
	 * Performs the actual indexing operation.
	 */
	public void index() {
		String pkgName = "/" + packageName;
		pkgName = pkgName.replace('.', '/');
		URL pkgURL = CommandHandler.class.getResource(pkgName);
		File pkg = new File(pkgURL.getFile());
		
		if (pkg.exists()) {
			// Get the list of the files contained in the package
			String[] files = pkg.list();
			ArrayList<String> classList = new ArrayList<String>(files.length);
			for (int i = 0; i < files.length; i++) {

				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					//Transform into fully qualified Java class name.
					String classname = files[i].substring(0, files[i].length() - 6);
					classname = packageName + "." + classname;
					classList.add(classname);
				}
			}
			
			instantiateCommands(classList);
		}
		
		indexed = true;
	}
	
	/**
	 * Instantiates all Command objects in the list of classes in the package.
	 * @param classList
	 */
	private void instantiateCommands(List<String> classList) {
		Class<?> cmdInterface = Command.class;
		
		for (String className : classList) {
			try {
				Class<?> c = Class.forName(className);
				
				//If the sub-class c is assignable from the Command
				//interface, we can add it to the tree.
				if (cmdInterface.isAssignableFrom(c) && !c.isInterface()) {
					System.out.println("Instantiating: " + className);
					Command cmd = (Command)c.newInstance();
					commandList.add(cmd);
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Returns the list of commands. If the index() method has not been
	 * called, this method will automatically call it.
	 * @return
	 */
	public List<Command> getCommands() {
		if (!indexed) {
			index();
		}
		
		return commandList;
	}
}
