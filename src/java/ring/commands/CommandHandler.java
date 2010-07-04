package ring.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import ring.commands.parser.CommandParser;
import ring.commands.parser.CommandParsingException;

/**
 * This class provides command handling service to a CommandSender (usually a mobile).
 * The CommandHandler class maintains a global store of all indexed command objects, as
 * well as the sender's list of aliased commands. The most important method of this class 
 * is the sendCommand method which returns a CommandResult indicating success or failure 
 * of the given command.
 * @author projectmoon
 *
 */
public final class CommandHandler {
	// The Command-Sending object this handler is linked to.
	private CommandSender sender;
	
	//Map of all built-in commands. This is shared across
	//all instances for performance/space reasons.
	//Doesn't need to be synchronized because Commands are immutable.
	//TreeMap for guaranteed entry order so that command completion
	//behavior is always the same.
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	//each command has a parser.
	private static Map<Command, CommandParser> parserMap = new HashMap<Command, CommandParser>();
												
	//Map of alternate (aliased) commands. Stored per class instance
	//So each user can have their own set of aliases.
	//HashMap for faster alternate cmd lookup. Don't care about order.
	private HashMap<String, String> alternateCommands;
	
	//It's always good to know what's happening!
	private static final Logger log = Logger.getLogger(CommandHandler.class.getName());


	/**
	 * Creates a new CommandHandler with the given CommandSender.
	 * @param sender
	 */
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
	 * Adds a list of Command objects to the set of all
	 * commands.
	 * @param cmds
	 */
	public static void addCommands(List<Command> cmds) {
		for (Command cmd : cmds) {
			//Command names should never be null.
			assert (cmd.getCommandName() != null);
			
			//If they are null for some reason, ignore it and continue.
			if (cmd.getCommandName() == null) {
				log.warning(cmd + " has no command name! Ignoring it.");
				continue;
			}
			
			if (containsCommand(cmd.getCommandName()) == false) {
				try {
					CommandParser parser = new CommandParser(cmd);
					commands.put(cmd.getCommandName(), cmd);
					parserMap.put(cmd, parser);
				}
				catch (CommandParsingException e) {
					log.severe("Error parsing command forms: " + e.getMessage());
				}
				
			}
			else {
				String collision = "Command [" + cmd.getCommandName() +"] is already in the command map!\n" +
					"Colliding objects: [" + cmd + "] and [" + commands.get(cmd.getCommandName()) + "]";
				log.severe(collision);
			}
		}
	}
	
	/**
	 * Adds an individual String-Command relation to the command Map. This
	 * method is useful for scripting languages that extend the MUD and
	 * implement their own commands and don't have a CommandIndexer to
	 * populate the command map with.
	 * @param cmd
	 */
	public static void addCommand(Command cmd) {
		try {
			CommandParser parser = new CommandParser(cmd);
			commands.put(cmd.getCommandName(), cmd);
			parserMap.put(cmd, parser);
		}
		catch (CommandParsingException e) {
			log.severe("Error parsing command forms: " + e.getMessage());
		}
	}
		
	/**
	 * Tells whether or not the command key is present in the shared command
	 * map.
	 * @param cmdKey
	 * @return true if the key is present, false otherwise.
	 */
	public static boolean containsCommand(String cmdKey) {
		return commands.containsKey(cmdKey);
	}

	/**
	 * Finds a Command given a string. This method has three levels of
	 * fallback before giving up. First, it checks the static command list.
	 * If it doesn't find anything there, it checks the local list of alternate
	 * commands. If that doesn't work, it attempts to complete the command.
	 * If none of those work, it returns the "bad" command.
	 * @param cmd
	 * @return The Command object corresponding to the string name, or the Bad command if nothing is found.
	 */
	private Command lookup(String cmd) {
		//First try direct lookup.
		Command comm = commands.get(cmd);
		
		//Next try alternate commands
		if (comm == null) {
			String altCmd = alternateCommands.get(cmd);
			if (altCmd != null)
				comm = commands.get(altCmd);
			
			//Next try command completion.
			if (comm == null) {
				comm = completeCommand(cmd);
			}
		}
		
		if (comm == null) {
			return new Bad();
		}
		else {
			return comm;
		}
	}
	
	/**
	 * Parses a command string. Currently, this just splits up the
	 * command by spaces.
	 * @param command
	 * @return A String array, with each token being a word split on spaces.
	 */
	private String[] parseCommandString(String command) {
		return command.split(" ");
	}
	

	/**
	 * Sends a command to this CommandHandler. This method assumes that
	 * the CommandSender was the one to send this command. Technically, it
	 * is possible to have an external entity send a command to a any CommandHandler,
	 * but only if they can actually access that handler. The actual execution of the
	 * command is synchronized on the command sender. Some commands may also synchronize
	 * on other objects in order to ensure atomicity.
	 * @param command
	 * @return the CommandResult containing results of the command.
	 */
	public void sendCommand(String command) {
		String[] parsedCmd = parseCommandString(command);
		
		//Make sure we have something to parse.
		if (parsedCmd.length > 0) {
			Command cmd = lookup(parsedCmd[0]);
			CommandParser parser = parserMap.get(cmd);
			
			CommandArguments params;
			try {
				params = parser.parse(sender, command);
				handleCommand(cmd, params);
			}
			catch (CommandParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else {
			//Else there was a space typed... just send a blank
			//command result.
			CommandResult res = new CommandResult();
			res.send();
		}
	}
	
	/**
	 * Invokes a Command with the specified parameters. Synchronizes on
	 * command sender. Individual commands may be further synchronized
	 * if necessary.
	 * @param cmd
	 * @param params
	 * @return the result of the command.
	 */
	private void handleCommand(Command cmd, CommandArguments params) {
		synchronized (sender) {
			try {
				cmd.execute(sender, params);
			}
			catch (RuntimeException e) {
				log.severe("There was a runtime exception executing the command " + cmd + " for sender " + sender + ":");
				e.printStackTrace();
				CommandResult cr = new CommandResult();
				cr.setFailText("There was an error: " + e.toString());
				cr.send();
			}
		}
	}

	/**
	 * This method returns a command name based on a fragment given to it. It
	 * returns the first command found in the set. If no suitable command can be
	 * found, it returns null.
	 */
	private Command completeCommand(String fragment) {
		//if the fragment is only 1 letter, it's not something we should bother
		//looking up; there is TOO much ambiguity. 1 letter commands are also
		//registered as alternate commands.
		if (fragment.length() <= 1) {
			return null;
		}

		//if the command is >= 2 letters, we can proceed with completion.
		//now, loop through all available command names
		//and see if we can find an available full command
		for (String key : commands.keySet()) {
			if (key.startsWith(fragment)) {
				return commands.get(key);
			}
		}

		return null;
	}
	
	/**
	 * Registers an alternate command with this CommandHandler. This is used to
	 * implement alias functionality into the MUD, as well as have some built-in shortened
	 * commands that don't require full command lookup (i.e. "n" --> "north").
	 * @param origCmd the original long form of the command.
	 * @param newCmd the aliased command.
	 * @return true if successful, false otherwise.
	 */
	public boolean registerAlternateCommand(String origCmd, String newCmd) {
		return (alternateCommands.put(newCmd, origCmd) == null);
	}

}
