package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

//TODO implement levelup
public class LevelUp extends AbstractAdminCommand implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("Magic is powerful, but not THAT powerful.");
		
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			mob.levelup();
		
			res.setText("Leveled up...");
			res.setSuccessful(true);
		}
		return res;
	}

	public String getCommandName() {
		throw new UnsupportedOperationException();
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}