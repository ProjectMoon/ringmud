package ring.movement;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;
import ring.entities.*;
import ring.mobiles.*;
import ring.world.*;
import ring.util.*;

/*
TODO:
-change changeLocation code to use MOVE_ constants instead of current Room[] exit list.
-get rid of Room[] exit list.
-change getRoomByDirection to use MOVE_ constants
-change hiddensearchDCs to use MOVE_ constants
-remove regular direction constants
-
*/

public class Room {
    /*
      This class describes a room in the MUD world. It is not a WorldObject, because a room in the MUD world is merely an abstraction
      of a space. A "room" could be an actual room or it could be part an open field. It could be a cave. It could be under
      the ocean. I'm sure you get the point.
      Movement in the MUD works as follows: Every Room stores a local HashMap of possible exits, and then there is
      the global HashMap stored in class World. This is the result of developing an easy-to-program movement system.
      The room's HashMap is first checked for possible exits, including Room.NO_EXIT. If Room.NO_EXIT is found, the
      system returns an error that does not allow the player to move in that direction. If a valid exit is found, it
      moves the player to that Room. If null is found, it moves on to the global HashMap, and the rules are the same.
      
      There are several methods spread around throughout the MUD that deal with movement, but all movement commands
      are to be called from this class. In fact, the various helper methods will not allow themselves to be called
      without a proper verification hash that is dynamically generated at runtime.
    */
    
    //internal/system info:
    //parentZone is the Zone that holds the Room. This is in place to make sure there are no
    //duplicates in other Zones.
    //private Zone parentZone; commented out for now .... not sure if it's necessary
    
    //Local set of exits: This is checked before the global set of locations. This allows us to seal off certain exits,
    //create portals, and other stuff.
    private HashMap<ZoneCoordinate, Room> localExits = new HashMap<ZoneCoordinate, Room>();

    //Room.NO_EXIT constant: This tells us that there is no exit in a given direction.
    public static final Room NO_EXIT = new Room();
    private static final String NO_EXITS_STRING = "[B][RED]NO EXITS![R][WHITE]";
    
    //Room data.
    //Mobiles present in the Room.
    private Vector mobiles;
    //Entities present in the Room.
    private Vector entities;
    //Title of the Room.
    private String title;
    //Detailed description of the Room.
    private String text;
    //The size of this room in FEET. These ones are default and should be removed after ALPHA is complete.
    private int length = 1;
    private int width = 1;
    private int height = 1;
    //the search check required to find this room from another room.
    private int searchDC;
    //no exit room? only set by Room() constructor.
    //this means that the "room" is really just not an exit from the room the person is coming from
    //not that there is no exit from THIS room.
    private boolean noExit = false;
    //a string that tells us all the exits in this room. only updated when the exits to a room
    //are changed, which isn't usually.
    private String cachedExitsString;
    
    //***********
    //BEGIN CONSTRUCTORS
    //There are FIVE (5) different constructors that can be called. They should cover all
    //possible cases of a room. The constructors are usually only called when the server is booting up,
    //reading the zone files, and building the world.
    
    //DEFAULT ROOM CONSTRUCTOR.
    //The default room constructor creates the special Room.NO_EXIT room. This is used when checking movement in order to determine that there isnt' a room
    //to move to!
    public Room() {
	title = "NO EXIT";
	text = "NO EXIT";
	noExit = true;
    }
    
    //EMPTY ROOM CONSTRUCTOR.
    //Room constructor to construct an empty room with a non-default title and text.
    //Exits are not set from this method.
    public Room(String title, String text) {
	this.title = title;
	this.text = text;
	mobiles = new Vector();
	entities = new Vector();
	searchDC = 0;
    }
    
