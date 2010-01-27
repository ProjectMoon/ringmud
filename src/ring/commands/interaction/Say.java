package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.AudioStimulus;
import ring.world.World;

public class Say implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		// Make the CommandResult object.
		params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		res.setFailText("You cannot talk!");
		Mobile mob = (Mobile) sender;
		
		// Someone silenced can't talk...
		if (mob.getBaseModel().isSilent())
			return res;

		String message = "";
		String textBackToPlayer;

		// check for speaker's deafness.
		if (mob.getBaseModel().isDeaf())
			textBackToPlayer = "You say something, but you're not entirely sure what since you cannot hear!";
		else
			textBackToPlayer = "You say, \"";

		String textToOtherPlayers = mob.getBaseModel().getName() + " says, \"";

		// Get the "parameters" as words.
		int length = params.length();
		for (int c = 0; c < length; c++) {
			message += (String) params.getParameter(c);

			if (c != length - 1) {
				message += " ";
			}
		}

		// check for speaker's deafness deafness again.
		if (!mob.getBaseModel().isDeaf()) {
			textBackToPlayer += message;
			textBackToPlayer += "\"";
		}

		textToOtherPlayers += message;
		textToOtherPlayers += "\"";

		res.setText(textBackToPlayer);

		//Send audio to other players in the room.
		AudioStimulus as = new AudioStimulus();
		as.setDepiction(textToOtherPlayers);
		as.setDeafDepiction(mob.getBaseModel().getName() + " say something, but you cannot hear it!");
		StimulusSender.sendStimulus(mob.getLocation(), as, mob);

		//The text will get sent back to this player as command result data.
		res.setSuccessful(true);
		return res;

	}

	public String getCommandName() {
		return "say";
	}

	public void rollback() {
		throw new UnsupportedOperationException();	
	}

}
