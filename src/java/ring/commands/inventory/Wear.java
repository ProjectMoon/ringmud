package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.items.Armor;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;

public class Wear implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		System.out.println("In wear");
		//params.init(CommandType.INVENTORY);
		Object t = null;//params.getParameter(0);
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You can't wear that!");

		if (t == null) {
			res.setFailText("[R][WHITE]Wear what?");
			res.send();
			return;
		}
		if (!(t instanceof Armor)) {
			res.send();
			return;
		}

		Armor target = (Armor)t;

		// We've checked everything dealing with if the thing is wearable or
		// not.
		Mobile mob = (Mobile) sender;

		// Check if the target is wearable.
		if (!(target.isWearableFor(mob))) {
			res.send();
			return;
		}

		// Check if the wearer meets all the requirements to wear target
		// (alignment, class, etc.)

		// Check if wearer has the actual body part to wear the target on. The
		// method that will do
		// this actually returns an integer corresponding to the number of
		// eligible body parts
		// on the wearer's body. If this number is greater than one, the next
		// step has a special
		// addition.
		
		boolean success = false;
		success = mob.equip(target.getPartWornOn(), target);
		
		if (!success) {
			res.setFailText("[R][WHITE]You lack the proper body shape to wear that!");
			res.send();
		}

		// Remove the target from the wearer's inventory.
		mob.removeItemFromInventory(target);

		// Set text and stuff.
		res.setText("[R][WHITE]You wear "
				+ target.getIdlePrefix().toLowerCase() + " "
				+ target.getName() + " on your "
				+ target.getPartWornOn().getName().toLowerCase() + ".");
		res.setSuccessful(true);

		// Notify other players.
		VisualStimulus vs = new VisualStimulus();
		vs.setDepiction("[R][WHITE]" + mob.getBaseModel().getName()
				+ " wears " + target.getIdlePrefix().toLowerCase()
				+ " " + target.getName() + " on " + mob.getBaseModel().getGender().getPossessive() + " "
				+ target.getPartWornOn().getName().toLowerCase() + ".");
		
		StimulusSender.sendStimulus(mob.getLocation(), vs, mob);
		
		res.send();
	}

	public String getCommandName() {
		return "wear";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
