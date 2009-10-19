package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.mobiles.Mobile;
import ring.world.World;

//TODO implement say
public class Say implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		// Make the CommandResult object.
		params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		res.setFailText("You cannot talk!");
		Mobile mob = (Mobile) sender;
		
		// Someone silenced can't talk...
		if (mob.isSilent)
			return res;

		String message = "";
		String textBackToPlayer;

		// check for speaker's deafness.
		if (mob.isDeaf)
			textBackToPlayer = "You say something, but you're not entirely sure what since you cannot hear!";
		else
			textBackToPlayer = "You say, \"";

		String textToOtherPlayers = mob.getName() + " says, \"";

		// Get the "parameters" as words.
		int length = params.length();
		for (int c = 0; c < length; c++) {
			message += (String) params.getParameter(c);

			if (c != length - 1) {
				message += " ";
			}
		}

		// check for speaker's deafness deafness again.
		if (!mob.isDeaf) {
			textBackToPlayer += message;
			textBackToPlayer += "\"";
		}

		textToOtherPlayers += message;
		textToOtherPlayers += "\"";

		res.setText(textBackToPlayer);

		World.sendAudioToLocation(mob, textToOtherPlayers, mob.getName()
				+ " says something, but you cannot hear it!\n");

		res.setSuccessful(true);
		
		// Return the CommandResult.
		return res;

	}

	public String getCommandName() {
		return "say";
	}

	public void rollback() {
		throw new UnsupportedOperationException();	
	}

}
