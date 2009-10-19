package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.effects.Affectable;
import ring.entities.Item;
import ring.mobiles.BodyPart;
import ring.mobiles.Mobile;

public class Remove implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.EQUIPMENT);
		Object t = params.getParameter(0);

		CommandResult res = new CommandResult();

		// Check if the thing is an actual item...
		if (t == null) {
			res.setFailText("[R][WHITE]Remove what?");
			return res;
		}

		if (!(t instanceof Affectable)) {
			res.setFailText("[R][WHITE]You aren't wearing that!");
			return res;
		}

		// If yes, make it an item.
		Item item = (Item) t;

		BodyPart part = item.getPartWornOn();
		Mobile mob = (Mobile) sender;

		mob.dequip(part);
		mob.addItemToInventory(item);

		res.setText("[R][WHITE]You remove "
				+ item.getIndefiniteDescriptor().toLowerCase() + " "
				+ item.getName() + " from your " + part.getName().toLowerCase()
				+ ".");

		res.setSuccessful(true);
		return res;

	}

	public String getCommandName() {
		return "remove";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
