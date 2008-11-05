/*
 * This file is licensed under the CDDL license.
 * Refer to the associated legal documentation.
 */

package ring.movement;
import java.util.HashMap;

/**
 * This contains the global location grid, as well as methods for adding to and retrieving from
 * the location grid. It connects rooms in a global, IMPLICIT fashion. Local room exits (which are EXPLICIT)
 * are checked before using this class. IMPLICT means that rooms form links between themselves automatically
 * instead of having to be told to do so.
 * @author jeff
 */
public final class LocationGrid {
    private LocationGrid() {} //no instantiation of this class.
    private static HashMap<ZoneCoordinate, Room> roomList = new HashMap<ZoneCoordinate, Room>();
    
    //getRoomByCoord method.
    //This method gets a Room by its ZoneCoordinate.
    public static synchronized Room getRoomByCoord(ZoneCoordinate zc) {
	System.out.println ("in get room by coord");
	System.out.println ("*** returning: " + roomList.get(zc));
	return roomList.get(zc);
    }    
    
    //setCoordRoom method.
    //This method ties a Room to a ZoneCoordinate. It returns true if a new entry was made,
    //and false if an entry was replaced.
    public static synchronized boolean setCoordRoom(ZoneCoordinate zc, Room r) {
	Object o = roomList.put(zc, r);
	if (o == null) return true;
	else return false;
    }
}
