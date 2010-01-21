package ring.commands;

import java.util.Vector;

import ring.entities.Entity;
import ring.mobiles.Mobile;
import ring.mobiles.backbone.Inventory;
import ring.movement.Location;
import ring.movement.Room;
import ring.world.WorldObject;

/**
 * <p>
 * Title: RingMUD Codebase
 * </p>
 * 
 * <p>
 * Description: RingMUD is a java codebase for a MUD with a working similar to
 * DikuMUD
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Company: RaiSoft/Thermetics
 * </p>
 * 
 * @author Jeff Hair
 * @version 1.0
 */
public final class CommandParameters {	
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

	private Object[] parameters;
	private String[] initParameters;
	private CommandSender sender;
	private CommandType cmdType;
	
	public CommandParameters(String[] params, CommandSender sender) {
		this.sender = sender;
		initParameters = params;
	}

	public void init(CommandType cmdType) {
		this.cmdType = cmdType;
		parameters = getParameters(initParameters, cmdType);
	}

	public CommandType getType() {
		return cmdType;
	}

	// length method.
	// Using initParameters guarantees that this will return, well.. usually.
	public int length() {
		return initParameters.length;
	}

	// getParameter method.
	// Returns a parameter.
	public Object getParameter(int index) {
		if (parameters == null)
			return null;
		if (index >= parameters.length)
			return null;
		return parameters[index];
	}

	public String getParameterAsText(int index) {
		if (parameters == null)
			return null;
		else
			return initParameters[index];
	}

	// lastParameter method.
	// Returns the last parameter in the parameters array.
	public Object lastParameter() {
		return parameters[parameters.length - 1];
	}

	// constructSpellName method.
	// This simply returns up to parameters.length - 1 in a combined string.
	public String constructSpellName() throws ClassCastException {
		String text = "";
		for (int c = 0; c < parameters.length - 1; c++) {
			System.out.println("examining: " + parameters[c]);
			if (!(parameters[c] instanceof String))
				throw (new ClassCastException("Object must be string!"));
			else
				text += (String) parameters[c] + " ";
		}

		return text.trim();
	}

	public String paramString() {
		if (parameters == null)
			return null;
		String res = "";
		for (int c = 0; c < parameters.length; c++) {
			if (parameters[c] == null)
				res += "[null]";
			else
				res += parameters[c].toString();
			res += " ";
		}

		res = res.trim();
		return res;
	}

	// getParameters method.
	// This method gets all of the parameters needed for the method to be
	// executed properly.
	// All nulls and such are handled properly as well.
	private Object[] getParameters(String[] params, CommandType cmdType) {
		WorldObject o;

		// Handle a command that has no parameters.
		if ((params == null) || (params.length == 0) || (params[0] == null))
			return null;

		// Do things according to the type of command.
		// is is a "say"-type command?
		if (cmdType.equals(CommandType.TEXT))
			return params;

		// well it must require some sort of object, so let's declare an
		// Object[].
		Object[] parameters = new Object[params.length];

		// Is it a command that requires the last object to be a WorldObject
		// target? i.e. a cast command
		if (cmdType.equals(CommandType.SPELL)) {
			for (int c = 0; c < params.length - 1; c++) {
				parameters[c] = params[c];
			}

			String name = params[params.length - 1].toString();
			parameters[params.length - 1] = getWorldObjectFromRoomByName(name);
		}// END OF SPELL COMMANDS

		// Is it a command that looks for parameters in the inventory?
		if (cmdType.equals(CommandType.INVENTORY)) {
			for (int x = 0; x < params.length; x++) {
				// First, let's check to see if the parameter is something in
				// the room. This is the most common
				// Parameter.
				o = getWorldObjectFromInventoryByName(params[x]);
				if (o != null) {
					parameters[x] = o;
				}

				// it must be something else. For now, return null.
				else {
					parameters[x] = null;
				}
			}
		} // END OF INVENTORY COMMAND PARAMETERS!!!!

		// Is it a command that looks for parameters in the equipment?
		if (cmdType.equals(CommandType.EQUIPMENT)) {
			for (int x = 0; x < params.length; x++) {
				// First, let's check to see if the parameter is something in
				// the room. This is the most common
				// Parameter.
				o = getWorldObjectFromEquipmentByName(params[x]);
				if (o != null) {
					parameters[x] = o;
				}

				// it must be something else. For now, pass it along.
				else {
					parameters[x] = params[x];
				}
			}
		} // END OF EQUIPMENT COMMAND PARAMETERS!!!!!!

		// It must be a command that looks for parameters in a room...
		if (cmdType.equals(CommandType.FROM_ROOM)) {
			for (int x = 0; x < parameters.length; x++) {
				// First, let's check to see if the parameter is something in
				// the room. This is the most common
				// Parameter.
				o = getWorldObjectFromRoomByName(params[x]);
				if (o != null) {
					parameters[x] = o;
				}

				// it must be something else. For now, pass it along.
				else {
					parameters[x] = params[x];
				}
			}
		} // END OF CMD PARAMETERS

		// Return the parameters....
		return parameters;
	}

