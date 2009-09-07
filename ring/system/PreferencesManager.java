package ring.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import ring.main.RingModule;

/**
 * Manages MUD system preferences. These are internal configuration values for the
 * MUD itself. Users and developers should rarely have to mess with these, except perhaps
 * during initial setup.
 * @author jeff
 *
 */
public class PreferencesManager implements RingModule {
	public static void main(String[] args) {
		//new PreferencesManager().start(new String[] { "setup", "auto" });
		PreferencesManager p = new PreferencesManager();
		
		InputStream defaultCfgStream = p.getClass().getClassLoader().getResourceAsStream("ring/main/default-config.properties");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(defaultCfgStream));
			//PrintWriter writer = new PrintWriter(new FileWriter(cfgFile));
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				//writer.println(line);
				System.out.println(line);
			}
			
			//writer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void clear(String className, String pref) {
		boolean all = false;
		if (pref.equals("*")) {
			all = true;
		}

		if (all) {
			System.out.println("You are about to clear ALL preferences for " + className + "!");
		}
		else {
			System.out.println("You are about to clear " + className + "." + pref);
		}
		
		System.out.println("Enter y to continue, or n to cancel.");
		
		String answer = readFromKeyboard();
		if (answer.equals("y")) {
			doClear(className, pref, all);
		}
		else {
			System.out.println("Clear aborted.");
		}
		
	}
	
	private static void doClear(String className, String pref, boolean all) {
		try {
			Preferences prefs = getPrefs(className);
			if (all) {
				prefs.clear();
			}
			else {
				prefs.remove(pref);
			}
			
			prefs.flush();
			System.out.println("Preference(s) cleared.");
		}
		catch (BackingStoreException e) {
			System.err.println("Error clearing: " + e.getCause().getMessage());
			System.exit(1);
		}
	}

	private static String readFromKeyboard() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	/**
	 * Parses the class and preference name, received in the form of
	 * java.pkg.name.prefName.
	 * @param string
	 * @return A String[] array of length 2, containing the class name and pref name.
	 */
	private static String[] parseClassAndPref(String arg) {
		String[] ret = new String[2];
		int lastDot = arg.lastIndexOf(".");
		
		String prefName = arg.substring(lastDot + 1, arg.length());
		String className = arg.substring(0, lastDot);
		
		if (prefName.equals("")) {
			System.err.println("Error: Can't have an empty preference name.");
			System.exit(1);
		}
		
		ret[0] = className;
		ret[1] = prefName;
		
		return ret;
	}

	public void start(String[] args) {
		if (args.length == 0) {
			usage();
			System.exit(0);
		}
		
		if (args[0].equals("setup")) {
			setup(args[1]);
		}
		else if (args[0].equals("set")) {
			String[] splitValues = parseClassAndPref(args[1]);
			String prefValue = args[2];
			
			set(splitValues[0], splitValues[1], prefValue);
		}
		else if (args[0].equals("get")) {
			displayPreferenceValue(args[1]);
		}
		else if (args[0].equals("clear")) {
			String[] splitValues = parseClassAndPref(args[1]);
			clear(splitValues[0], splitValues[1]);
		}
		else if (args[0].equals("fclear")) {
			String[] splitValues = parseClassAndPref(args[1]);
			if (splitValues[1].equals("*"))
				doClear(splitValues[0], splitValues[1], true);
			else
				doClear(splitValues[0], splitValues[1], false);
		}
		else {
			usage();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}	

	private static void usage() {
		System.out.println("PreferencesManager: Unrecognized option. Try:");
		System.out.println("setup auto: Automatically detect and set up a new configuration");
		System.out.println("setup unix: Set up a new configuration for UNIX systems.");
		System.out.println("setup windows: Set upa new configuration for Windows systems.");
		System.out.println("\tset pkg.class.preference pref: Sets a preference");
		System.out.println("\tget pkg.class.preference: Prints preference value");
		System.out.println("\tclear pkg.class.preference: Clears a preference");
		System.out.println("\tfclear pkg.class.preference: Clears preference without asking");
		System.out.println("Example syntax:");
		System.out.println("ring.system.PreferencesManager set ring.system.MUDConfig.configLocation /path/to/configfile");
	}
	
	private static void setup(String type) {
		if (type.equals("auto")) {
			//Auto will pick up either Windows or UNIX.
			//Specifically, it will default to UNIX if Windows is not the OS.
			if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
				doWindowsSetup();
			}
			else {
				new PreferencesManager().doUnixSetup();
			}
		}
		else if (type.equals("unix")) {
			new PreferencesManager().doUnixSetup();
		}
		else if (type.equals("windows")) {
			doWindowsSetup();
		}
		else {
			usage();
		}
	}
	
	private void doUnixSetup() {
		System.out.println("Setting up a new configuration for a UNIX system.");
		System.out.println("Configuration and data files will be stored in /etc/ringmud");
		
		File configPath = new File("/etc/ringmud/");
		if (configPath.mkdirs()) {
			System.out.println("Created /etc/ringmud");
			
			System.out.println("Extracting default mud.config");
			File cfgFile = new File("/etc/ringmud/mud.config");
			InputStream defaultCfgStream = this.getClass().getClassLoader().getResourceAsStream("ring/main/default-config.properties");
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(defaultCfgStream));
				PrintWriter writer = new PrintWriter(new FileWriter(cfgFile));
				
				String line = "";
				while ((line = reader.readLine()) != null) {
					writer.println(line);
				}
				
				writer.close();
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Creating data directories...");
			new File("/etc/ringmud/data/players/").mkdirs();
			new File("/etc/ringmud/data/world/").mkdirs();
		}
		else {
			System.out.println("/etc/ringmud already exists, or you have insufficient permissions.");
			System.out.println("Please delete the directory and make sure you can create it.");
			System.out.println("You will probably need to run this as root to create the directory.");
		}
	}
	
	private static void doWindowsSetup() {
		System.out.println("Setting up a new configuration for a Windows system.");
		System.out.println("Configuration and data files will be stored in C:\\etc\\ringmud");
	}

	private static void displayPreferenceValue(String pref) {
		//TODO implement displaying list of all prefs with * character.
		String value = getString(pref);
		if (value != null)
			System.out.println(pref + " = " + value);
		else
			System.out.println("\"" + pref + "\" is not set.");
	}

	protected static void set(String className, String prefName, String prefValue) {
		Preferences prefs = null;
		prefs = getPrefs(className);
		prefs.put(prefName, prefValue);
				
		try {
			prefs.flush();
			System.out.println("Set " + className + "." + prefName + " = " + prefValue);
		} 
		catch (BackingStoreException e) {
			System.err.println("Error setting perference: " + e.getCause().getMessage());
		}
		
	}
	
	protected static String getString(String pref) {
		Preferences prefs = getPrefs(ring.system.MUDConfig.class);
		String[] splitValues = parseClassAndPref(pref);
		return getString(splitValues[0], splitValues[1]);
	}
	
	protected static String getString(String className, String pref) {
		try {
			Class<?> c = Class.forName(className);
			Preferences prefs = getPrefs(c);
			return prefs.get(pref, null);
		} catch (ClassNotFoundException e) {
			System.err.println("PreferencesManager: Unrecgonized class component " + className);
			System.exit(1);
			return null;
		}
		
		
	}
	
	private static Preferences getPrefs(Class c) {
		return Preferences.systemNodeForPackage(c);
	}
	
	private static Preferences getPrefs(String className) {
		try {
			Class c = Class.forName(className);
			return getPrefs(c);
		} catch (ClassNotFoundException e) {
			System.err.println("PreferencesManager: Unrecognized class component " + className);
			System.exit(1);
			return null; //not reachable
		}
	}
}
