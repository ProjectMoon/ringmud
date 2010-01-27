package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;
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
		if (!(thing instanceof Item))
			return res;

		// If yes, remove thing from room and add to getter's inventory.
		Item i = (Item) thing;
		getter.addItemToInventory(i);

		// Remove the item from the room.
		Room room = (Room)getter.getLocation();
		room.removeItem(i);

		res.setText("You get " + i.getIdlePrefix().toLowerCase()
				+ " " + i.getName() + "[R][WHITE].");

		// Notify other people in room
		VisualStimulus vs = new VisualStimulus();
		vs.setDepiction(getter.getBaseModel().getName() + " picks up "
				+ i.getIdlePrefix().toLowerCase() + " " + i.getName()
				+ "[R][WHITE].");
		
		StimulusSender.sendStimulus(getter.getLocation(), vs, getter);

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
