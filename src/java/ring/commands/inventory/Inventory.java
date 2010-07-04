package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.items.Item;
import ring.mobiles.Mobile;

public class Inventory implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		res.setFailText("You are currently carrying:\nNothing.");
		String text = "You are currently carrying:\n";
		Mobile mob = (Mobile) sender;
		
		//FQCN because of ambiguous reference
		ring.mobiles.backbone.Inventory inventory = mob.getDynamicModel().getInventory();

		if ((inventory == null) || (inventory.size() == 0)) {
			res.send();
			return;
		}

		for (Item item : inventory) {
			text += item.getName() + "\n";
		}

		res.setText(text);
		res.setSuccessful(true);
		res.send();
	}

	public String getCommandName() {
		return "inventory";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
