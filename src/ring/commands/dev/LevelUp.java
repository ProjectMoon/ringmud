package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.mobiles.Mobile;

public class LevelUp extends AbstractAdminCommand implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("Magic is powerful, but not THAT powerful.");
		
		if (super.isAccessAllowed(sender)) {
			Mobile mob = (Mobile) sender;
			mob.levelup();
		
			res.setText("Leveled up...");
			res.setSuccessful(true);
		}
		res.send();
	}

	public String getCommandName() {
		return "levelup";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}