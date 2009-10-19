package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.effects.Affectable;
import ring.entities.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.world.World;

public class Drop implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.INVENTORY);
		Object target = params.getParameter(0);

		CommandResult res = new CommandResult();

		if (target == null) {
			res.setFailText("[R][WHITE]Drop what?");
			return res;
		}

		if (!(target instanceof Affectable)) {
			res.setFailText("[R][WHITE]You don\'t have that item.");
			return res;
		}

		Item item = (Item) target;
		Mobile mob = (Mobile) sender;

		// Remove the item from the inventory.
		if (mob.removeItemFromInventory(item)) {
			// Put it back in the room the mobile is in.
			Room room = (Room)mob.getLocation();
			room.addEntity(item);
			System.out.println("added.");
	
			// Set the text.
			res.setText("[R][WHITE]You drop "
					+ item.getIndefiniteDescriptor().toLowerCase() + " "
					+ item.getName() + "[R][WHITE].");
	
			// Notify other people in the world.
			World.sendVisualToLocation(mob, mob.getName() + " drops "
					+ item.getIndefiniteDescriptor().toLowerCase() + " "
					+ item.getName() + "[R][WHITE].",
					"\nYou hear the thud of something being dropped.\n");
	
			}
		else {
			res.setFailText("[R][WHITE]You can't drop that!");
		}
		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "inventory";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
