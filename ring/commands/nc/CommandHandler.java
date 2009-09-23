package ring.commands.nc;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;

/**
 * This class provides command handling service to a CommandSender (usually a mobile).
 * The CommandHandler class maintains a global store of all indexed command objects, as
 * well as the sender's list of aliased commands. The main methd of this class is the 
 * sendCommand method which returns a CommandResult indicating success or failure of the
 * given command.
 * @author projectmoon
 *
 */
public final class CommandHandler {
	// This is the class that contains code for all commands. It returns a
	// CommandResult object with a
	// boolean value and a String. The boolean indicates if the command
	// "failed." The String is what gets
	// sent back to the mobile if it is a PlayerCharacter.

	// The Command-Sending object this handler is linked to.
	private CommandSender sender;
	
	//Map of all built-in commands. This is shared across
	//all instances for performance/space reasons.
	//Doesn't need to be synchronized because Commands are immutable.
	private static HashMap<String, Command> commands = new HashMap<String, Command>();
												
	//Map of alternate (aliased) commands. Stored per class instance
	//So each user can have their own set of aliases.
	private HashMap<String, String> alternateCommands;
	
	//It's always good to know what's happening!
	private static final Logger log = Logger.getLogger(CommandHandler.class.getName());


	public CommandHandler(CommandSender sender) {
		this.sender = sender;
		
		alternateCommands = new HashMap<String, String>();

		// Register the alternate commands.
		// These commands are checked BEFORE command completion
		registerAlternateCommand("north", "n");
		registerAlternateCommand("south", "s");
		registerAlternateCommand("west", "w");
		registerAlternateCommand("east", "e");
		registerAlternateCommand("up", "u");
		registerAlternateCommand("down", "d");
		registerAlternateCommand("look", "l");
		registerAlternateCommand("look", "lo");
		registerAlternateCommand("say", "\'");
		registerAlternateCommand("inventory", "inv");
		registerAlternateCommand("equipment", "eq");
		registerAlternateCommand("prepare", "prep");
		registerAlternateCommand("movesilently", "ms");
	}
	
	/**
	 * Searches a given Java package for Command objects. Any objects
	 * that are found are added to the global command list. This method
	 * is used during MUD boot to index command packages specified in the
	 * commands.properties file.
	 * @param pkgName
	 */
	public static void indexCommands(String packageName) {
		CommandIndexer indexer = new CommandIndexer(packageName);

		for (Command cmd : indexer.getCommands()) {
			addCommand(cmd.toString(), cmd);			
		}
	}
	

	
	public static void main(String[] args) {
		CommandHandler.indexCommands("ring.commands.nc");
		Command c = commands.get("test");
		System.out.println(c);
	}
	
	/**
	 * Adds an individual String-Command relation to the command Map. This
	 * method is useful for scripting languages that extend the MUD and
	 * implement their own commands.
	 * @param cmd
	 */
	public static void addCommand(String cmdKey, Command cmd) {
		commands.put(cmdKey, cmd);
	}

	/**
	 * Finds a Command given a string. This method has three levels of
	 * fallback before giving up. First, it checks the static command list.
	 * If it doesn't find anything there, it checks the local list of alternate
	 * commands. If that doesn't work, it attempts to complete the command.
	 * If none of those work, it returns the "bad" command.
	 * @param cmd
	 * @return
	 */
	private Command lookup(String cmd) {
		Command comm = commands.get(cmd);
		
		//Next try alternate commands
		if (comm == null) {
			comm = commands.get(alternateCommands.get(cmd));
			
			//Next try command completion.
			if (comm == null) {
				comm = completeCommand(cmd);
			}
		}
		
		if (comm == null) {
			return commands.get("bad");
		}
		else {
			return comm;
		}
	}
	
	private String[] parseCommandString(String command) {
		return command.split(" ");
	}
	
	private String[] isolateParameters(String[] parsedCmdString) {
		if (parsedCmdString.length == 1) return null;
		
		String[] params = new String[parsedCmdString.length - 1];
		
		for (int c = 1; c < parsedCmdString.length; c++) {
			params[c - 1] = parsedCmdString[c];
		}
		
		return params;
	}

	// sendCommand method.
	// This method splits a command up and calls handleCommand(). It returns a
	// CommandResult that the sender can use.
	public CommandResult sendCommand(String command) {
		log.info("received from [" + sender.toString() + "]: " + command);
		
		String[] parsedCmd = parseCommandString(command);
		Command cmd = lookup(parsedCmd[0]);
		CommandParameters params = new CommandParameters(isolateParameters(parsedCmd), sender);
		log.fine("Made cmd object");

		// actually do the command.
		CommandResult cr = handleCommand(cmd, params);
		log.info("handled command [" + command + "] from " + sender.toString());
		return cr;
	}

	// completeCommand method.
	// This method returns a command name based on a fragment given to it. It
	// returns by alphabetical priority
	// for ambiguous fragments. If no suitable command can be found, it returns
	// null.
	private Command completeCommand(String fragment) {
		// if the fragment is only 1 letter, it's not something we should bother
		// looking
		// up; there is TOO much ambiguity. 1 letter commands are registered as
		// alternate commands.
		if (fragment.length() <= 1) { // the 5 includes the CMD_
			return null;
		}

		// if the command is >= 2 letters, we can proceed with completion.
		// now, loop through all available command names
		// and see if we can find an available full command
		for (String key : commands.keySet()) {
			if (key.startsWith(fragment)) {
				return commands.get(key);
			}
		}

		return null;
	}

	// handleCommand method.
	// This method invokes the current command entered.
	private CommandResult handleCommand(Command cmd, CommandParameters params) {
		return cmd.execute(sender, params);
	}

	
	// registerAlternateCommand method.
	// This method registers an alternate--often shortened--command that is
	// linked to another command.
	// This is used for things like "north" and "n" being the same in-game. This
	// is a crude system and
	// I plan to implement an auto-text algorithm with priorities built in
	// later.
	//
	// Returns true if succeeds, and false if fails.
	public boolean registerAlternateCommand(String origCmd, String newCmd) {
		alternateCommands.put(newCmd, origCmd);
		// code here.
		return true;
	}

}