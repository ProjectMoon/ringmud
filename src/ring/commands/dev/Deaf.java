package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class Deaf extends AbstractAdminCommand implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			boolean deaf = mob.getDynamicModel().getSensesGroup().getAuditorySense().isDisabled();
			mob.getDynamicModel().getSensesGroup().getAuditorySense().setDeafness(!deaf);
			
			res.setFailText("Couldn't make you deaf (or not deaf).");
			res.setText("Your deafness: " + !deaf);
			res.setSuccessful(true);
		}
		res.send();
	}

	public String getCommandName() {
		return "deaf";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
