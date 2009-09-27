package ring.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.logging.Logger;

import ring.commands.nc.Command;
import ring.commands.nc.CommandHandler;
import ring.commands.nc.CommandIndexer;
import ring.commands.nc.IndexerFactory;
import ring.jox.BeanParser;
import ring.jox.beans.RoomSet;
import ring.mobiles.Mobile;
import ring.resources.*;

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

        //Load the properites file
        MUDConfig.loadProperties();

        //Load commands
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
    private static void loadCommands() {
		Properties pkgProps = new Properties();
		Properties jythonProps = new Properties();
		
		pkgProps.setProperty("package", "ring.commands.nc");
		jythonProps.setProperty("directory", "/etc/ringmud/commands");
		CommandIndexer indexer = IndexerFactory.getIndexer("ring.commands.nc.PackageIndexer", pkgProps);
		indexer.index();
		CommandHandler.addCommands(indexer.getCommands());
	}
    
    public static void main(String[] args) {
    	loadCommands();
    	CommandHandler h = new CommandHandler(new Mobile());
    	h.sendCommand("test");
    }

	/**
     * Builds the "universe." The universe is all rooms in the world
     * composed from all files in all directories. In a normal setup,
     * there is only one data directory. However, in a setup with multiple
     * world data directories, this will make sure all rooms get linked together.
     */
    private static void buildUniverse() {
        String[] roomSetFiles = MUDConfig.getRoomSetFiles();
        RoomSet universe = new RoomSet();
        universe.setName("Universe");
        for (String filePath : roomSetFiles) {
        	RoomSet set = constructFromFiles(filePath);
        	log.info("Processed globe " + set);
        	universe.copyFrom(set);
        }
        
        universe.construct();
    }
    
    private static RoomSet constructFromFiles(String directory) {
    	File dir = new File(directory);
    	RoomSet globe = new RoomSet();
    	globe.setName(directory);
    	if (dir.isDirectory() == false) {
    		throw new MUDBootException("Data files should only be specified by directory, not absolute files.");
    	}
    	else {
    		File[] dataFiles = dir.listFiles(new XMLFileNameFilter());
    		
    		for (File dataFile : dataFiles) {
    			log.fine("Processing RoomSet " + dataFile);
    			try {
					FileInputStream stream = new FileInputStream(dataFile);
					BeanParser<RoomSet> roomParser = new BeanParser<RoomSet>();
	    			RoomSet set = roomParser.parse(stream, RoomSet.class);
	    			globe.copyFrom(set);
				} 
    			catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
    		}
    		
    		return globe;
    	}
    }
}
