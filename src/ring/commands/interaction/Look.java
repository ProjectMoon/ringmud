package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.effects.Affectable;
import ring.mobiles.Mobile;

public class Look implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		Object t = params.getParameter(0);

		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You don't see that here.");

		// set up an object for our "looker"
		Mobile mob = (Mobile) sender;

		// First check to see if we are just looking at the room. ie: sending
		// just the "look" command. If so, we delegate to RoomLookAction.
		if (t == null) {
			new RoomLookAction(mob).doLook();
			return CommandResult.blankResult(true);
		}

		// Now check to see if we are looking at something specific, if true,
		// return the look description
		// of the thing being looked at it.
		else {
			if ((!(t instanceof Affectable)) && (t != null))
				return res;

			// is our looker blind?
			if (mob.getBaseModel().isBlind()) {
				res.setFailText("You have nothing to look at, for you are blind!");
				return res;
			}

			Affectable target = (Affectable) t;
			// Get the look text of the thing being looked at.
			String lookText = target.getLongDescription();
			// add it to the CommandResult.
			res.setText("You look at " + target.getName() + " and see:\n"
					+ lookText);
			// The command succeeded.
			res.setSuccessful(true);
		}

		// Return CommandResult.
		return res;
	}

	public String getCommandName() {
		return "look";
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}

}
