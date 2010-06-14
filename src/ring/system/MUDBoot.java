package ring.system;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.XMLDBException;

import ring.commands.CommandHandler;
import ring.commands.CommandIndexer;
import ring.commands.IndexerFactory;
import ring.daemons.Daemon;
import ring.events.EventLoader;
import ring.intermud3.Intermud3Daemon;
import ring.movement.WorldBuilder;
import ring.nrapi.ObjectIndexSystem;
import ring.python.Interpreter;
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
	/**
	 * Boots the mud server.
	 */
	public static void boot() {
		System.out.println("Loading RingMUD.");
		
		System.out.println("Loading Jython...");
		Interpreter.INSTANCE.getInterpreter();
		
		//Load Object Index System
		System.out.println("Loading Object Index System...");
		ObjectIndexSystem.start();
		
		//Load all event handlers
		System.out.println("Loading event handlers...");
		loadEventHandlers();
		
		//Synchronize with static
		System.out.println("Synchronziing with STATIC...");
		System.err.println("WARNING: Syncing not implemented yet.");
		
		//Restore world state from DB
		System.out.println("Restoring world state...");
		System.err.println("WARNING: Restoring of world state not implemented yet.");

		//Load commands
		System.out.println("Loading commands...");
		loadCommands();

		//Load effects

		//Start the world ticker
		System.out.println("Starting the world ticker...");
		Ticker ticker = Ticker.getTicker();
		Thread t = new Thread(ticker);
		t.setName("World Ticker");
		t.start();

		// Load classes

		// Load items

		// Load NPCs

		// Load the universe (world)
		System.out.println("Building world...");
		try {
			WorldBuilder.buildWorld();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Load i3, if it is there.
		loadI3();
		
		System.out.println("Done loading RingMUD.");
	}

	/**
	 * Loads both internal commands (in packages) and Jython-based commands
	 * (from script files).
	 */
	private static void loadCommands() {
		Properties pkgProps = MUDConfig.getPluginProperties("pkgIndexer");

		CommandIndexer pkgIndexer = IndexerFactory.getIndexer(
				"ring.commands.PackageIndexer", pkgProps);
		CommandHandler.addCommands(pkgIndexer.getCommands());

		CommandIndexer jythonIndexer = IndexerFactory.getIndexer(
				"ring.commands.JythonIndexer");
		CommandHandler.addCommands(jythonIndexer.getCommands());
	}

	/**
	 * Loads event handlers.
	 */
	private static void loadEventHandlers() {
		EventLoader loader = new EventLoader();
		try {
			loader.loadEvents();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void loadI3() {
		Properties i3props = MUDConfig.getPluginProperties("i3");
		
		if (i3props != null) {
			System.out.println("Connecting to Intermud3...");
			Daemon i3 = new Intermud3Daemon();
			try {
				i3.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
