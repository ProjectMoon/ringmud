package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;

import ring.effects.Affectable;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.movement.Room;

public class Drop implements Command {

	public synchronized void execute(CommandSender sender, CommandArguments params) {
		//params.init(CommandType.INVENTORY);
		Object target = null;//params.getParameter(0);

		CommandResult res = new CommandResult();

		if (target == null) {
			res.setFailText("[R][WHITE]Drop what?");
			res.send();
		}

		if (!(target instanceof Affectable)) {
			res.setFailText("[R][WHITE]You don\'t have that item.");
			res.send();
		}

		Item item = (Item) target;
		Mobile mob = (Mobile) sender;

		// Remove the item from the inventory.
		if (mob.removeItemFromInventory(item)) {
			// Put it back in the room the mobile is in.
			Room room = (Room) mob.getLocation();
			room.addItem(item);
			System.out.println("added.");

			// Set the text.
			res.setText("[R][WHITE]You drop "
					+ item.getIdlePrefix().toLowerCase() + " " + item.getName()
					+ "[R][WHITE].");

			// Notify other people in the world.
			VisualStimulus vs = new VisualStimulus();
			vs.setDepiction(mob.getBaseModel().getName() + " drops "
					+ item.getIdlePrefix().toLowerCase() + " " + item.getName()
					+ "[R][WHITE].");

			vs.setBlindDepiction("You hear the thud of something being dropped.");

			StimulusSender.sendStimulus(mob.getLocation(), vs, mob);

		} else {
			res.setFailText("[R][WHITE]You can't drop that!");
		}
		res.setSuccessful(true);
		res.send();
	}

	public String getCommandName() {
		return "drop";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
