package ring.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import ring.system.MUDConfig;

/**
 * Main class for the jar file. Provides entry point to other classes in the system
 * such as the server or preferences manager utility.
 * @author jeff
 *
 */
public class RingMain {
	public static String USAGE_LOCATION = "ring/main/help/";
	public static String MODULES_LOCATION = "ring/main/modules.properties";
	
	public static void main(String[] args) {
		RingMain main = new RingMain();
		if (args.length < 1 || args[0].equals("help")) {
			if (args.length <= 1) {
				main.usage(null);
			}
			else {
				main.usage(args[1]);
			}
		}
		else {
			//Load configuration as the very first thing.
			MUDConfig.loadProperties();
					
			//Load the specified module.
			String app = args[0];
			String[] appArgs = new String[args.length - 1];
			System.arraycopy(args, 1, appArgs, 0, args.length - 1);
			main.executeModule(app, appArgs);
		}
	}
	
	public void usage(String module) {
		if (module == null || module.equals("")) {
			module = "main";
		}
		
		String moduleHelp = module + "-help.txt";
		
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(USAGE_LOCATION + moduleHelp);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
		}
		catch (Exception e) {
			System.out.println("There is no help for \"" + module + "\"");
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
			module.execute(appArgs);
			
			/*
			//Support for embedded databases removed right now.
			//Shut down eXist if necessary.
			if (module.usesDatabase()) {
				//This looks a bit odd, but the DB reference is static.
				new ExistDB().shutdown();
			}
			*/
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Module " + app + " is defined, but the module class could not be found.");
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println(app + " is not a defined RingMUD module");
		}
	}
}
