package ring.commands.admin;

import java.util.List;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.annotations.Template;
import ring.commands.annotations.Form;
import ring.mobiles.senses.stimuli.AudioStimulus;
import ring.players.PlayerCharacter;
import ring.server.MUDConnectionManager;

@Template({
	@Form(clause = "#text")
})
public class Godvoice extends AbstractAdminCommand implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("You don't feel particularly divine...");
		
		//Make sure person is an administrator.
//		if (super.isAccessAllowed(sender) == false) {
//			return res;
//		}
		
		String message = (String)params.getArgument(0);
		String textBackToPlayer;

		textBackToPlayer = "You project your voice across the cosmos, saying, \"";

		String textToOtherPlayers = "The voice of the gods rumbles in the sky: \"";
		
		textBackToPlayer += message;
		textBackToPlayer += "\"";

		textToOtherPlayers += message;
		textToOtherPlayers += "\"";

		res.setText(textBackToPlayer);
		
		//Godvoice is sent to all players.
		List<PlayerCharacter> players = MUDConnectionManager.getCurrentCharacters();
		
		//Not even being deaf stops the power of the gods.
		AudioStimulus as = new AudioStimulus();
		as.setDepiction(textToOtherPlayers);
		as.setDeafDepiction(textToOtherPlayers);
		
		//Players consume this stimulus directly.
		for (PlayerCharacter player : players) {
			//Ignore the sender.
			if (player != sender) {
				player.getDynamicModel().getSensesGroup().consume(as);
			}
		}

		res.setSuccessful(true);
		// Return the CommandResult.
		res.send();

	}

	public String getCommandName() {
		return "godvoice";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
