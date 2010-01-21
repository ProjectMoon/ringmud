package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class Blind extends AbstractAdminCommand implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			if (mob.isBlind)
				mob.isBlind = false;
			else
				mob.isBlind = true;
			res.setFailText("Couldn't make you blind (or not blind).");
			res.setText("Your blindess: " + mob.isBlind);
			res.setSuccessful(true);
		}
		return res;
	}

	public String getCommandName() {
		return "blind";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