    //MOBILES BUT NO ENTITIES CONSTRUCTOR.
    //This constructor is called by the Zone Builder if MOBILES BUT NO ENTITIES are put in
    //the Room by default.
    public Room(String title, String text, Mobile[] mobileList) {
	this.title = title; this.text = text;
	mobiles = new Vector(); 
	entities = new Vector();
	
	//loop through mobileList and add them all to the mobiles vector.
	for (int c = 0; c < mobileList.length; c++)
	    mobiles.addElement(mobileList[c]);
    }
    
    //ENTITIES BUT NO MOBILES CONSTRUCTOR.
    //This constructor is called by the Zone Builder if ITEMS BUT NO MOBILES are put in the Room
    //by default.
    public Room(String title, String text, Entity[] entList) {
	this.title = title;
	this.text = text;
	mobiles = new Vector();
	entities = new Vector();
	
	//loop through mobileList and add them all to the items vector.
	for (int c = 0; c < entList.length; c++)
	    entities.addElement(entList[c]);
    }
    
    //ENTITIES AND MOBILES CONSTRUCTOR.
    //This constructor is called by the Zone Builder if there are ITEMS AND MOBILES put in the
    //Room by default.
    public Room(String title, String text, Mobile[] mobileList, Entity[] entList) {
	this.title = title;
	this.text = text;
	mobiles = new Vector();
	entities = new Vector();
	
	//loop through mobileList and add them all to the mobiles vector.
	for (int c = 0; c < mobileList.length; c++)
	    mobiles.addElement(mobileList[c]);
	
	//loop through itemList and add them all to the item vector.
	for (int c = 0; c < entList.length; c++)
	    entities.addElement(entList[c]);
    }
    
    //****************
    //END CONSTRUCTOR SECTION OF CODE
    //#########################################################

    //MOVENT-RELATED CODE:
    //this code deals with checking the local hashmap for exits, sealing exits, etc.
    public Room getLocalExit(ZoneCoordinate zc) {
	return localExits.get(zc);
    }

    //addLocalExit method.
    //this method adds a local exit. An exit must be in a unit direction. Returns true if successful, and false if not.
    public boolean addLocalExit(ZoneCoordinate zc, Room exit) {
	//first check if zc is a unit direction
	if (!zc.isUnitDirection())
	    return false; 

	//now we can add the exit.
	localExits.put(zc, exit);
        addExitString(ZoneCoordinate.getDirectionString(zc));
        return false;
    }

    //sealAllExits method.
    //this method seals off all exits to a room, that is, places Room.NO_EXIT in all of the unit directions.
    public void sealAllExits() {
	localExits.put(ZoneCoordinate.NORTH, Room.NO_EXIT);
	localExits.put(ZoneCoordinate.SOUTH, Room.NO_EXIT);
	localExits.put(ZoneCoordinate.EAST, Room.NO_EXIT);
	localExits.put(ZoneCoordinate.WEST, Room.NO_EXIT);
	localExits.put(ZoneCoordinate.UP, Room.NO_EXIT);
	localExits.put(ZoneCoordinate.UP, Room.NO_EXIT);
        cachedExitsString = NO_EXITS_STRING;
    }

    //sealExit method.
    //this method seals off a given exit. that is, it places Room.NO_EXIT at the specified local zone coordinate.
    public void sealExit(ZoneCoordinate exit){ 
	localExits.put(exit, Room.NO_EXIT);
        removeExitString(ZoneCoordinate.getDirectionString(exit));
    }
    
    //makeImplictExit method.
    //This method makes an exit implicit by removing its local exit.
    public boolean makeImplictExit(ZoneCoordinate direction) {
        Room r = localExits.remove(direction);
        addExitString(ZoneCoordinate.getDirectionString(direction));
        if (r != null) return true;
        else return false;
    }
    
    //addExitString method.
    //adds an exit to the exit string.
    private void addExitString(String s) {
        //if there are no exits, just set it directly
        if (cachedExitsString.equals(NO_EXITS_STRING)) {
            cachedExitsString = s;
            return;
        }
        
        //otherwise, proceed as normal
        //only add if it's not already there.
        if (cachedExitsString.indexOf(s) == -1)
            cachedExitsString += " " + s;
    }
    
