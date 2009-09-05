package ring.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Main class for the jar file. Provides entry point to other classes in the system
 * such as the server or preferences manager utility.
 * @author jeff
 *
 */
public class RingMain {
	public static String USAGE_LOCATION = "ring/main/usage.txt";
	public static String MODULES_LOCATION = "ring/main/modules.properties";
	
	public static void main(String[] args) {
		RingMain main = new RingMain();
		if (args.length < 1) {
			main.usage();
		}
		else {
			String app = args[0];
			String[] appArgs = new String[args.length - 1];
			System.arraycopy(args, 1, appArgs, 0, args.length - 1);
			main.executeModule(app, appArgs);
		}
	}
	
	public void usage() {
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(USAGE_LOCATION);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void executeModule(String app, String[] appArgs) {
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(MODULES_LOCATION);
		try {
			props.load(input);
			String appClassStr = props.getProperty(app);
			Class appClass = Class.forName(appClassStr);
			Object appInstance = appClass.newInstance();
			RingModule module = (RingModule)appInstance;
			module.start(appArgs);			
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Module " + app + " is defined, but points to an invalid Java class.");
		}
		catch (ClassCastException e) {
			System.err.println(app + " exists, but is not a valid RingMUD module!");
		}
		catch (NullPointerException e) {
			System.err.println(app + " is not a defined RingMUD module");
		}
		
	}
}
