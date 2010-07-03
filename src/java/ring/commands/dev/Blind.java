package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class Blind extends AbstractAdminCommand implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
		
			boolean blind = mob.getDynamicModel().getSensesGroup().getVisualSense().disabledFromBindness();
			mob.getDynamicModel().getSensesGroup().getVisualSense().setDisabledFromBlindness(!blind);
			
			res.setFailText("Couldn't make you blind (or not blind).");
			res.setText("Your blindess: " + !blind);
			res.setSuccessful(true);
		}
		res.send();
	}

	public String getCommandName() {
		return "blind";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
