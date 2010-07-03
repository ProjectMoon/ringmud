package ring.commands.dev;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.admin.AbstractAdminCommand;
import ring.events.Event;
import ring.events.EventDispatcher;

/**
 * Dumps events.
 * @author projectmoon
 *
 */
public class Edump extends AbstractAdminCommand implements Command {

	@Override
	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		res.setSuccessful(true);
		
		String text = "Event Information:\n";
		for (Event e : EventDispatcher.getEvents()) {
			text += "[" + e + "]:\n";
			text += "   Type: " + e.getName() + "\n";
			text += "   Bound To: ";
			
			for (String document : e.getContext().getDocuments()) {
				for (String id : e.getContext().getIDs(document)) {
					String canonicalID = document + ":" + id;
					text += canonicalID + " ";
				}
			}
			
			text += "\n\n";
		}
		
		res.setText(text);
		res.send();
	}

	@Override
	public String getCommandName() {
		return "edump";
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	
}
