package ring.commands.interaction;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.effects.Affectable;
import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.senses.stimuli.VisualStimulus;

public class Look implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.FROM_ROOM);
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
			// is our looker blind?
			if (mob.getBaseModel().isBlind()) {
				res.setFailText("You have nothing to look at, for you are blind!");
				return res;
			}
			
			//If not, proceed with looking at stuff.
			if (t instanceof Mobile) {
				Mobile lookingAt = (Mobile)t;
				VisualStimulus stimulus = new VisualStimulus();
				stimulus.setDepiction("You look at " + lookingAt.getBaseModel().getName() + " and see:\n" + lookingAt.getBaseModel().getDescription());
				
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
				return CommandResult.blankResult(true);
			}
			else if (t instanceof Entity) {
				throw new UnsupportedOperationException("Looking at entities not yet implemented.");
			}
			else if (t instanceof Item) {
				throw new UnsupportedOperationException("Looking at items not yet implemented.");
			}
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
