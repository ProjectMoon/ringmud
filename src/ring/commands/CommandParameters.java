package ring.commands;

import java.util.Collection;
import java.util.List;

import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.backbone.Inventory;
import ring.movement.Room;
import ring.world.WorldObject;

/**
 * This class is responsible for transforming parameters received into objects for the
 * MUD to use. In effect, it is the bridge between what the user types in at the terminal
 * and the internals of the object-oriented MUD system.
 * @author projectmoon
 *
 */
public final class CommandParameters {
	//TODO rework logic for most of this. It has the following problems:
	//1. Will not work for any CommandSender except Mobiles. (BIG limitation)
	//2. Ghetto transformation logic. i.e.: since everything is transformed
	//		individually, the results may not be what is desired.
	
	/**
	 * An enum representing different types of Command parameters. The type tells
	 * the CommandParameters objects how to translate the strings it has into
	 * actual MUD objects.
	 */
	public enum CommandType {
		TEXT,
		SPELL,
		INVENTORY,
		EQUIPMENT,
		FROM_ROOM
	}

	private Object[] objParameters;
	private String[] initParameters;
	private CommandSender sender;
	private CommandType cmdType;
	
	/**
	 * Creates a new CommandParameters object for the specified parameter string
	 * and {@link CommandSender}.
	 * @param params
	 * @param sender
	 */
	public CommandParameters(String[] params, CommandSender sender) {
		this.sender = sender;
		initParameters = params;
	}

	/**
	 * Initializes the command parameters to support a particular {@link CommandType}.
	 * Different command types are initialized in different ways. For example, a text
	 * command applies no transformations to its parameters. However, a command that
	 * operates on a Room (i.e. CommandType.FROM_ROOM) would transform its parameters
	 * into {@link WorldObject}s.
	 * @param cmdType
	 */
	public void init(CommandType cmdType) {
		this.cmdType = cmdType;
		objParameters = getParameters(initParameters, cmdType);
	}

	/**
	 * Gets the {@link CommandType} that these parameters were
	 * initialized to support.
	 * @return
	 */
	public CommandType getType() {
		return cmdType;
	}

	/**
	 * Returns the amount of parameters in this object.
	 * @return The number of parameters.
	 */
	public int length() {
		return initParameters.length;
	}
	
	public String getTextParameters(int fromIndex) {
		String res = "";
		
		for (int c = fromIndex; c < length(); c++) {
			res += initParameters[c] + " ";
		}
		
		return res.trim();
	}

	/**
	 * Gets the object parameter at the specified index.
	 * @param index
	 * @return The parameter as an object, or null if there are no parameters.
	 */
	public Object getParameter(int index) {
		if (objParameters == null)
			return null;
		
		if (index >= objParameters.length)
			return null;
		
		return objParameters[index];
	}

	/**
	 * Gets the text parameter at the specified index.
	 * @param index
	 * @return The parameter as a String, or null if there are no parameters.
	 */
	public String getParameterAsText(int index) {
		if (objParameters == null)
			return null;
		else
			return initParameters[index];
	}

	/**
	 * Convenience method for returning the last parameter in object form.
	 * @return
	 */
	public Object lastParameter() {
		return objParameters[objParameters.length - 1];
	}

	/**
	 * Convenience method for creating spell names.
	 * @return
	 * @throws ClassCastException
	 */
	public String constructSpellName() throws ClassCastException {
		String text = "";
		for (int c = 0; c < objParameters.length - 1; c++) {
			System.out.println("examining: " + objParameters[c]);
			if (!(objParameters[c] instanceof String))
				throw (new ClassCastException("Object must be string!"));
			else
				text += (String) objParameters[c] + " ";
		}

		return text.trim();
	}

	/**
	 * This gets the parameter string for the command, which everything
	 * that comes after the initial command verb.
	 * @return The parameter string, or null if there are no parameters.
	 */
	public String paramString() {
		if (objParameters == null)
			return null;
		String res = "";
		for (int c = 0; c < objParameters.length; c++) {
			if (objParameters[c] == null)
				res += "[null]";
			else
				res += objParameters[c].toString();
			res += " ";
		}

		res = res.trim();
		return res;
	}

	/**
	 * This method transforms parameters from string names into objects.
	 * @param params
	 * @param cmdType
	 * @return
	 */
	private Object[] getParameters(String[] params, CommandType cmdType) {
		// Handle a command that has no parameters.
		if ((params == null) || (params.length == 0) || (params[0] == null))
			return null;

		switch (cmdType) {
			case TEXT:
				return initializeForText(params);
			case SPELL:
				return initializeForSpell(params);
			case INVENTORY:
				return initializeForInventory(params);
			case EQUIPMENT:
				return initializeForEquipment(params);
			case FROM_ROOM:
				return initializeForFromRoom(params);
			default:
				throw new IllegalArgumentException("Unrecognized CommandType.");
		}
	}
	
	/**
	 * Initializes the parameters for a text command. There are no transformations
	 * done to the parameters.
	 * @param params
	 * @return An Object array with the modified parameters.
	 */
	private Object[] initializeForText(String[] params) {
		return params;
	}
	
