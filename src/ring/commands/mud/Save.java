package ring.commands.mud;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Save implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		/*
		res.setFailText("[B][RED]SAVING FAILED!! PLEASE NOTIFY AN ADMINISTRATOR!![R][WHITE]");
		Mobile mob = (Mobile) sender;

		MobileLoader.saveMobile(mob.getName() + ".mob", mob);

		res.setText("[R][WHITE]Character saved.");
		res.setSuccessful(true);
		return res;
		*/
		
		throw new UnsupportedOperationException();
	}

	public String getCommandName() {
		return "save";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
