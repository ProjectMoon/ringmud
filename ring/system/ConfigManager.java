package ring.system;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Manages MUD system preferences. These are internal configuration values for the
 * MUD itself. Users and developers should rarely have to mess with these, except perhaps
 * during initial setup.
 * @author jeff
 *
 */
public class ConfigManager {
	public static void main(String[] args) {
		if (args[0].equals("set")) {
			set(args[1], args[2]);
		}
		else if (args[0].equals("get")) {
			get(args[1]);
		}
		else {
			System.out.println("ConfigManager: Unrecognized option. Try get <pref> or set <pref>");
		}
	}

	protected static void get(String pref) {
		if (pref.equals("configLocation")) {
			Preferences prefs = getPrefs(ring.system.MUDConfig.class);
			System.out.println(pref + " = " + prefs.get(pref, null));
		}		
	}

	protected static void set(String pref, String value) {
		if (pref.equals("configLocation")) {
			Preferences prefs = getPrefs(ring.system.MUDConfig.class);
			prefs.put("configLocation", value);
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Set config location to: " + value);
		}
	}
	
	protected static String getString(String pref) {
		Preferences prefs = getPrefs(ring.system.MUDConfig.class);
		return prefs.get(pref, null);
	}
	
	private static Preferences getPrefs(Class c) {
		return Preferences.systemNodeForPackage(c);
	}
	
	private static Preferences getPrefs(String className) {
		try {
			Class c = Class.forName(className);
			return getPrefs(c);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
