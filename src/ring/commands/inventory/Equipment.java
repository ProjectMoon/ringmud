package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.items.Item;
import ring.mobiles.Mobile;

public class Equipment implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You are wearing:\nNothing.");

		String equipment = "[R][WHITE]You are wearing:\n";
		Mobile mob = (Mobile) sender;
		
		//FQCN because of ambiguous reference
		ring.mobiles.backbone.Equipment mobEquipment = mob.getDynamicModel().getEquipment();

		if ((mobEquipment == null) || (mobEquipment.size() == 0))
			return res;

		for (Item item : mobEquipment) {
			if (item != null) {
				equipment += "<worn on " + item.getPartWornOn().getName()
						+ "> " + item.getName() + "\n";
			}
		}

		res.setText(equipment);
		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "equipment";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
