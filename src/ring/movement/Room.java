package ring.movement;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.List;
import java.util.Random;
import java.util.Vector;

import ring.entities.Entity;
import ring.mobiles.Mobile;
import ring.resources.RingResource;
import ring.resources.beans.RoomBean;
import ring.world.World;

/*
 TODO:
 -change changeLocation code to use MOVE_ constants instead of current Room[] exit list.
 -get rid of Room[] exit list.
 -change getRoomByDirection to use MOVE_ constants
 -change hiddensearchDCs to use MOVE_ constants
 -remove regular direction constants
 -
 */

public class Room implements Location, RingResource<RoomBean> {
	// Room.NO_EXIT constant: This tells us that there is no exit in a given
	// direction.
	public static final Room NO_EXIT = new Room();
	private static final String NO_EXITS_STRING = "[B][RED]NO EXITS![R][WHITE]";

	// Room data.
	
	// Unique ID for this room in its set.
	private String roomID;
	
	// Mobiles present in the Room.
	private Vector<Mobile> mobiles;

	// Entities present in the Room.
	private Vector<Entity> entities;

	// Title of the Room.
	private String title;

	// Detailed description of the Room.
	private String text;

	// The size of this room in FEET. These ones are default and should be
	// removed after ALPHA is complete.
	private int length = 1;
	private int width = 1;
	private int height = 1;

	// the search check required to find this room from another room.
	private int searchDC;

	// a string that tells us all the exits in this room. only updated when the
	// exits to a room
	// are changed, which isn't usually.
	// private String cachedExitsString;

	public Room() {
		title = "NO EXIT";
		text = "NO EXIT";
		mobiles = new Vector<Mobile>();
		entities = new Vector<Entity>();
		searchDC = 0;
	}

	// EMPTY ROOM CONSTRUCTOR.
	// Room constructor to construct an empty room with a non-default title and
	// text.
	// Exits are not set from this method.
	public Room(String title, String text) {
		this.title = title;
		this.text = text;
		mobiles = new Vector<Mobile>();
		entities = new Vector<Entity>();
		searchDC = 0;
	}

	// MOBILES BUT NO ENTITIES CONSTRUCTOR.
	// This constructor is called by the Zone Builder if MOBILES BUT NO ENTITIES
	// are put in
	// the Room by default.
	public Room(String title, String text, Mobile ... mobileList) {
		this.title = title;
		this.text = text;
		mobiles = new Vector<Mobile>();
		entities = new Vector<Entity>();

		// loop through mobileList and add them all to the mobiles vector.
		for (int c = 0; c < mobileList.length; c++)
			mobiles.addElement(mobileList[c]);
	}

	// ENTITIES BUT NO MOBILES CONSTRUCTOR.
	// This constructor is called by the Zone Builder if ITEMS BUT NO MOBILES
	// are put in the Room
	// by default.
	public Room(String title, String text, Entity ... entList) {
		this.title = title;
		this.text = text;
		mobiles = new Vector<Mobile>();
		entities = new Vector<Entity>();

		// loop through mobileList and add them all to the items vector.
		for (int c = 0; c < entList.length; c++)
			entities.addElement(entList[c]);
	}

	// ENTITIES AND MOBILES CONSTRUCTOR.
	// This constructor is called by the Zone Builder if there are ITEMS AND
	// MOBILES put in the
	// Room by default.
	public Room(String title, String text, Mobile[] mobileList, Entity[] entList) {
		this.title = title;
		this.text = text;
		mobiles = new Vector<Mobile>();
		entities = new Vector<Entity>();

		// loop through mobileList and add them all to the mobiles vector.
		for (int c = 0; c < mobileList.length; c++)
			mobiles.addElement(mobileList[c]);

		// loop through itemList and add them all to the item vector.
		for (int c = 0; c < entList.length; c++)
			entities.addElement(entList[c]);
	}

	// ****************
	// END CONSTRUCTOR SECTION OF CODE
	// #########################################################

	public String getID() {
		return roomID;
	}
	
	public void setID(String id) {
		roomID = id;
	}
	
	
	
	/**
	 * Convenience method for getting a list of exits in a
	 * Room. Only returns exits that are plainly visible.
	 * That is, it returns exits that have a search DC <= 0.
	 * @return The list of exits.
	 */
	public String getExitsString() {
		return getExitsString(0);	
	}