	/**
	 * Initializes the parameters for a spell command. Leaves everything up to the
	 * last word as text, but transforms the last word into a target from the room.
	 * @param params
	 * @return An Object array with the modified parameters.
	 */
	private Object[] initializeForSpell(String[] params) {
		Object[] parameters = new Object[params.length];
		
		for (int c = 0; c < params.length - 1; c++) {
			parameters[c] = params[c];
		}

		String name = params[params.length - 1].toString();
		parameters[params.length - 1] = getWorldObjectFromRoomByName(name);
		
		return parameters;
	}

	/**
	 * Initializes the parameters for a command that pulls from the current room.
	 * Every parameter is transformed into a world object from the room.
	 * @param params
	 * @return An Object array with the modified parameters.
	 */
	private Object[] initializeForFromRoom(String[] params) {
		Object[] parameters = new Object[params.length];
		
		for (int x = 0; x < parameters.length; x++) {
			// First, let's check to see if the parameter is something in
			// the room. This is the most common
			// Parameter.
			WorldObject o = getWorldObjectFromRoomByName(params[x]);
			parameters[x] = o;
		}
		
		return parameters;
	}
	
	/**
	 * Initializes the parameters for a command that pulls form the
	 * sender's equipment. Parameters are transformed into world objects
	 * from the sender's equipment.
	 * @param params
	 * @return An Object array with the modified parameters.
	 */
	private Object[] initializeForEquipment(String[] params) {
		Object[] parameters = new Object[params.length];
		
		for (int x = 0; x < params.length; x++) {
			// First, let's check to see if the parameter is something in
			// the room. This is the most common
			// Parameter.
			WorldObject o = getWorldObjectFromEquipmentByName(params[x]);
			if (o != null) {
				parameters[x] = o;
			}

			// it must be something else. For now, pass it along.
			else {
				parameters[x] = params[x];
			}
		}
		
		return parameters;
	}
	
	/**
	 * Initializes the parameters for a command that targets the sender's
	 * inventory. Parameters are transformed into world objects from the sender's
	 * inventory.
	 * @param params
	 * @return An Object array with the modified parameters.
	 */
	private Object[] initializeForInventory(String[] params) {
		Object[] parameters = new Object[params.length];

		for (int x = 0; x < params.length; x++) {
			// First, let's check to see if the parameter is something in
			// the room. This is the most common
			// Parameter.
			WorldObject o = getWorldObjectFromInventoryByName(params[x]);
			if (o != null) {
				parameters[x] = o;
			}

			// it must be something else. For now, return null.
			else {
				parameters[x] = null;
			}
		}
		
		return parameters;
	}

	/**
	 * This method returns the most relevant {@link WorldObject} found in the sender's
	 * inventory.
	 * @param name
	 * @return The most relevant WorldObject, or null if nothing was found.
	 */
	@SuppressWarnings("unchecked")
	private WorldObject getWorldObjectFromInventoryByName(String name) {
		Mobile mob = (Mobile)sender;
		Inventory inventory = mob.getDynamicModel().getInventory();
		return search(name, inventory.getItems());
	}

	/**
	 * This method returns the most relevant WorldObject found in the
	 * sender's equipment.
	 * @param name The name of the item to search for in the equipment.
	 * @return The most relevant WorldObject, or null if nothing was found.
	 */
	@SuppressWarnings("unchecked")
	private WorldObject getWorldObjectFromEquipmentByName(String name) {
		Mobile mob = (Mobile) sender;
		Collection<Item> items = mob.getDynamicModel().getEquipment().getItems();
		return search(name, items);
	}

	/**
	 * This method returns the most relevant {@link WorldObject} found in the sender's
	 * current Room.
	 * @param name The name of the WorldObject to search for in the room.
	 * @return The most relevant WorldObject, or null if nothing was found.
	 */
	@SuppressWarnings("unchecked")
	private WorldObject getWorldObjectFromRoomByName(String name) {
		//Too short to search on.
		if (name.length() < 2) {
			return null;
		}
		
		//
		Mobile senderMob = (Mobile)this.sender;
		Room room = senderMob.getLocation();
		
		//Get all world objects in this room to search against.
		List<Mobile> mobs = room.getMobiles();
		List<Item> items = room.getItems();
		List<Entity> entities = room.getEntities();
		
		return search(name, mobs, items, entities);
	}
	
	/**
	 * This method delegates to {@link ring.commands.WorldObjectSearch} in order
	 * to search collections of world objects from generic data sources. It returns
	 * the most relevant world object found amongst all presented collections. The
	 * text searched for is case-insensitive.
	 * @param name The name to search for.
	 * @param worldObjectLists {@link java.util.Collection}s of {@link WorldObject}s. 
	 * @return The most relevant world object, or null if nothing was found.
	 */
	private WorldObject search(String name, Collection<? extends WorldObject> ... worldObjectLists) {
		WorldObjectSearch search = new WorldObjectSearch();
		
		for (Collection<? extends WorldObject> list : worldObjectLists) {
			search.addSearchList(list);
		}
		
		List<WorldObject> results = search.search(name);
		
		if (results.size() > 0) {
			return results.get(0);
		}
		else {
			return null;
		}
	}

	public String[] getParameterArray() {
		return initParameters;
	}
}
