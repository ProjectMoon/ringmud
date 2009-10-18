package ring.commands.nc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Package-level indexer class that indexes Command objects from a given Java package.
 * Much of class's code was implemented by studying and/or modifying parts of the code from
 * the ClassList.java file by Kris Dover (krisdover@hotmail.com), a class for obtaining a
 * list of classes from the classpath. ClassList.java can be downloaded at 
 * http://www.xmlizer.biz/java/classloader/ClassList.java
 * @author projectmoon
 *
 */
class PackageIndexer implements CommandIndexer {
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private boolean indexed = false;
	private Properties props;
	private Set<String> indexedPackages = new HashSet<String>();
	
	/**
	 * Creates new CommandIndexer without a package specified.
	 */
	public PackageIndexer() {
		
	}
	
	public static void main(String[] args) {
		PackageIndexer i = new PackageIndexer();
		Properties pkgProps = new Properties();
		pkgProps.setProperty("pkgIndexer.package", "ring.commands.nc");
		i.setProperties(pkgProps);
		i.index();
	}
	
	/**
	 * Indexes commands from the packages specified in the properties.
	 */
	public void index() throws IllegalStateException {
		if (props == null) {
			throw new IllegalStateException("PackageIndexer: No properties! Can't index without them!");
		}
		
		//Set up the packages we are going to index first.
		String pkgList = props.getProperty("pkgIndexer.packages");
		if (pkgList == null) {
			System.err.println(this + ": Couldn't find pkgIndexer.packages property. Stopping.");
			return;
		}
		
		String[] packages = pkgList.split(";");
		for (String pkg : packages) indexedPackages.add(pkg);
		
		//Now onward with processing the classpath.
		Object[] classPath = getClassPathEntries();
		
		for (Object o : classPath) {
			String classPathEntry = null;
			if (o instanceof URL) {
				URL url = (URL)o;
				classPathEntry = url.getFile();
			}
			else if (o instanceof String) {
				classPathEntry = (String)o;
			}
			
			assert(classPathEntry != null);
			
			List<String> classFiles = getEntryFiles(classPathEntry);
			//classFiles should always exist, even if it is just an empty list.
			assert (classFiles != null);
			processClassFiles(classFiles);
		}
		
		indexed = true;
	}
	
	/**
	 * Helper method to process found files from the classpath.
	 * @param classFiles An enumeration containing either Strings or JarEntry objects.
	 */
	private void processClassFiles(List<String> classFiles) {
		if (classFiles == null) {
			throw new IllegalArgumentException("Class file list cannot be null!");
		}
		
		for (String filename : classFiles) {
			String className = filename.replace('/', '.').substring(0, filename.length() - 6);
			String classPackage = className.substring(0, className.lastIndexOf("."));
			if (indexedPackages.contains(classPackage)) {
				attemptInstantiateCommand(className);
			}
		}
	}

	/**
	 * Returns the class path entries for the PackageIndexer. It tries to cast the
	 * class loader to a URLClassLoader. If that fails, it will tokenize the system
	 * classpath.
	 * @return An array of classpath entries as either URLs or Strings.
	 */
	private Object[] getClassPathEntries() {
		try {
			URLClassLoader urlLoader = (URLClassLoader)PackageIndexer.class.getClassLoader();
			return urlLoader.getURLs();
		}
		catch (ClassCastException e) {
			return System.getProperty("java.class.path", "").split(File.pathSeparator);
		}
	}

	/**
	 * Gets a list of String filenames representing class files from the given classpath entry.
	 * This method will work on classpath entries that are jar files or simple directory structures.
	 * The filenames returned are guaranteed to end in a .class extension, but are not guaranteed to
	 * be actual Java class files.
	 * @param classPathEntry
	 * @return A list of class files from the given entry.
	 */
	private List<String> getEntryFiles(String classPathEntry) {
		//Get a list of filenames from a jar file.
		if (classPathEntry.endsWith(".jar")) {
			return getEntryFilesFromJar(classPathEntry);
		}
		//Or, get a list of filenames from a starting directory.
		else {
			return getEntryFilesFromDirectory(classPathEntry);
		}
	}
	
	/**
	 * Gets a list of class file entries from a jar file.
	 * @param classPathEntry
	 * @return
	 */
	private List<String> getEntryFilesFromJar(String classPathEntry) {
		try {
			JarFile jar = new JarFile(classPathEntry);
			Enumeration<JarEntry> entries = jar.entries();
			ArrayList<String> entryList = new ArrayList<String>(jar.size());
			
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class"))
					entryList.add(entry.getName());
			}
			
			entryList.trimToSize();
			return entryList;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>(0);
		}
	}
	
	/**
	 * Gets a list of file entries from a starting directory.
	 * @param classPathEntry
	 * @return
	 */
	private List<String> getEntryFilesFromDirectory(String classPathEntry) {
		File f = new File(classPathEntry);
		List<File> files = getFilesRecursively(f);
		List<String> filenames = new ArrayList<String>(files.size());
		
		//This loop removes the first part of the absolute path corresponding to
		//The class path entry. This allows us to find the actual class fully-qualified
		//class names (FQCNs)
		for (File file : files) {
			filenames.add(file.getAbsolutePath().substring(classPathEntry.length()));
		}
		
		return filenames;
	}
	
	/**
	 * Gets a list of File objects ending with .class recursively, from a given starting
	 * File.
	 * @param start
	 * @return The list of all .class files found.
	 */
	private List<File> getFilesRecursively(File start) {
		ArrayList<File> files = new ArrayList<File>();
		if (start.isDirectory()) {
			File[] dirFiles = start.listFiles();
			
			for (File file : dirFiles) {
				if (file.isDirectory()) {
					files.addAll(getFilesRecursively(file));
				}
				else {
					if (file.getAbsolutePath().endsWith(".class"))
						files.add(file);
				}
			}
		}
		else {
			if (start.getAbsolutePath().endsWith(".class"))
				files.add(start);
		}
		
		return files;
	}
	
	/**
	 * Attempts to instantiate a Command object from the given class name String.
	 * To be instantiated, the object must implement the Command interface.
	 * @param className
	 */
	private void attemptInstantiateCommand(String className) {
		Class<?> cmdInterface = Command.class;
		
		try {
			Class<?> c = Class.forName(className);
			
			//If the sub-class c is assignable from the Command
			//interface, we can add it to the map.
			if (cmdInterface.isAssignableFrom(c) && !c.isInterface()) {
				System.out.println(this + ": Found command " + className);
				Command cmd = (Command)c.newInstance();
				commandList.add(cmd);
			}
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	
	/**
	 * Returns the list of commands. If the index() method has not been
	 * called, this method will automatically call it.
	 * @return The list of indexed package commands.
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

	/**
	 * Gets this plugin's properties.
	 */
	public Properties getProperties() {
		return props;
	}

	/**
	 * Sets this plugin's properties.
	 */
	public void setProperties(Properties props) {
		this.props = props;
	}
	
	/**
	 * Returns "[PackageIndexer]"
	 */
	public String toString() {
		return "[PackageIndexer]";
	}
}
