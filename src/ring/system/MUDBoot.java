package ring.system;

import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.commands.CommandHandler;
import ring.commands.CommandIndexer;
import ring.commands.IndexerFactory;
import ring.movement.WorldBuilder;
import ring.persistence.ResourceList;
import ring.persistence.XQuery;
import ring.world.Ticker;

/**
 * This class is what "boots" the MUD. It retrieves information from the various
 * data files and then loads them into memory. The boot sequence is very
 * defined; the MUD first loads all effects defined in files, followed by class
 * features and then MobileClasses. It then continues with items, NPCs, and
 * finally the world itself. Each section of the boot depends on the previous
 * section being finished and having all information available.
 * 
 * @author jeff
 */
public class MUDBoot {
	private static final Logger log = Logger.getLogger(MUDBoot.class.getName());

	/**
	 * Boots the mud server.
	 */
	public static void boot() {
		System.out.println("Loading RingMUD.");
		
		//Load all event handlers
		System.out.println("Loading event handlers from static...");
		loadEventHandlers();
		
		// Restore world state from DB
		System.err.println("WARNING: Restoring of world state not implemented yet.");

		// Load commands
		System.out.println("Loading commands...");
		loadCommands();

		// Load effects

		// Load class features
		System.out.println("Loading class features...");
		// String[] classFeatureFiles = MUDConfig.getClassFeaturesFiles();
		// for (String file : classFeatureFiles)
		// ClassFeatureLoader.loadClassFeaturesFromFile(file);
		
		//Start the world ticker
		System.out.println("Starting the world ticker...");
		Ticker ticker = Ticker.getTicker();
		Thread t = new Thread(ticker);
		t.setName("World Ticker");
		t.start();

		System.out.println("Done.");
		// Load classes

		// Load items

		// Load NPCs

		// Load the universe (world)
		try {
			WorldBuilder.buildWorld();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads both internal commands (in packages) and Jython-based commands
	 * (from script files).
	 */
	private static void loadCommands() {
		Properties pkgProps = MUDConfig.getPluginProperties("pkgIndexer");
		Properties jythonProps = MUDConfig.getPluginProperties("jythonIndexer");

		CommandIndexer pkgIndexer = IndexerFactory.getIndexer(
				"ring.commands.PackageIndexer", pkgProps);
		CommandHandler.addCommands(pkgIndexer.getCommands());

		CommandIndexer jythonIndexer = IndexerFactory.getIndexer(
				"ring.commands.JythonIndexer", jythonProps);
		CommandHandler.addCommands(jythonIndexer.getCommands());
	}

	/**
	 * Loads event handlers.
	 */
	private static void loadEventHandlers() {
		XQuery xq = new XQuery("for $doc in //*[@codebehind != \"\"] return data($doc/@codebehind");
		try {
			ResourceList results = xq.execute();
			
			for (Resource r : results) {
				XMLResource res = (XMLResource)r;
				String documentID = res.getDocumentId();
				String pythonFile = res.getContent().toString();
				
				//The scripts are executed, and the event handler is extracted via
				//EventHandler, or some such. Basically like XMLConverter.
				//The event handler object is cleared between script executions and
				//all created event handlers are stored in a hashmap with document names
				//as the key.
				//UnmarshalListener also will now take care of binding events by
				//looking up event handlers in the hashmap. From there it will delegate
				//to retrieving any events found for the specific ID.
			}
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
