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
		Zone zone = new Zone();
		zones.addElement(zone);
                
		String s = "[WHITE]Test Room";
		String x = "[CYAN]This is the room that all testing occurs in. Here is where the [B][RED]Lord Ao[R][CYAN] will reside in the future as well.";
		Room room = new Room(s, x);
		room.setSize(20, 20, 20);
                
		String s2 = "[WHITE]Test Room 2";
		String x2 = "[CYAN]This is the OTHER room that all testing occurs in. Here is where the [B][RED]Lord Ao[R][CYAN] will reside in the future as well.";
		Room room2 = new Room(s2, x2);
		room2.setSize(30, 40, 20);
                
		String s3 = "[WHITE]Test Room 3";
		String x3 = "[CYAN]This is YET [B][RED]ANOTHER[R][CYAN] room that all testing occurs in. Here is where the [B][RED]Lord Ao[R][CYAN] will reside in the future as well.";
		Room room3 = new Room(s3, x3);
		room3.setSize(10000, 20, 10);
                
		String s4 = "[WHITE]Hidden Room!";
		String x4 = "[YELLOW]This room is [B]HIDDEN![R]";
		Room room4 = new Room(s4, x4);
		room4.setSize(15, 30, 12);
                room4.setSearchDC(10);
		
                room.sealAllExits();
                room.makeImplictExit(ZoneCoordinate.NORTH);
                room.makeImplictExit(ZoneCoordinate.WEST);
                
                room2.sealAllExits();
                room2.makeImplictExit(ZoneCoordinate.SOUTH);
                                
		//set up the room grid
                /** NEW ROOL SET UP CODE GOES HERE **/
                LocationGrid.setCoordRoom(new ZoneCoordinate(0, 0, 0, 0), room);
                LocationGrid.setCoordRoom(new ZoneCoordinate(0, 0, 1, 0), room2);
                //LocationGrid.setCoordRoom(new ZoneCoordinate(0, 0, 0, 1), room3);
                LocationGrid.setCoordRoom(new ZoneCoordinate(0, -1, 0, 0), room4);
		//roomList.put(new ZoneCoordinate(0, 0, 0, 0), room);
		//roomList.put(new ZoneCoordinate(0, 0, 1, 0), room2);
		//roomList.put(new ZoneCoordinate(0, 0, 0, 1), room3);
		//roomList.put(new ZoneCoordinate(0, -1, 0, 0), room4);                
		/*
		room.setNorthRoom(room2);
		room.setUpRoom(room3);
		room.setWestRoom(room4);
		//room.setSearchDC(Room.WEST, 10);

		//links from surrounding rooms back to main room.
		room2.setSouthRoom(room);
		room3.setDownRoom(room);
		room4.setEastRoom(room);
		*/
		
		Effect hpe = new Effect(Effect.Duration.PERMANENT, 0, new HPChange());
		EffectCreatorParameters p = new EffectCreatorParameters(); p.add("HPChange:amount", 40);
		hpe.passParameters(p);

		Armor armor = new Armor(40, hpe, BodyPart.createUniquePart(Body.HEAD), "[B][CYAN]Head Helm [RED]X90[R][WHITE]", "A", "lies here, gleaming");
		room.addEntity(armor);
		NPC mob = new NPC();
		String n = "[B][YELLOW]A Human Commoner[R][WHITE]";
		String r = "[B][YELLOW]Human[R][WHITE]";
		mob.setName(n);
		mob.setDescription("A human commoner. Doesn't look very dangerous.");
		Race r2 = new Race();
		r2.setName(r);
		mob.setRace(r2);
		worldTicker.addTickerListener(mob, "0001");
		mob.setLocation(new ZoneCoordinate(0, 0, 0, 0));
		room.addMobile(mob);
		zone.addEntrance(room);
		
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

	//This method is used to send text to players in a location.
	public synchronized void notifyPlayersAtLocation(Room room, String text) {

	}

	//sendAudioToLocation method.
	//This method and its variants send "aduio" to a location. That is, all non-deaf characters
	//will see it appear on their screens.

	//This version sends the data to non-deaf characters in the room except for the specified Mobile.
	//Mainly used for the say command.
	public static synchronized void sendAudioToLocation(Mobile mobile, String text, String deafText) {
		Room room = mobile.getLocation().getRoom();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isDeaf) player.getCommunicator().send(text);
				else if ((deafText != null) || (!deafText.equals(""))) player.getCommunicator().send(deafText);
			}
		}
	}

	//sendVisualToLocation method.
	//This method and its variants send "visual" information to a location. That is, all non-blind
	//characters will see it appear on their screens.

	//This version sends the data to non-blind characters in the room.
	public static synchronized void sendVisualToLocation(Room room, String text, String blindText) {
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if (mob.isPlayer()) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isBlind) player.getCommunicator().send(text);
				else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().send(blindText);

			}
		}
	}

	//This version sends the data to non-blind characters in the room except for the specified Mobile.
	//Mainly used for the say command.
	public static synchronized void sendVisualToLocation(Mobile mobile, String text, String blindText) {
		Room room = mobile.getLocation().getRoom();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				if (!player.isBlind) player.getCommunicator().send(text);
				else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().send(blindText);
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
		Room room = mobile.getLocation().getRoom();
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
						if (!player.isBlind) player.getCommunicator().send(text);
						else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().send(blindText);
					}
				}
				
				else { //mobile is moving regularly.
					System.out.println("in regular move");
					if (!player.isBlind) player.getCommunicator().send(text);
					else if ((blindText != null) || (!blindText.equals(""))) player.getCommunicator().send(blindText);
				}
			} //end player if condition
		} //end for loop
	}


	//Notifies players in the room of the mobile except for the specified mobile.
	//Useful for the voice of the gods!
	public static synchronized void notifyPlayersAtLocation(Mobile mobile, String text) {
		Room room = mobile.getLocation().getRoom();
		Vector mobiles = room.getMobiles();
		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile)mobiles.get(c);
			if ((mob.isPlayer()) && (!mob.equals(mobile))) {
				PlayerCharacter player = (PlayerCharacter)mob;
				player.getCommunicator().send(text);
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
