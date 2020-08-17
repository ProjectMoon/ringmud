package ring.movement;

import ring.worldbuilder.WorldBuilderKt;
import ring.worldbuilder.ZoneModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds the world.
 * @author projectmoon
 *
 */
public class WorldBuilder {
	private static Map<String, Room> roomCache = new HashMap<String, Room>();
	
	public static void buildWorld()  {
		//Use new world building system to construct the world, but convert to
		//objects we understand.
		ZoneModel zone = WorldBuilderKt.buildTestWorld();
		List<Location> locs = WorldBuilderKt.convertToLegacyWorld(zone);

		//Add all the rooms to the cache beforehand.
		//We must do this beforehand to get all room references.
		//Otherwise, we encounter exceptions.
		for (Location loc : locs) {
			Room room = loc.getRoom();
			attemptAddToCache(room);
		}
		
		//Now construct the world.
		for (Location loc: locs) {
			Room room = loc.getRoom();
			for (Portal port : loc.getExits()) {
				port.setDestination(getRoom(port.getDestinationID()));
				LocationManager.addToGrid(room, port);
			}
		}

		System.err.println("[FIXME] Reimplement buildWorld");
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
}
