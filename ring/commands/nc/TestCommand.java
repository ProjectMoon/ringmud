package ring.commands.nc;

import ring.commands.CommandSender;

public class TestCommand implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		System.out.println("hello from test!");
		return null;
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	
	public String getCommandName() {
		return "test";
	}

}
