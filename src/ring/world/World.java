package ring.world;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import ring.players.PlayerCharacter;

public class World implements TickerListener {
	private static World world;
	private static ArrayList<PlayerCharacter> playerList;

	//Stuff of the world.
	private static Ticker worldTicker;

	//World Constants.
	public static final int TIMEOUT_LIMIT = 15;
		
	public World() {                
		//Instantiate all of the variables.
		System.out.println("Instantiating world variables...");
		playerList = new ArrayList<PlayerCharacter>();
		System.out.println("Done.");

		//Start up the world ticker.
		System.out.println("Instantiating the World Ticker...");
		worldTicker = new Ticker();
		System.out.println("Done.");

		//Add a listener and start the ticker.
		System.out.println("Starting the world ticker...");
		worldTicker.addTickerListener(this, "PULSE");
		worldTicker.start();

		world = this;
		System.out.println("Done.");
		System.out.println("World Initialization complete.");
	}
	
	public static void initWorld() {
		if (world == null) {
			world = new World();
		}
	}

	//loadZones method.
	//This method loads all of the valid zones and locations(Rooms) in the zone directory.
	//It uses the helper method buildZone to build the locations of the zone itself. The
	//locations are all linked together and put into the locations vector of the World
	//object as the method completes itself.

	//processTick method.
	public void processTick(TickerEvent e) {
		System.gc();
		System.runFinalization();
	}

	//This method will notify all gods online of something.
	public synchronized void notifyGods(String text) {

	}
        
	//sendAudioToLocation method.
	//This method and its variants send "aduio" to a location. That is, all non-deaf characters
	//will see it appear on their screens.

	//getTicker method.
	public Ticker getTicker() {
		return worldTicker;
	}

	//*************
	//BEGIN FLAG AND GET METHODS
	//getWorld method.
	//This method returns the world object.
	public static World getWorld() {
		return world;
	}

	//getPlayers method.
	//Returns the player list.
	public List<PlayerCharacter> getPlayers() {
		return playerList;
	}
}
