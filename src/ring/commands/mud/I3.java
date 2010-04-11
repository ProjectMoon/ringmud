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
			String mudName = params.getTextParameters(1);
			
			try {
				client.submitWhoRequest(mudName);
				res.setText("Who request for [B]" + mudName + "[R] submitted.");
				res.setSuccessful(true);
			} catch (I3NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else if (op.equalsIgnoreCase("tell")) {
			String[] target = params.getParameterAsText(1).split("@");
			String message = params.getTextParameters(2);
			
			System.out.println("tell mud: " + target[1]);
			System.out.println("tell user: " + target[0]);
			
			client.sendTell(target[1], target[0], message);
			res.setText("You tell [B]" + target[0] + "@" + target[1] + "[R]: " + message);
			res.setSuccessful(true);
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
