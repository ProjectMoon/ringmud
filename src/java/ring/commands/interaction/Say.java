package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.AudioStimulus;

public class Say implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		// Make the CommandResult object.
		//params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		res.setFailText("You cannot talk!");
		Mobile mob = (Mobile) sender;
		
		// Someone silenced can't talk...
		if (mob.getBaseModel().isSilent())
			res.send();

		String message = "";
		String textBackToPlayer;

		// check for speaker's deafness.
		if (mob.getDynamicModel().getSensesGroup().getAuditorySense().isDisabled())
			textBackToPlayer = "You say something, but you're not entirely sure what since you cannot hear!";
		else
			textBackToPlayer = "You say, \"";

		String textToOtherPlayers = mob.getBaseModel().getName() + " says, \"";

		// Get the "parameters" as words.
		int length = 0; //params.length();
		for (int c = 0; c < length; c++) {
			message += (String)"";

			if (c != length - 1) {
				message += " ";
			}
		}

		// check for speaker's deafness deafness again.
		if (!mob.getDynamicModel().getSensesGroup().getAuditorySense().isDisabled()) {
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
		res.send();

	}

	public String getCommandName() {
		return "say";
	}

	public void rollback() {
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
