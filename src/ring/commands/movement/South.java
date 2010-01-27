package ring.commands.movement;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;
import ring.movement.PortalNotFoundException;

public class South implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		MoveAction ma = new MoveAction(mob, LocationManager.SOUTH);

		boolean success = false;

		try {
			success = ma.doMove();
		} catch (PortalNotFoundException e) {
			// can theoretically be safely ignored.
		}

		res.setSuccessful(success);
		// A bit odd, but the data sending is handled by the movement system in
		// this case.
		if (success) {
			res.setReturnableData(false);
		}

		// Return the CommandResult.
		return res;
	}

	public String getCommandName() {
		return "south";
	}

	public void rollback() {

	}

}