    //removeExitString method.
    //removes an exit string from the exit string list.
    private void removeExitString(String s) {
        cachedExitsString.replace(s, "");
        if (cachedExitsString.length() == 0)
            cachedExitsString = NO_EXITS_STRING;
    }
    
    //getExitsString method.
    //gets the exits string.
    public String getExitsString() {
        return "[B][CYAN]Exits: [B]" + cachedExitsString + "[R][WHITE]";
    }
    
    //getExitsString method.
    //gets the exits string, taking into account a search check.
    public String getExitsString(int searchCheck) {
        return "[R][CYAN]Exits: [B]" + cachedExitsString;
    }    
    
    
    //addEntity method. This method adds an entity such as an item to the list.
    //If the entity list is empty, it is created.
    public void addEntity(Entity ent) {
	if (entities == null) entities = new Vector();
	entities.addElement(ent);
    }
    
    //addMobile method. This adds a mobile (whether it be an NPC or PC) to the room. It should
    //be called by changeLocation automatically when a player or NPC moves to another room.
    public void addMobile(Mobile mobile) {
	if (mobiles == null) {mobiles = new Vector();}
	mobiles.addElement(mobile);
    }
    
    //removeEntity method. This method removes an entity from the room. This should be called
    //when the item is picked up, destroyed, etc.
    public void removeEntity(Entity ent) {
	System.out.println("REMOVING ENTITY: " + ent);
	entities.removeElement(ent);
	if (entities.isEmpty()) entities = null;
    }
    
    //removeMobile method. This method is called when a mobile leaves a room. It should be
    //called by changeLocation when the mobile is leaving the room.
    public void removeMobile(Mobile mobile) {
	mobiles.removeElement(mobile);
	if (mobiles.isEmpty()) mobiles = null;
    }
    
    //getMobileList method. (regular version)
    //This method returns a string of all the current mobiles in the room, excluding the
    //specified mobile.
    public String getMobileList(Mobile excludedMobile) {
	String res = "";
	if (mobiles == null) return res;
	
	for (int c = 0; c < mobiles.size(); c++) {
	    Mobile mob = (Mobile)mobiles.get(c);
	    if (!mob.equals(excludedMobile))
		res += mob.getShortDescription() + "[R][WHITE] is here.\n";
	}
	
	return res;
    }
    
    //getMobileList method. (spot check version)
    //This method returns a string of all the current mobiles in the room, excluding the
    //specified mobile. It also takes a spot check number to deal with hiding mobiles.
    public String getMobileList(Mobile excludedMobile, int spotCheck) {
	String res = "";
	if (mobiles == null) return res;
	
	for (int c = 0; c < mobiles.size(); c++) {
	    Mobile mob = (Mobile)mobiles.get(c);
	    if (!mob.equals(excludedMobile)) {
		if (spotCheck >= mob.hideCheck) {
		    if (mob.hideCheck > 0) res += mob.getShortDescription() + "[R][WHITE] is here. ([B][YELLOW]hiding[R][WHITE])\n";
		    else res += mob.getShortDescription() + "[R][WHITE] is here.\n";
		}
		
	    }
	}
	
	return res;
    }
    
    //getEntityList method.
    //This method returns a string of all of the current entities in the room.
    public String getEntityList() {
	String res = "";
	
	if (entities == null) return res;
	
	for (int c = 0; c < entities.size(); c++) {
	    Entity ent = (Entity)entities.get(c);
	    if (c != entities.size() - 1)
		res += "[R][WHITE]" + ent	.getIndefiniteDescriptor() + " " + ent.getName() + " " + ent.getIdleDescriptor() + "[R][WHITE].\n";
	    else
		res += "[R][WHITE]" + ent	.getIndefiniteDescriptor() + " " + ent.getName() + " " + ent.getIdleDescriptor() + "[R][WHITE].";  
	}
	
	return res;
    }

