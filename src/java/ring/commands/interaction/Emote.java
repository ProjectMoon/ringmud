package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;

public class Emote implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		res.setFailText("Emote error.");
		//params.init(CommandType.TEXT);

		String emoteText = "";//params.paramString();

		// did they actually type something to emote?
		if (emoteText == null) {
			res.setFailText("[R][GREEN]What do you want to emote?[WHITE]");
			res.send();
		}

		// so they did.
		Mobile mob = (Mobile) sender;
		emoteText = mob.getBaseModel().getName() + " " + emoteText;

		// broadcast to the world and player.
		VisualStimulus stim = new VisualStimulus();
		stim.setDepiction(emoteText);
		StimulusSender.sendStimulus(mob.getLocation(), stim, mob);
		
		
		res.setText(emoteText);
		res.setSuccessful(true);
		res.send();
	}

	public String getCommandName() {
		return "emote";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
