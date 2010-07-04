package ring.commands.mud;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Save implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
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

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
