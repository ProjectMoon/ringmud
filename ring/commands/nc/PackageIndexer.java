package ring.commands.nc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Package-level helper class that indexes Command objects from a given Java package.
 * @author projectmoon
 *
 */
class PackageIndexer implements CommandIndexer {
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private boolean indexed = false;
	private Properties props;
	
	/**
	 * Creates new CommandIndexer without a package specified.
	 */
	public PackageIndexer() {
		
	}
	
	/**
	 * Performs the actual indexing operation.
	 */
	public void index() throws IllegalStateException {
		if (props == null) {
			throw new IllegalStateException("PackageIndexer: No properties! Can't index without them!");
		}
		String packageName = props.getProperty("package");
		String pkgPath = "/" + packageName;
		
		pkgPath = pkgPath.replace('.', '/');
		URL pkgURL = CommandHandler.class.getResource(pkgPath);
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
	public List<Command> getCommands() throws IllegalStateException {
		if (props == null) {
			throw new IllegalStateException("PackageIndexer: No properties! Can't get commands without them!");
		}
		
		if (!indexed) {
			index();
		}
		
		return commandList;
	}

	public Properties getProperties() {
		return props;
	}

	public void setProperties(Properties props) {
		this.props = props;
	}
}
