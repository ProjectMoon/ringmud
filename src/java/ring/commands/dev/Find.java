package ring.commands.dev;

import java.util.List;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.nrapi.ObjectIndexSystem;
import ring.nrapi.ObjectSearch;
import ring.nrapi.business.BusinessObject;

public class Find implements Command {

	@Override
	public void execute(CommandSender sender, CommandArguments params) {
		CommandResult res = new CommandResult();
		ObjectSearch search = ObjectIndexSystem.newSearch();
		
		String xpath = "";
		List<BusinessObject> bos = search.search(xpath);
		
		String result = "";
		for (BusinessObject bo : bos) {
			result += bo + "\n";
		}
		
		res.setSuccessful(true);
		res.setText(result);
		res.send();
	}

	@Override
	public String getCommandName() {
		return "find";
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

}
