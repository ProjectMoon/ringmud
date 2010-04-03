package ring.movement;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.XMLDBException;

import ring.persistence.Loadpoint;
import ring.persistence.XQuery;
import ring.system.MUDConfig;

/**
 * Builds the world.
 * @author projectmoon
 *
 */
public class WorldBuilder {
	private static Map<String, Room> roomCache = new HashMap<String, Room>();
	
	public static void buildWorld() throws XMLDBException, JAXBException, IOException {
		InputStream xqStream = WorldBuilder.class.getClassLoader().getResourceAsStream("ring/movement/worldbuilder.xq");
		XQuery xq = new XQuery(xqStream);
		xq.setLoadpoint(Loadpoint.STATIC);
				
		//Retrieve all locations in the static collection.
		List<Location> locs = xq.execute(Location.class);
		
		//Add all the rooms to the cache beforehand.
		//We must do this beforehand to get all room references.
		//Otherwise, we encounter exceptions.
		for (Location loc : locs) {
			Room room = loc.getRoom();
			attemptAddToCache(room);
			System.out.println("Found room: " + room);
		}
		
		//Now construct the world.
		for (Location loc: locs) {
			Room room = loc.getRoom();
			for (Portal port : loc.getExits()) {
				port.setDestination(getRoom(port.getDestinationID()));
				LocationManager.addToGrid(room, port);
			}
		}
	}
	
	private static void attemptAddToCache(Room room) {
		if (!roomCache.containsValue(room)) {
			roomCache.put(room.getID(), room);
		}
	}
	
	private static Room getRoom(String id) {
		Room room = roomCache.get(id);		
		return room;
	}
	
	public static void main(String[] args) throws Exception {
		MUDConfig.loadProperties();
		WorldBuilder.buildWorld();
	}
}
