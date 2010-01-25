package ring.world;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import ring.commands.*;
import ring.entities.*;
import ring.mobiles.*;
import ring.mobiles.MobileBaseModel.Type;
import ring.players.*;
import ring.effects.*;
import ring.effects.library.*;
import ring.movement.*;
import ring.util.*;
import java.util.*;

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

	//This version sends the data to non-deaf characters in the room except for the specified Mobile.
	//Mainly used for the say command.
	public static synchronized void sendAudioToLocation(Mobile mobile, String text, String deafText) {
		Room room = (Room)mobile.getLocation();
		List<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.getBaseModel().isDeaf()) { 
					player.getCommunicator().printWithPreline(text);
				}
				else if (deafText != null && !deafText.equals("")) { 
					player.getCommunicator().printWithPreline(deafText);
				}
			}
		}
	}

	//sendVisualToLocation method.
	//This method and its variants send "visual" information to a location. That is, all non-blind
	//characters will see it appear on their screens.

	//This version sends the data to non-blind characters in the room.
	public static synchronized void sendVisualToLocation(Room room, String text, String blindText) {
		List<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if (mob.isPlayer()) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.getBaseModel().isBlind()) {
					player.getCommunicator().printWithPreline(text);
				}
				else if ((blindText != null) && (!blindText.equals(""))) {
					player.getCommunicator().printWithPreline(blindText);
				}
			}
		}
	}

	//This version sends the data to non-blind characters in the room except for the specified Mobile.
	//Mainly used for the say command.
	public static synchronized void sendVisualToLocation(Mobile mobile, String text, String blindText) {
		Room room = (Room)mobile.getLocation();
		List<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.getBaseModel().isBlind()) {
					player.getCommunicator().printWithPreline(text);
				}
				else if ((blindText != null) && (!blindText.equals(""))) {
					player.getCommunicator().printWithPreline(blindText);
				}
			}
		}
	}

	//roomArriveLeaveToLocation method.
	//This method sends a "room broadcast" message to a location. A room broadcast is an arrive/leave
	//message that people see when a mobile leaves or arrives in their current room. This could be
	//considered a hybrid of sendVisual and sendAudio, because it does not allow blind characters
	//to see the text, (they "hear" something instead), but it also does not allow non-listening mobiles
	//to see the boradcast of a mobile moving silently. Deaf mobiles will automatically not see
	//moving silently broadcast no matter what.
	public static synchronized void roomArriveLeaveToLocation(Mobile mobile, String text, String blindText) {
		Room room = (Room)mobile.getLocation();
		List<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = mobiles.get(c);
			
			if ((!mob.equals(mobile)) && (mob.isPlayer())) {
				PlayerCharacter player = (PlayerCharacter)mob;
				
				//TODO check move silently here
				/*
				if (mobile.moveSilentlyCheck > 0) { //yes, he's moving silently
					if ((!player.isDeaf) && (player.listenCheck >= mobile.moveSilentlyCheck)) { //we beat the MS check
						if (!player.isBlind) player.getCommunicator().printWithPreline(text);
						else if ((blindText != null) && (!blindText.equals(""))) player.getCommunicator().printWithPreline(blindText);
					}
				}
				*/
				if (!player.getBaseModel().isBlind()) { 
					player.getCommunicator().printWithPreline(text);
				}
				else if ((blindText != null) && (!blindText.equals(""))) {
					player.getCommunicator().printWithPreline(blindText);
				}
			} //end player if condition
		} //end for loop
	}


	//Notifies players in the room of the mobile except for the specified mobile.
	//Useful for the voice of the gods!
	public static synchronized void notifyPlayersAtLocation(Mobile mobile, String text) {
		Room room = (Room)mobile.getLocation();
		List<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				player.getCommunicator().printWithPreline(text);
			}
		}
	}

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
