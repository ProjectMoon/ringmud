package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.entities.Item;
import ring.mobiles.Body;
import ring.mobiles.BodyPart;
import ring.mobiles.Mobile;
import ring.world.World;

public class Wear implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		System.out.println("In wear");
		params.init(CommandType.INVENTORY);
		Object t = params.getParameter(0);
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You can't wear that!");

		if (t == null) {
			res.setFailText("[R][WHITE]Wear what?");
			return res;
		}
		if (!(t instanceof Item)) {
			return res;
		}

		Item target = (Item) t;
		if (!target.isWearable())
			return res;

		// We've checked everything dealing with if the thing is wearable or
		// not.
		Mobile mob = (Mobile) sender;
		Body mobBody = mob.getBody();

		// Check if the target is wearable.
		if (!(target.isWearable()))
			return res;

		// Check if the wearer meets all the requirements to wear target
		// (alignment, class, etc.)

		// Check if wearer has the actual body part to wear the target on. The
		// method that will do
		// this actually returns an integer corresponding to the number of
		// eligible body parts
		// on the wearer's body. If this number is greater than one, the next
		// step has a special
		// addition.
		BodyPart[] eligibleParts = mobBody.getEligibleBodyParts(target);

		// Were there any parts actually found?
		if (eligibleParts == null)
			return res;

		// Check if the wearer has room on the body part. If the number from the
		// previous step was
		// greater than 1, start a for loop equal to the amount of body parts
		// remaining (# of them - 1).
		// Try to equip on each of those body parts.
		boolean success = false;
		BodyPart thePart = null;

		for (int c = 0; c < eligibleParts.length; c++) {
			thePart = eligibleParts[c];
			success = mob.equip(thePart, target);
			if (success)
				break;
		}
		
		if (!success) {
			res.setFailText("[R][WHITE]You lack the proper body shape to wear that!");
			return res;
		}

		// Remove the target from the wearer's inventory.
		mob.removeItemFromInventory(target);

		// Set text and stuff.
		res.setText("[R][WHITE]You wear "
				+ target.getIndefiniteDescriptor().toLowerCase() + " "
				+ target.getName() + " on your "
				+ thePart.getName().toLowerCase() + ".");
		res.setSuccessful(true);

		// Notify other players.
		World.sendVisualToLocation(mob, "[R][WHITE]" + mob.getName()
				+ " wears " + target.getIndefiniteDescriptor().toLowerCase()
				+ " " + target.getName() + " on " + mob.getHisHerIts() + " "
				+ thePart.getName().toLowerCase() + ".", null);

		return res;

	}

	public String getCommandName() {
		return "wear";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
