package ring.resources.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class represents a set of RoomBeans being loaded in by the JOX parser. It stores
 * all the information usable by the world-building system. In the actual game objects,
 * a Room does not directly know about its exists. Thus, it must query the LocationManager.
 * @author projectmoon
 *
 */
public class RoomBeanSet implements Serializable {
	public static final long serialVersionUID = 1;
	
	private static Logger log = Logger.getLogger(RoomBeanSet.class.getName());
	private HashMap<String, RoomBean> roomSet;
	private String name;
	
	public RoomBeanSet() {
		roomSet = new HashMap<String, RoomBean>();
	}
	
	public void copyFrom(RoomBeanSet otherSet) {
		roomSet.putAll(otherSet.roomSet);
	}
	
	public RoomBean[] getRoom() {
		return roomSet.values().toArray(new RoomBean[0]);
	}
	
	public void setRoom(RoomBean[] rooms) {
		for (RoomBean room : rooms) {
			roomSet.put(room.getID(), room);
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
