package ring.movement;

import ring.world.*;
import java.util.HashMap;

public class ZoneCoordinate implements Comparable {
    //Instance variables
    private int x, y, z, zindex;

    //The global location HashMap: This stores the location of all rooms in the game. However, when moving from room to room,
    //the room's local HashMap is checked before checking this. If no exits are found in that Room, the global HashMap is used.
    //Exits between rooms are IMPLICIT in this list rather than EXPLICIT like in the room's local exits.
    //That is, if a room is next to another, they automatically form an exit. This is different than local eixts
    //which must be manually linked together.
    private static HashMap<ZoneCoordinate, Room> roomList;
    
    //Constants
    public static final ZoneCoordinate NORTH = new ZoneCoordinate(0, 1, 0);
    public static final ZoneCoordinate SOUTH = new ZoneCoordinate(0, -1, 0);
    public static final ZoneCoordinate EAST = new ZoneCoordinate(1, 0, 0);
    public static final ZoneCoordinate WEST = new ZoneCoordinate(-1, 0, 0);
    public static final ZoneCoordinate UP = new ZoneCoordinate(0, 0, 1);
    public static final ZoneCoordinate DOWN = new ZoneCoordinate(0, 0, -1);
    public static final ZoneCoordinate ORIGIN = new ZoneCoordinate(0, 0, 0);
    
    public ZoneCoordinate(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }
    
    public ZoneCoordinate(int zindex, int x, int y, int z) {
	this.zindex = zindex;
	this.x = x;
	this.y = y;
	this.z = z;
    }
    
    public ZoneCoordinate offset3D(ZoneCoordinate zc) {
	return new ZoneCoordinate(x + zc.x, y + zc.y, z + zc.z);
    }
    public ZoneCoordinate offset4D(ZoneCoordinate zc) { return offset3D(zc); }
    
    public static ZoneCoordinate getDirection(String dir) {
	if (dir.equalsIgnoreCase("north")) return NORTH;
	if (dir.equalsIgnoreCase("south")) return SOUTH;
	if (dir.equalsIgnoreCase("west")) return WEST;
	if (dir.equalsIgnoreCase("east")) return EAST;
	if (dir.equalsIgnoreCase("up")) return UP;
	if (dir.equalsIgnoreCase("down")) return DOWN;
	return null;
    }
    
    public static String getDirectionString(ZoneCoordinate zc) {
	if (zc.equals(NORTH)) return "north";
	if (zc.equals(SOUTH)) return "south";
	if (zc.equals(WEST)) return "west";
	if (zc.equals(EAST)) return "east";
	if (zc.equals(UP)) return "up";
	if (zc.equals(DOWN)) return "down";
	return "getDirectionString: Coordinate not understood!!";
    }
    
    public static String getOppositeDirectionString(ZoneCoordinate zc) { 
	if (zc.equals(NORTH)) return "the south";
	if (zc.equals(SOUTH)) return "the north";
	if (zc.equals(WEST)) return "the east";
	if (zc.equals(EAST)) return "the west";
	if (zc.equals(UP)) return "below";
	if (zc.equals(DOWN)) return "above";
	return "getOppositeDirectionString: Coordinate not understood!!";
    }
    
    public static ZoneCoordinate getOppositeDirection(ZoneCoordinate zc) {
	return new ZoneCoordinate(-zc.x, -zc.y, -zc.z);
    }

    
    public Room getRoom() { 
	return LocationGrid.getRoomByCoord(this); 
    }
    
    //convience method for calling the setCoordRoom method of World.
    public boolean setRoom(Room r) {
	return LocationGrid.setCoordRoom(this, r);
    }
    
    public String getAllExitsString(int search) { return ""; }
    
    public boolean equals(Object other) {
	if (!(other instanceof ZoneCoordinate)) return false;
	ZoneCoordinate zc = (ZoneCoordinate)other;
	if ((x == zc.x) && (y == zc.y) && (z == zc.z)) return true;
	else return false;
    }
    
    public int hashCode() {
	return (1 + zindex) * x * y * z;
    }

    //isUnitDirection method.
    //This method returns true if the direction is a "unit" direction--that is, X Y and Z are between -1 and 1 (inclusive).
    public boolean isUnitDirection() {
	return (x >= -1 && x <= 1 && y >= -1 && y <= 1 && z >= -1 && z <= 1);
    }
    
    public int compareTo(Object other) {
	if (!(other instanceof ZoneCoordinate)) return -1;
	ZoneCoordinate zc = (ZoneCoordinate)other;
	return ((x - zc.x) + (y - zc.y) + (z - zc.z));
    }
}