    //isNoExit method.
    //Checks if this room is a "no exit" room. This means that the room really isn't a room, but a placeholder that tells us there is no exit in the given direction.
    public boolean isNoExit() {
	return noExit;
    }
    
    /*
      MOVEMENT CODE v3
      Keys, reorganize heirarchy, sealExit, openExit
    */
    
    /*
    //getExitsString method.
    //Returns all available NON-HIDDEN exits as a string.
    public String getVisibleExitsString() {
    String res = "[CYAN]Exits: ";
    if ((exits[NORTH] != null) && (searchDCs[NORTH] <= 0)) res += "north ";
    if ((exits[SOUTH] != null) && (searchDCs[SOUTH] <= 0)) res += "south ";
    if ((exits[WEST] != null) && (searchDCs[WEST] <= 0)) res += "west ";
    if ((exits[EAST] != null) && (searchDCs[EAST] <= 0)) res += "east ";
    if ((exits[UP] != null) && (searchDCs[UP] <= 0)) res += "up ";
    if ((exits[DOWN] != null) && (searchDCs[DOWN] <= 0)) res += "down ";
    
    if ((exits[NORTH] == null) && (exits[SOUTH] == null) && (exits[WEST] == null)
    && (exits[EAST] == null) && (exits[UP] == null) && (exits[DOWN] == null)) {
    res += "[B][RED]None![R][WHITE]";
    }
    
    return res;
    }
    
    //getHiddenExitsString method.
    //Returns a listing of available HIDDEN exits based on a check passed.
    public String getHiddenExitsString(int check) {
    String res = "";
    if ((exits[NORTH] != null) && (check >= searchDCs[NORTH]) && (searchDCs[NORTH] > 0)) {
    res += "north ";
    }
    
    if ((exits[SOUTH] != null) && (check >= searchDCs[SOUTH]) && (searchDCs[SOUTH] > 0)) {
    res += "south ";
    }
    
    if ((exits[EAST] != null) && (check >= searchDCs[EAST]) && (searchDCs[EAST] > 0)) {
    res += "east ";
    }
    
    if ((exits[WEST] != null) && (check >= searchDCs[WEST]) && (searchDCs[WEST] > 0)) {
    res += "west ";
    }
    
    if ((exits[UP] != null) && (check >= searchDCs[UP]) && (searchDCs[UP] > 0)) {
    res += "up ";
    }
    
    if ((exits[DOWN] != null) && (check >= searchDCs[DOWN]) && (searchDCs[DOWN] > 0)) {
    res += "down ";
    }
    
    //if nothing was found...
    if (res.equals("")) res = "None.";
    
    return res;
    }
    
    //getExitsString method.
    //Returns all available, including HIDDEN, exits as a string based on a search check DC.
    public String getAllExitsString(int check) {
    String res = "[CYAN]Exits: ";
    if ((check >= searchDCs[NORTH]) && (exits[NORTH] != null)) {
    res += "north ";
    if (searchDCs[NORTH] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }

    if ((check >= searchDCs[SOUTH]) && (exits[SOUTH] != null)) {
    res += "south ";
    if (searchDCs[SOUTH] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }
    
    if ((check >= searchDCs[EAST]) && (exits[EAST] != null)) {
    res += "east ";
    if (searchDCs[EAST] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }
    
    if ((check >= searchDCs[WEST]) && (exits[WEST] != null)) {
    res += "west ";
    if (searchDCs[WEST] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }
    
    if ((check >= searchDCs[UP]) && (exits[UP] != null)) {
    res += "up ";
    if (searchDCs[UP] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }
    
    if ((check >= searchDCs[DOWN]) && (exits[DOWN] != null)) {
    res += "down ";
    if (searchDCs[DOWN] > 0) res += "([B][YELLOW]hidden![R][CYAN]) ";
    }
    
    if ((exits[NORTH] == null) && (exits[SOUTH] == null) && (exits[WEST] == null)
    && (exits[EAST] == null) && (exits[UP] == null) && (exits[DOWN] == null)) {
    res = "[B][RED]None![R][WHITE]";
    }
    
    return res;
    }
    
    
    //generateBlindExitsString method.
    //This generates a random set of exits based on a search check. It's used for the search command when
    //the user is blind. It should unveil random NON-HIDDEN exits based on the following criteria:
    //DC 10: 2 random exits.
    //DC 15: 4 random exits.
    //DC 20: 6 random exits.
    //This method assumes the check is at least 10.
    //TODO: This needs to not have the possibility of generating an exit twice.
    public String generateBlindExitsString(int check) {
    String res = "";
    Random gen = new Random(System.nanoTime());
    int revealExitsNum = 0;
    int exitsRevealed = 0;
    boolean[] exitsMarked = new boolean[MAX_EXITS];
    
    if (check >= 10) revealExitsNum = 2;
    if (check >= 15) revealExitsNum = 4;
    if (check >= 20) revealExitsNum = 6;
    
    for (int c = 0; c < 6; c++) {
    if (exitsRevealed == revealExitsNum) break;
    for (int x = 0; x < 6; x++) {
    int ranDirection = gen.nextInt(MAX_EXITS);
    if ((exitsMarked[ranDirection] == false) && (exits[ranDirection] != null) && (searchDCs[ranDirection] <= 0)) {
    exitsMarked[ranDirection] = true;
    break;
    }
    }
    
    exitsRevealed++;
    }
    
    for (int v = 0; v < MAX_EXITS; v++) {
    if (exitsMarked[v]) res += getDirectionString(v) + " ";
    }
    
    if (res.equals("")) res = "None.";
    
    return res;
    }
    */
    
