package ring.commands.mud;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;

//TODO implement help
public class Help implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		throw new UnsupportedOperationException();
	}

	public String getCommandName() {
		return "help";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
