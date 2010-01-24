package ring.commands.inventory;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.items.Armor;
import ring.items.Item;
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
		if (!(t instanceof Armor)) {
			return res;
		}

		Armor target = (Armor)t;

		// We've checked everything dealing with if the thing is wearable or
		// not.
		Mobile mob = (Mobile) sender;

		// Check if the target is wearable.
		if (!(target.isWearableFor(mob)))
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
		
		boolean success = false;
		success = mob.equip(target.getPartWornOn(), target);
		
		if (!success) {
			res.setFailText("[R][WHITE]You lack the proper body shape to wear that!");
			return res;
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
		World.sendVisualToLocation(mob, "[R][WHITE]" + mob.getBaseModel().getName()
				+ " wears " + target.getIdlePrefix().toLowerCase()
				+ " " + target.getName() + " on " + mob.getBaseModel().getGender().getPossessive() + " "
				+ target.getPartWornOn().getName().toLowerCase() + ".", null);

		return res;

	}

	public String getCommandName() {
		return "wear";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
