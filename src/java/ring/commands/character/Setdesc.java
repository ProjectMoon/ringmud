package ring.commands.character;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Setdesc implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		//params.init(CommandType.TEXT);
		//String text = params.paramString();
		String text = "";
		res.setFailText("[R][WHITE]You didn't enter anything!");
		
		if (text == null)
			res.send();
		
		Mobile mob = (Mobile) sender;
		mob.getBaseModel().setDescription(text);
		res.setText("[R][WHITE]Description changed to: " + text);
		res.setSuccessful(true);
		res.send();
	}

	public String getCommandName() {
		return "setdesc";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
