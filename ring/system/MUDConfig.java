package ring.system;

import java.net.MalformedURLException;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.net.*;

/**
 * This is the front-end to the MUD config file. Configuration is stored using
 * the Properties API, with simple key=value pairs. The MUD's config file allows
 * a number of different options to be used. These mostly affect where data is
 * loaded from, password policy, maximum number of connections, and the like.
 * @author jh44695
 */
public class MUDConfig {
    private static Properties config;
    private static final String SEP = System.getProperty("file.separator");
    private static final String CFG_PATH = System.getProperty("user.home") +
                                            SEP + "RingMUD" + SEP;

    private static final String DEFAULT_DATA_PATH = CFG_PATH + "data" + SEP;

    public static void loadProperties() {
        config = new Properties();
        try {
            System.out.print("Loading MUD configuration: ");
            System.out.println(CFG_PATH + "mud.config");
            config.load(new FileInputStream(CFG_PATH + "mud.config"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Couldn't find the config file. Loading default values.");
            loadDefaults();
        }
        catch (Exception e) {
            System.err.println("There was an error loading the program configuration");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadDefaults() {
          	try {
            config.setProperty("class_features_data", DEFAULT_DATA_PATH + "classfeatures.xml");
            config.setProperty("item_data", DEFAULT_DATA_PATH + "items.xml");
            config.setProperty("npc_data", DEFAULT_DATA_PATH + "npcs.xml");
            config.setProperty("zone_data", DEFAULT_DATA_PATH + "world.xml");
            config.setProperty("world_state_file", DEFAULT_DATA_PATH + "world.state");
            config.setProperty("player_saves", DEFAULT_DATA_PATH + "players" + SEP);

            String comments = "RingMUD configuration file\n" +
                    "This is the main config file for the MUD\n" +
                    "Default values should be suitable for most systems.\n" +
                    "However, if you wish to include additional data sources\n" +
                    "Specify them via full path name and separate with semicolons (;).";

            makeDirectories();
            String path = Configmanager.getstring("ring.system.MUDConfig.configLocation");
            
            URL url = null;
            URI uri = null;
            string urlString = path;
            
            try { 
            	uri = new URI(url.toString());
            } catch (URISyntaxException e) {
            	e.printStackTrace();
            }
            
            try {
            	url = new URL(urlString);
            } catch (MalformedURLException e) {
            	e.printStackTrace();
            }
            config.store(path, comments);
               
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeDirectories() {
        File f = new File(CFG_PATH);
        f.mkdir();

        f = new File(DEFAULT_DATA_PATH);
        f.mkdir();
    }

    public static String[] getClassFeaturesFiles() {
        String paths = config.getProperty("class_features_data");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getItemFiles() {
        String paths = config.getProperty("item_data");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getNPCFiles() {
        String paths = config.getProperty("npc_data");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getZoneFiles() {
        String paths = config.getProperty("zone_data");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getPlayerSavePath() {
        String paths = config.getProperty("player_saves");
        String[] ret = paths.split(";");
        return ret;
    }

    public static String[] getWorldStateFile() {
        String paths = config.getProperty("world_state_file");
        String[] ret = paths.split(";");
        return ret;
    }

}
