package ring.commands.character;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.mobiles.Mobile;

public class Setdesc implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		params.init(CommandType.TEXT);
		String text = params.paramString();
		res.setFailText("[R][WHITE]You didn't enter anything!");
		if (text == null)
			return res;
		Mobile mob = (Mobile) sender;
		mob.setLongDescription(text);
		res.setText("[R][WHITE]Description changed to: " + text);
		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "setdesc";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
