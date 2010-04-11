package ring.commands.mud;

import java.util.List;

import com.aelfengard.i3.I3NotConnectedException;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.intermud3.Intermud3Client;
import ring.intermud3.Intermud3Daemon;
import ring.players.PlayerCharacter;

public class I3 implements Command {

	@Override
	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		
		res.setFailText("This MUD is not connected to I3.");
				
		//Continue onwards.
		String op = params.getParameterAsText(0);
		
		PlayerCharacter pc = (PlayerCharacter)sender;
		Intermud3Client client = Intermud3Daemon.currentDaemon().getClientForPlayer(pc.getPlayer());
		
		if (!client.isConnected()) {
			return res;
		}
		
		String result = "";
		if (op.equalsIgnoreCase("muds")) {
			result = "List of MUDs:\n";
			List<String> muds = client.getMuds();
			for (String mud : muds) {
				result += mud + "\n";
			}
			
			res.setSuccessful(true);
			res.setText(result);
		}
		else if (op.equalsIgnoreCase("who")) {
			String mudName = params.getParameterAsText(1);
			
			try {
				client.submitWhoRequest(mudName);
			} catch (I3NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return res;
	}

	@Override
	public String getCommandName() {
		return "i3";
	}

	@Override
	public void rollback() {
		
	}

}