	/**
	 * Convenience method for getting a list of exits in a Room,
	 * including hidden ones that are visible given the search check
	 * parameter.
	 * @param searchCheck
	 * @return The list of exits found based on the search check result.
	 */
	public String getExitsString(int searchCheck) {
		List<Portal> exits = LocationManager.getPortals(this);
		
		if (exits == null) {
			return NO_EXITS_STRING;
		}
		
		String exitString = "[CYAN]";
		
		//Add all exits to the string that are
		//that the search check allows us to see.
		for (Portal p : exits) {
			if (p.getSearchDC() > 0) {
				if (searchCheck >= p.getSearchDC()) {
					exitString += p.getDisplayName() + ", ";
				}
			}
			else {
				exitString += p.getDisplayName() + ", ";
			}
		}
		
		//Remove the last ", " and return
		//Or, if we have nothing... return an empty string. Bit of an ugly check.
		if (exitString.equals("[CYAN]")) {
			return "";
		}
		else {
			return exitString.substring(0, exitString.length() - 2);
		}
	}
	
	/**
	 * Method defined by the Location interface. Queries the Location Manager for finding
	 * the desired Portal. This method is equivalent to calling the following method:
	 * LocationManager.getDestination(roomObj, direction).
	 * @param direction The string identifier of the desired Portal.
	 * @return The Portal object, if one exists. null otherwise.
	 */
	public Portal getPortal(String direction) {
		try {
			return LocationManager.getPortal(this, direction);
		}
		catch (PortalNotFoundException e) {
			//TODO log this.
			e.printStackTrace();
			System.out.println("uh oh");
			return null;
		}
	}

	// addEntity method. This method adds an entity such as an item to the list.
	// If the entity list is empty, it is created.
	public void addEntity(Entity ent) {
		entities.addElement(ent);
	}

	// addMobile method. This adds a mobile (whether it be an NPC or PC) to the
	// room. It should
	// be called by changeLocation automatically when a player or NPC moves to
	// another room.
	public void addMobile(Mobile mobile) {
		mobiles.addElement(mobile);
	}

	// removeEntity method. This method removes an entity from the room. This
	// should be called
	// when the item is picked up, destroyed, etc.
	public void removeEntity(Entity ent) {
		entities.removeElement(ent);
	}

	// removeMobile method. This method is called when a mobile leaves a room.
	// It should be
	// called by changeLocation when the mobile is leaving the room.
	public void removeMobile(Mobile mobile) {
		mobiles.removeElement(mobile);
	}

