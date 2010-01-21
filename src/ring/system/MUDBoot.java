package ring.system;

import java.util.Properties;
import java.util.logging.Logger;

import ring.commands.Command;
import ring.commands.CommandHandler;
import ring.commands.CommandIndexer;
import ring.commands.IndexerFactory;
import ring.resources.loaders.LoaderFactory;
import ring.resources.loaders.RoomLoader;

/**
 * This class is what "boots" the MUD. It retrieves information from the
 * various data files and then loads them into memory. The boot sequence is
 * very defined; the MUD first loads all effects defined in files, followed by
 * class features and then MobileClasses. It then continues with items, NPCs,
 * and finally the world itself. Each section of the boot depends on the previous
 * section being finished and having all information available.
 * @author jeff
 */
public class MUDBoot {
	private static final Logger log = Logger.getLogger(MUDBoot.class.getName());
	
    public static void boot() {
        System.out.println("Loading RingMUD.");
        
        //Init resource loaders
        LoaderFactory.initLoaders();
        
        //Load all beans.
        LoaderFactory.loadAllBeans();
        
        //Load commands
        System.out.println("Loading commands...");
        loadCommands();
        
        //Load effects

        //Load class features
        System.out.println("Loading class features...");
        //String[] classFeatureFiles = MUDConfig.getClassFeaturesFiles();
        //for (String file : classFeatureFiles)
            //ClassFeatureLoader.loadClassFeaturesFromFile(file);

        System.out.println("Done.");
        //Load classes

        //Load items

        //Load NPCs

        //Load the universe (world)
        buildUniverse();
    }
    
    /**
     * Loads both internal commands (in packages) and Jython-based commands
     * (from script files).
     */
    public static void loadCommands() {
		Properties pkgProps = MUDConfig.getPluginProperties("pkgIndexer");
		Properties jythonProps = MUDConfig.getPluginProperties("jythonIndexer");
		
		CommandIndexer pkgIndexer = IndexerFactory.getIndexer("ring.commands.PackageIndexer", pkgProps);
		CommandHandler.addCommands(pkgIndexer.getCommands());
		
		if (jythonProps.size() > 0) {
			CommandIndexer jythonIndexer = IndexerFactory.getIndexer("ring.commands.JythonIndexer", jythonProps);
			CommandHandler.addCommands(jythonIndexer.getCommands());
		}
		else {
			System.out.println("Jython Indexer is not enabled.");
		}
		
		
		
	}
    
    private static void buildUniverse() {
    	RoomLoader roomLoader = (RoomLoader) LoaderFactory.getRoomLoader();
    	roomLoader.constructWorld();
    }
}
