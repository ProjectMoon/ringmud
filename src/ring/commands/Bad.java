package ring.commands;


public class Bad implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setText("[R][GREEN]What?");
		res.setSuccessful(true);
		res.send();		
	}

	public String getCommandName() {
		return "bad";
	}

	public void rollback() {
		
	}

}
