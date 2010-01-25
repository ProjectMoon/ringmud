package ring.movement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.XMLDBException;

import ring.items.Item;
import ring.mobiles.Mobile;
import ring.persistence.DataStore;
import ring.persistence.DataStoreFactory;
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
	
	public static void buildWorld() throws XMLDBException, JAXBException {
		XQuery xq = new XQuery();
		String query = "for $loc in //location return $loc";
		xq.setQuery(query);
		
		//for each Location:
		//	Add its Room to the cache
		//	for each Portal:
		//		port.setDestination(getRoom(port.getDestionationID());
		//		LocationManager.addToGrid(room, port)
		
		List<Location> locs = xq.query(Location.class);
		for (Location loc : locs) {
			Room room = loc.getRoom();
			attemptAddToCache(room);
			
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
		
		if (room == null) {
			room = DataStoreFactory.getDefaultStore().retrieveRoom(id);
			
			if (room != null) {
				roomCache.put(id, room);
			}
			else {
				throw new WorldConstructionException("Couldn't find room ID \"" + id + "\" in the cache or the database. Aborting.");
			}
		}
		
		return room;
	}
	
	public static void main(String[] args) throws Exception {
		MUDConfig.loadProperties();
		WorldBuilder.buildWorld();
		Mobile r = DataStoreFactory.getDefaultStore().retrieveMobile("mob1");
		System.out.println(r);
	}
}
