package ring.commands.movement;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.PortalNotFoundException;

public class North implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;
		Portal destination;
		
		try {
			destination = LocationManager.getPortal(mob.getLocation(), LocationManager.NORTH);
			boolean success = LocationManager.move(mob, destination);
			res.setSuccessful(success);
			res.setReturnableData(!success);
		}
		catch (PortalNotFoundException e) {
			res.setSuccessful(false);
			res.setReturnableData(true);
			res.send();
		}
	}

	public String getCommandName() {
		return "north";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
