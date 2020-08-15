package ring.commands.mud;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.players.PlayerCharacter;

public class Quit implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setSuccessful(true);
		res.setText("Shutting down your connection...");
		PlayerCharacter player = (PlayerCharacter)sender;
		player.quit();
		res.send();
	}

	public String getCommandName() {
		return "quit";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
