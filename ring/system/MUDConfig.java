package ring.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the front-end to the MUD config file. Configuration is stored using
 * the Properties API, with simple key=value pairs. The MUD's config file allows
 * a number of different options to bje used. These mostly affect where data is
 * loaded from, password policy, maximum number of connections, and the like.
 * @author jh44695
 */
public class MUDConfig {
	private static Logger log = Logger.getLogger(MUDConfig.class.getName());
    private static Properties config;
    private static final String SEP = System.getProperty("file.separator");
    
    public static void loadProperties() {
        config = new Properties(loadDefaults());
        try {
        	String path = PreferencesManager.getString("ring.system.MUDConfig.configLocation") + SEP + "mud.config";
            System.out.println("Loading MUD configuration: " + path);
            config.load(new FileInputStream(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("Couldn't find the config file. Loading default values.");
            loadDefaults();
        }
        catch (Exception e) {
            System.err.println("There was an error loading the program configuration:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Properties loadDefaults() {
    	InputStream input = MUDConfig.class.getClassLoader().getResourceAsStream("ring/main/default-config.properties");
    	Properties props = new Properties();
    	try {
			props.load(input);
		}
    	catch (IOException e) {
			log.log(Level.SEVERE, "Problem loading default properties", e);
		}
    	return props;
    }

    public static String getServerIP() {
    	return config.getProperty("server.address");
    }
    
    public static String getRoomProvider() {
    	return config.getProperty("room.provider");
    }
    
    public static String getPortalProvider() {
    	return config.getProperty("portal.provider");
    }
    
    public static String[] getClassFeaturesFiles() {
        String paths = config.getProperty("data.classFeatures");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getItemFiles() {
        String paths = config.getProperty("data.items");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getNPCFiles() {
        String paths = config.getProperty("data.mobiles");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getRoomSetFiles() {
        String paths = config.getProperty("data.world");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getPlayerSavePath() {
        String paths = config.getProperty("data.players");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String getSaveDirectory() {
        return config.getProperty("data.saveDir");
    }
    
	public static long getTimeoutLimit() {
		return Long.parseLong(config.getProperty("connection.timeout"));
	}
	
	public static String[] getJythonCommands() {
		return config.getProperty("scripts.commands").split(";");
	}

	public static String getItemProvider() {
		return config.getProperty("items.provider");
	}

}