    //generateBlindRandomOccupantList method.
    //This generates a random list of Mobiles or Entities in the room for a blind mobile based on a search
    //check DC as per the following guidelines:
    //DC < 25: no information revealed.
    //Each point above 25 reveals one mobile or entity.
    public String generateBlindRandomOccupantList(int check) {
	if (check <= 25) return "None.";
	
	int numToReveal = check - 25;
	Vector mobs = getMobiles();
	Vector ents = getEntities();
	Random gen = new Random(System.nanoTime());
	String res = "";
	int creatures = 0;
	int objects = 0;
		
	for (int c = 0; c < numToReveal; c++) {
	    if (c % 2 == 0) { //reveal a mobile
		int i = gen.nextInt(mobs.size());
		if (mobs.size() > 0) {
		    Mobile mob = (Mobile)mobs.get(i);
		    if(mob != null) creatures++;
		}
		
	    }
	    
	    else { //reveal an entity
		int i = gen.nextInt(mobs.size());
		if (ents.size() > 0) {
		    Entity ent = (Entity)ents.get(i);
		    if (ent != null) objects++;
		}				
	    }
	}
	
	if ((creatures == 0) && (objects == 0)) return "None.";
	
	res = creatures + " creatures and " + objects + " objects.";
	
	return res;	
    }
    
    
    //getSearchDC method.
    //Gets the Search DC of this room
    public int getSearchDC() {
	return searchDC;
    }
    
    //setSearchDC method.
    //Sets the Search DC of this room
    public void setSearchDC(int dc) {
	searchDC = dc;
    }
    
    //getMobiles method.
    //Returns the list of mobiles.
    public Vector getMobiles() {
	return mobiles;
    }
    
    //getEntities method.
    //Returns the list of entities.
    public Vector getEntities() {
	return entities;
    }
    
    //getTitle method.
    //Returns the title of this room.
    public String getTitle() {
	return title + "[R][WHITE] (" + length + " ft long, " + width + " ft wide, " + height + " ft tall)";
    }
    
    //getDescription method.
    //Returns the description of this room.
    public String getDescription() {
	return text;
    }
    
    //set the three dimensional size of this room.
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
}
