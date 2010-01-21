package ring.resources.loaders;

import ring.entities.Item;
import ring.movement.Room;
import ring.resources.providers.ItemProvider;
import ring.resources.providers.RoomProvider;
import ring.system.MUDConfig;

/**
 * Factory class for getting resource loaders.
 * @author projectmoon
 *
 */
public class LoaderFactory {
	//The singleton instances
	private static final RoomLoader roomLoader = new RoomLoader();
	private static final ItemLoader itemLoader = new ItemLoader();

	/**
	 * Initializes the resource loaders by reflectively instantiating them from
	 * mud.config.
	 */
	@SuppressWarnings("unchecked")
	public static void initLoaders() {
		System.out.println("Initializing resource loaders...");
		//Get all class names
		String roomProviderClassName = MUDConfig.getRoomProvider();
		String itemProviderClassName = MUDConfig.getItemProvider();
		
		//Reflectively instantiate each one and set the providers.
		try {
			//Room provider
			Class<RoomProvider> roomProviderClass = (Class<RoomProvider>) Class.forName(roomProviderClassName);
			RoomProvider roomProvider = roomProviderClass.newInstance();
			roomLoader.setProvider(roomProvider);
			
			//Item provider
			Class<ItemProvider> itemProviderClass = (Class<ItemProvider>) Class.forName(itemProviderClassName);
			ItemProvider itemProvider = itemProviderClass.newInstance();
			itemLoader.setProvider(itemProvider);
			
			//TODO other providers...
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void loadAllBeans() {
		System.out.println("Loading RingBeans...");
		itemLoader.loadBeans();
		roomLoader.loadBeans();
	}
	
	public static ResourceLoader<Room> getRoomLoader() {
		return roomLoader;
	}
	
	public static ResourceLoader<Item> getItemLoader() {
		return itemLoader;
	}
}