	// getWorldObjectFromInventoryByName method.
	// This method returns an Entity in the inventory of the CommandSender via a
	// String name.
	// Will have to be updated later to accomodate things like 1.sword, 2.berry,
	// etc.
	private WorldObject getWorldObjectFromInventoryByName(String name) {
		Mobile mob;
		
		try {
			mob = (Mobile) sender;
		} catch (NullPointerException e) {
			System.err.println("WARNING: NULL SENDER SOURCE");
			mob = null;
		}

		Inventory inventory = mob.getInventory();
		return inventory.getItemByName(name);
	}

	// getWorldObjectFromEquipment method.
	// This method returns a WorldObject from the Mobile's equipment.
	private WorldObject getWorldObjectFromEquipmentByName(String name) {
		Mobile mob;
		
		try {
			mob = (Mobile) sender;
		} catch (NullPointerException e) {
			System.err.println("WARNING: NULL SENDER SOURCE");
			mob = null;
		}

		return mob.getEquipment().getItemByName(name);
	}

	// getWorldObjectByName method.
	// This method returns a WorldObject in the room of the CommandSender via a
	// String name.
	// Will have to be updated later to accomodate things like
	// "1.monster, 2.sword" etc.
	private WorldObject getWorldObjectFromRoomByName(String name) {
		if (name.equals("a"))
			return null;
		Mobile o;
		name = name.toLowerCase();
		try {
			o = (Mobile) sender;
		} catch (NullPointerException e) {
			System.out.println(e);
			e.printStackTrace();
			// System.out.println("WARNING: NULL SENDER SOURCE");
			o = null;
		}

		Location location = o.getLocation();
		Room room = (Room)location;
		// First loop through mobiles to see if the thing we're looking for is a
		// mobile...
		try {
			Vector<Mobile> mobiles = room.getMobiles();
			if (mobiles.size() > 0) {
				for (int c = 0; c < mobiles.size(); c++) {
					Mobile mob = (Mobile) mobiles.get(c);
					if ((mob.getShortDescription().toLowerCase()).indexOf(name) != -1) {
						return mob;
					}
				}
			}

			// next try the entities...
			Vector<Entity> entities = room.getEntities();
			if (entities.size() > 0) {
				for (int c = 0; c < entities.size(); c++) {
					Entity ent = (Entity) entities.get(c);
					if ((ent.getName().toLowerCase().indexOf(name) != -1)) {
						return ent;
					}
				}
			}
		}
		// Catch a null pointer... This tells us it isn't anything... just a
		// sort of thing to return.
		catch (NullPointerException e) {
			System.err.println("COULDN'T FIND!");
			return null;
		}

		// No WorldObject found to return; therefore it must be a string that
		// needs to be passed...
		return null;
	}

	public String[] getParameterArray() {
		return initParameters;
	}

}
