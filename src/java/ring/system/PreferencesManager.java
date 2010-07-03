package ring.system;

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
	private static void clear(String pref) {
		boolean all = false;
		System.out.println("You are about to clear " + pref);
		
		System.out.println("Enter y to continue, or n to cancel.");
		
		String answer = readFromKeyboard();
		if (answer.equals("y")) {
			doClear(PreferencesManager.class.getName(), pref, all);
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
	/*private static String[] parseClassAndPref(String arg) {
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
	}*/

	public void execute(String[] args) {
		if (args.length == 0) {
			usage();
			System.exit(0);
		}
		
		if (args[0].equals("set")) {
			set(args[1], args[2]);
		}
		else if (args[0].equals("get")) {
			displayPreferenceValue(args[1]);
		}
		else if (args[0].equals("clear")) {
			clear(args[1]);
		}
		else if (args[0].equals("fclear")) {
			doClear(PreferencesManager.class.getName(), args[1], true);
		}
		else {
			usage();
		}
	}

	private static void usage() {
		System.out.println("PreferencesManager: Unrecognized option. Try:");
		System.out.println("\tset prefName prefValue: Sets a preference");
		System.out.println("\tget prefName: Prints preference value");
		System.out.println("\tclear prefName Clears a preference");
		System.out.println("\tfclear prefName: Clears preference without asking");
		System.out.println("Example syntax:");
		System.out.println("prefs set configLocation /path/to/configfile");
	}
	
	private static void displayPreferenceValue(String pref) {
		//TODO implement displaying list of all prefs with * character.
		String value = getString(pref);
		if (value != null)
			System.out.println(pref + " = " + value);
		else
			System.out.println("\"" + pref + "\" is not set.");
	}
	
	public static void set(String prefName, String prefValue) {
		set(PreferencesManager.class.getName(), prefName, prefValue);
	}

	private static void set(String className, String prefName, String prefValue) {
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
	
	public static String getString(String pref) {
		return getString(PreferencesManager.class.getName(), pref);
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
	
	private static Preferences getPrefs(Class<?> c) {
		return Preferences.systemNodeForPackage(c);
	}
	
	private static Preferences getPrefs(String className) {
		try {
			Class<?> c = Class.forName(className);
			return getPrefs(c);
		} catch (ClassNotFoundException e) {
			System.err.println("PreferencesManager: Unrecognized class component " + className);
			System.exit(1);
			return null; //not reachable
		}
	}

	@Override
	public boolean usesDatabase() {
		return false;
	}
}
