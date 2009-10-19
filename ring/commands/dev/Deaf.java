package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class Deaf extends AbstractAdminCommand implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			if (mob.isDeaf)
				mob.isDeaf = false;
			else
				mob.isDeaf = true;
			res.setFailText("Couldn't make you deaf (or not deaf).");
			res.setText("Your deafness: " + mob.isDeaf);
			res.setSuccessful(true);
		}
		return res;

	}

	public String getCommandName() {
		return "deaf";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
