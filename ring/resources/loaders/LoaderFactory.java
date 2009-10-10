package ring.resources.loaders;

import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.resources.providers.RoomProvider;

/**
 * Factory class for getting resource loaders.
 * @author projectmoon
 *
 */
public class LoaderFactory {
	public static ResourceLoader<Room> getRoomLoader() {
		RoomLoader r = new RoomLoader();
		r.setProvider(new RoomProvider());
		return r;
	}
	
	public static ResourceLoader<Room> getRoomLoader(RoomProvider provider) {
		RoomLoader r = new RoomLoader();
		r.setProvider(provider);
		return r;
	}
	
	public static ResourceLoader<Mobile> getMobileLoader() {
		return new MobileLoader();
	}
}
