package ring.mobiles.senses.handlers;

import ring.commands.CommandResult;
import ring.mobiles.senses.DepictionHandler;
import ring.mobiles.senses.ProcessedDepiction;

public class CommandResultHandler implements DepictionHandler {
	private CommandResult result;
	
	public CommandResultHandler(CommandResult result) {
		this.result = result;
	}
	
	@Override
	public void handle(ProcessedDepiction depiction) {
		result.setText(depiction.getDepiction());
		result.setSuccessful(true);
		result.send();
	}

}