	// getMobileList method. (regular version)
	// This method returns a string of all the current mobiles in the room,
	// excluding the specified mobile.
	public String getMobileList(Mobile excludedMobile) {
		String res = "";
		if (mobiles == null || mobiles.isEmpty())
			return res;

		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile) mobiles.get(c);
			if (!mob.equals(excludedMobile))
				res += mob.getShortDescription() + "[R][WHITE] is here.\n";
		}

		return res;
	}

	// getMobileList method. (spot check version)
	// This method returns a string of all the current mobiles in the room,
	// excluding the specified mobile. It also takes a spot check number
	// to deal with hiding mobiles.
	public String getMobileList(Mobile excludedMobile, int spotCheck) {
		String res = "";
		if (mobiles == null || mobiles.isEmpty())
			return res;

		for (int c = 0; c < mobiles.size(); c++) {
			Mobile mob = (Mobile) mobiles.get(c);
			if (!mob.equals(excludedMobile)) {
				if (spotCheck >= mob.hideCheck) {
					if (mob.hideCheck > 0)
						res += mob.getShortDescription()
								+ "[R][WHITE] is here. ([B][YELLOW]hiding[R][WHITE])\n";
					else
						res += mob.getShortDescription()
								+ "[R][WHITE] is here.\n";
				}

			}
		}

		return res;
	}

	// getEntityList method.
	// This method returns a string of all of the current entities in the room.
	public String getEntityList() {
		String res = "";

		if (entities == null || entities.isEmpty())
			return res;

		for (int c = 0; c < entities.size(); c++) {
			Entity ent = (Entity) entities.get(c);
			if (c != entities.size() - 1)
				res += "[R][WHITE]" + ent.getIndefiniteDescriptor() + " "
						+ ent.getName() + " " + ent.getIdleDescriptor()
						+ "[R][WHITE].\n";
			else
				res += "[R][WHITE]" + ent.getIndefiniteDescriptor() + " "
						+ ent.getName() + " " + ent.getIdleDescriptor()
						+ "[R][WHITE].";
		}

		return res;
	}

	  
	//generateBlindExitsString method. 
	//This generates a random set of exits based on a search check.
	//It's used for the search command when
	//the user is blind. It should unveil random NON-HIDDEN exits based on the following
	//criteria:
	//DC 10: 2 random exits.
	//DC 15: 4 random exits.
	//DC 20: 6 random exits.
	//This method assumes the check is at least 10.
	//TODO: This needs to not have the possibility of generating an exit twice.
	public String generateBlindExitsString(int check) { 
		//TODO: implement this.
		throw new UnsupportedOperationException();
	}
	
	// generateBlindRandomOccupantList method.
	// This generates a random list of Mobiles or Entities in the room for a
	// blind mobile based on a search
	// check DC as per the following guidelines:
	// DC < 25: no information revealed.
	// Each point above 25 reveals one mobile or entity.
	//TODO: Implement better randomness for revealing mobs and entities
	public String generateBlindRandomOccupantList(int check) {
		if (check <= 25)
			return "None.";

		int numToReveal = check - 25;
		Vector<Mobile> mobs = getMobiles();
		Vector<Entity> ents = getEntities();
		Random gen = new Random(System.currentTimeMillis());
		String res = "";
		int creatures = 0;
		int objects = 0;

		for (int c = 0; c < numToReveal; c++) {
			if (c % 2 == 0) { // reveal a mobile
				int i = gen.nextInt(mobs.size());
				if (mobs.size() > 0) {
					Mobile mob = (Mobile) mobs.get(i);
					if (mob != null)
						creatures++;
				}

			}

			else { // reveal an entity
				int i = gen.nextInt(mobs.size());
				if (ents.size() > 0) {
					Entity ent = (Entity) ents.get(i);
					if (ent != null)
						objects++;
				}
			}
		}

		if ((creatures == 0) && (objects == 0))
			return "None.";

		res = creatures + " creatures and " + objects + " objects.";

		return res;
	}

	// getSearchDC method.
	// Gets the Search DC of this room
	public int getSearchDC() {
		return searchDC;
	}

	// setSearchDC method.
	// Sets the Search DC of this room
	public void setSearchDC(int dc) {
		searchDC = dc;
	}

	// getMobiles method.
	// Returns the list of mobiles.
	public Vector<Mobile> getMobiles() {
		return mobiles;
	}

	// getEntities method.
	// Returns the list of entities.
	public Vector<Entity> getEntities() {
		return entities;
	}

	// getTitle method.
	// Returns the title of this room.
	public String getTitle() {
		return title + "[R][WHITE] (" + length + " ft long, " + width
				+ " ft wide, " + height + " ft tall)";
	}

	// getDescription method.
	// Returns the description of this room.
	public String getDescription() {
		return text;
	}

	// set the three dimensional size of this room.
	public void setSize(int length, int width, int height) {
		this.length = length;
		this.width = width;
		this.height = height;
	}

	public int height() {
		return height;
	}

	public int length() {
		return length;
	}

	public int width() {
		return width;
	}

	/**
	 * At the moment, all Movables can enter a Room. Probably can implement search DCs in here.
	 */
	public boolean canEnter(Movable m) {
		return true;
	}

	public void movableEnters(Movable m, Portal from) {
		System.out.println(m + " has arrived from " + from.getDisplayName());
		System.out.println("new room: " + this.getTitle());
		
		Mobile mob = (Mobile)m;
	
		// default arrive and leave text to broadcast to others
		String arriveText = mob.getName() + " [R][WHITE]arrives from "
				+ from.getDisplayName() + ".";

		// is our mobile hiding? if so, make some vague leave/arrive text.
		if (mob.hideCheck > 0) {
			arriveText = "In the corner of your eye, you see someone (or something) arrive from "
					+ from.getDisplayName() + " and disappear.";
		}

		mob.resetChecks(); // reset the character's search, listen, and
							// spot checks.

		World.roomArriveLeaveToLocation(mob, arriveText, "[R][WHITE]You hear the sounds of someone arriving.");

		// subtract the right amount of move points.
		mob.changeCurrentMV(-1);

		// display the new information of the room
		if (mob.isBlind)
			mob.sendData("You stumble into a new area, but you cannot see anything!");
		else {
			mob.sendData(this.getTitle() + "\n"
				+ this.getDescription() + "\n"
				+ this.getExitsString(mob.getSearchCheck()) + "\n"
				+ this.getMobileList(mob, mob.spotCheck)
				+ this.getEntityList()
			);
		}
	}

	public void movableLeaves(Movable m, Portal to) {
		System.out.println(m + " leaves to " + to.getDisplayName());
		
		Mobile mob = (Mobile)m;
		
		// default arrive and leave text to broadcast to others
		String arriveText = mob.getName() + " [R][WHITE]leaves to "
				+ to.getDisplayName() + ".";

		// is our mobile hiding? if so, make some vague leave/arrive text.
		if (mob.hideCheck > 0) {
			arriveText = "In the corner of your eye, you see someone (or something) leave to "
					+ to.getDisplayName() + " and disappear.";
		}

		mob.resetChecks(); // reset the character's search, listen, and
							// spot checks.

		World.roomArriveLeaveToLocation(mob, arriveText, "[R][WHITE]You hear the sounds of someone leaving.");
	}

	public void populateFromBean(RoomBean bean) {
		this.roomID = bean.getID();
		this.title = bean.getTitle();
		this.text = bean.getDescription();
		this.height = bean.getHeight();
		this.width = bean.getWidth();
		this.length = bean.getLength();
	}

	/**
	 * Returns the bean ID, which in this case is the same as the room ID.
	 */
	public String getBeanID() {
		return getID();
	}
}
