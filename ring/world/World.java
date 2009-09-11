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
import ring.players.*;
import ring.spells.*;
import ring.effects.*;
import ring.effects.library.*;
import ring.movement.*;
import ring.util.*;
import java.util.*;
import ring.resources.ClassFeatureLoader;

public class World implements TickerListener {
	private static World world;
	private static Vector playerList;
	private static Vector zones;
	private static Vector rooms;

	//Stuff of the world.
	private static Vector mobiles;
	private static Ticker worldTicker;
	private static Vector races;
	private static Vector classes;

	//World flags.
	private static boolean isShutDown;

	//Player threads.
	ThreadGroup playerThreads = new ThreadGroup("Player Threads");

	//Other stuff.
	private static Date serverStarted;
	private static int minimumPlayerType = PlayerCharacter.MORTAL;

	//World Constants.
	public static final int TIMEOUT_LIMIT = 15;
	public Room r1 = new Room("Room 1", "This is the description for room 1");
	
	public World() {                
		//Instantiate all of the variables.
		System.out.println("Instantiating world variables...");
		playerList = new Vector();
		zones = new Vector();
		rooms = new Vector();
		mobiles = new Vector();
		races = new Vector();
		classes = new Vector();
		/** room init line here? **/
		isShutDown = false;
		System.out.println("Done.");

		//Start up the world ticker.
		System.out.println("Instantiating the World Ticker...");
		worldTicker = new Ticker();
		System.out.println("Done.");

		//Build zones.
		//RIGHT NOW THIS IS A TEST PIECE OF CODE FOR THE WORLD
		//YEP
		System.out.println("Building zones...");
		Room r2 = new Room("Room 2", "This is the description for room 2");
		Room r3 = new Room("Room 3", "This is the description for room 3");
		Room r4 = new Room("Room 4", "This is the description for room 4");
		
		System.out.println("Room information:");
		System.out.println("Room1: " + r1);
		System.out.println("Room2: " + r2);
		System.out.println("Room3: " + r3);
		System.out.println("Room4: " + r4);
	
		//Room portals.
		//Room 2 is north of room 1.
		Portal r2port = new Portal(r2, "north");
		
		//Room 3 is east of room 2.
		Portal r3port = new Portal(r3, "east");
		
		//Room 4 is south of room 3.
		Portal r4port = new Portal(r4, "south");
		
		//Room 1 is west of room 4.
		Portal r1port = new Portal(r1, "west");
		
		System.out.println("Portal information: ");
		System.out.println(r2port);
		System.out.println(r3port);
		System.out.println(r4port);
		System.out.println(r1port);
	
		//Link the rooms.
		//System.out.println("Link room 1 to room 2: " + LocationManager.addToGrid(r1, r2port, true));
		//System.out.println("Link room 2 to room 3: " + LocationManager.addToGrid(r2, r3port, true));
		//System.out.println("Link room 3 to room 4: " + LocationManager.addToGrid(r3, r4port, true));
		//System.out.println("Link room 4 to room 1: " + LocationManager.addToGrid(r4, r1port, true));
		
		Effect hpe = new Effect(Effect.Duration.PERMANENT, 0, new HPChange());
		EffectCreatorParameters p = new EffectCreatorParameters(); p.add("HPChange:amount", 40);
		hpe.passParameters(p);

		Armor armor = new Armor(40, hpe, Body.HEAD, "[B][CYAN]Head Helm [RED]X90[R][WHITE]", "A", "lies here, gleaming");
		r1.addEntity(armor);
		NPC mob = new NPC();
		String n = "[B][YELLOW]A Human Commoner[R][WHITE]";
		String r = "[B][YELLOW]Human[R][WHITE]";
		mob.setName(n);
		mob.setLongDescription("A human commoner. Doesn't look very dangerous.");
		Race ra2 = new Race();
		ra2.setName(r);
		mob.setRace(ra2);
		worldTicker.addTickerListener(mob, "0001");
		
		//mob.setLocation(r1);
		//r1.addMobile(mob);
		
		System.out.println("Done.");

		//Add a listener and start the ticker.
		System.out.println("Starting the world ticker...");
		worldTicker.addTickerListener(this, "PULSE");
		worldTicker.start();

		world = this;
		System.out.println("Done.");
		System.out.println("World Initialization complete.");
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
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isDeaf) player.getCommunicator().sendWithPreLine(text);
				else if ((deafText != null) || (!deafText.equals(""))) player.getCommunicator().sendWithPreLine(deafText);
			}
		}
	}

	//sendVisualToLocation method.
	//This method and its variants send "visual" information to a location. That is, all non-blind
	//characters will see it appear on their screens.

	//This version sends the data to non-blind characters in the room.
	public static synchronized void sendVisualToLocation(Room room, String text, String blindText) {
		Vector<Mobile> mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if (mob.isPlayer()) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isBlind) player.getCommunicator().sendWithPreLine(text);
				else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().sendWithPreLine(blindText);

			}
		}
	}

	//This version sends the data to non-blind characters in the room except for the specified Mobile.
	//Mainly used for the say command.
	public static synchronized void sendVisualToLocation(Mobile mobile, String text, String blindText) {
		Room room = (Room)mobile.getLocation();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isBlind) player.getCommunicator().sendWithPreLine(text);
				else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().sendWithPreLine(blindText);
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
		System.out.println("in ArriveLeave");
		Room room = (Room)mobile.getLocation();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			
			if ((!mob.equals(mobile)) && (mob.isPlayer())) {
				PlayerCharacter player = (PlayerCharacter)mob;
				
				//we have now established that this mob is a player and should receive this message.
				//but is our excluded mobile ("mobile") moving silently? if so, we have other
				//conditions to worry about.
				if (mobile.moveSilentlyCheck > 0) { //yes, he's moving silently
					if ((!player.isDeaf) && (player.listenCheck >= mobile.moveSilentlyCheck)) { //we beat the MS check
						if (!player.isBlind) player.getCommunicator().sendWithPreLine(text);
						else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().sendWithPreLine(blindText);
					}
				}
				
				else { //mobile is moving regularly.
					System.out.println("in regular move");
					if (!player.isBlind) player.getCommunicator().sendWithPreLine(text);
					else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().sendWithPreLine(blindText);
				}
			} //end player if condition
		} //end for loop
	}


	//Notifies players in the room of the mobile except for the specified mobile.
	//Useful for the voice of the gods!
	public static synchronized void notifyPlayersAtLocation(Mobile mobile, String text) {
		Room room = (Room)mobile.getLocation();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				player.getCommunicator().sendWithPreLine(text);
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

	//getZoneList method.
	//This returns the zone list.
	public Vector getZoneList() {
		return zones;
	}

	//getPlayerThreadGroup method.
	//Returns the player thread group.
	public ThreadGroup getPlayerThreadGroup() {
		return playerThreads;
	}

	//getPlayers method.
	//Returns the player list.
	public Vector getPlayers() {
		return playerList;
	}

	//isShutdown method.
	//Determines if the world is shutdown or not.
	public static boolean isShutdown() {
		return isShutDown;
	}

}
