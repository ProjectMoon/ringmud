package ring.commands.mud;

import java.util.ArrayList;
import java.util.List;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.players.PlayerCharacter;
import ring.world.World;

//TODO implement who
public class Who implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]Please use the command 'help who' for information on how to use the who command.");

		//For now, simple implementation
		String[] whoParams = params.getParameterArray();
		if (whoParams == null || whoParams.length == 0) {
			return res;
		}
		
		List<PlayerCharacter> players = World.getWorld().getPlayers();
		ArrayList<String> whoNames = new ArrayList<String>();
		
		for (String whoParam : whoParams) {
			for (PlayerCharacter player : players) {
				if (player.getWhoTag().contains(whoParam)) {
					whoNames.add(player.getBaseModel().getName());
				}
			}
		}
		
		String whoText = "Players Online:\n---------------\n";
		
		for (String name : whoNames) {
			whoText += name + "\n";
		}
		
		res.setSuccessful(true);
		res.setText(whoText);
		
		// Parse the parameters.
		// Current acceptable parameters include the following:
		// class names, player names, race names, individual level (a number), a
		// level range (#-#),
		// rp, lfg, chaotic, lawful, neutral, good, evil, god, staff, immortal,
		// mortal,
		// forger, greater, lesser, admin, areas, rpgod, code, zone, invis
		// (<-THIS LINE FOR GODS ONLY!!)

		// IF AN ILLEGAL PARAMETER IS FOUND, simply return res and it will
		// return the failed
		// CommandResult with the error message. Illegal parameters are:
		// Anything that's not in the above list.
		// Anything a player should not have access to (i.e. mortals doing
		// "who zone" or "who admin")

		// Produce a String that meets all acceptable parameters.
		// To do this, run through the list of all players and test their whotag
		// against the parameter list string of
		// this command. A player's whotag has the following format:
		// "playername racename level classname zonename ethical moral <god status> <player set status> <admin set status>"
		// For example:
		// "ao human 13 fighter Waterdeep neutral evil forger rp code areas"

		// A simple indexOf test or something similar against all whotags will
		// allow us to construct the list of
		// players meeting the user's selected parameters.

		// Set the CommandResult's text to the string.

		// Return the result.
		return res;
	}

	public String getCommandName() {
		return "who";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
