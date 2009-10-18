package ring.commands.nc;

import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;

public class South implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		boolean success = mob.move(LocationManager.SOUTH);
		res.setSuccessful(success);
		//A bit odd, but the data sending is handled by the movement system in this case.
		if (success) {
			res.setReturnData(false);
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
