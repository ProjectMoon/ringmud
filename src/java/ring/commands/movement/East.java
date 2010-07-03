package ring.commands.movement;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.PortalNotFoundException;

public class East implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;
		Portal destination;
		
		try {
			destination = LocationManager.getPortal(mob.getLocation(), LocationManager.EAST);
			LocationManager.move(mob, destination);
		}
		catch (PortalNotFoundException e) {
		}
	}

	public String getCommandName() {
		return "east";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
