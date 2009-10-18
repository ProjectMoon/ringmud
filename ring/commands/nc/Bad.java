package ring.commands.nc;

import ring.commands.CommandSender;

public class Bad implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setText("[R][GREEN]What?");
		res.setSuccessful(true);
		return res;		
	}

	public String getCommandName() {
		return "bad";
	}

	public void rollback() {
		
	}

}
