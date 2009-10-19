package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.entities.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.world.World;
import ring.world.WorldObject;

public class Get implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.FROM_ROOM);
		Object target = params.getParameter(0);

		CommandResult res = new CommandResult();
		res.setFailText("[R][GREEN]You can't get that.[WHITE]");

		if (target == null) {
			res.setFailText("[R][WHITE]You don't see that here.");
			return res;
		}

		Mobile getter = (Mobile) sender;
		WorldObject thing = (WorldObject) target;

		// Check if thing is gettable.
		if (!thing.isItem())
			return res;

		// If yes, remove thing from room and add to getter's inventory.
		Item i = (Item) thing;
		getter.addItemToInventory((Item) thing);

		// Remove the item from the room.
		Room room = (Room)getter.getLocation();
		room.removeEntity((Item) thing);

		res.setText("You get " + i.getIndefiniteDescriptor().toLowerCase()
				+ " " + i.getName() + "[R][WHITE].");

		// Notify other people in room
		World.sendVisualToLocation(getter, getter.getName() + " picks up "
				+ i.getIndefiniteDescriptor().toLowerCase() + " " + i.getName()
				+ "[R][WHITE].", null);

		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "get";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
