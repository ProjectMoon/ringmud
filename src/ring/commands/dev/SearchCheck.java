package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class SearchCheck extends AbstractAdminCommand implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("Yes, you do have one of those...");
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			res.setText("Your current search check: " + mob.hiddenExitSearchCheck);
			res.setSuccessful(true);
		}
		return res;
	}

	public String getCommandName() {
		return "searchcheck";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
