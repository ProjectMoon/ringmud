package ring.commands.nc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import ring.commands.CommandSender;
import ring.system.MUDBoot;
import ring.system.MUDConfig;

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
	//TreeMap for guaranteed entry order.
	private static Map<String, Command> commands = new TreeMap<String, Command>();
												
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
			if (cmd.getCommandName() != null) {
				commands.put(cmd.getCommandName(), cmd);
			}
			else {
				System.err.println(cmd + " has no command name. Cannot add!");
			}
		}
	}
	
	public static void main(String[] args) {
		MUDConfig.loadProperties();
		MUDBoot.loadCommands();
		Command c = commands.get("test");
		System.out.println(c);
	}
	
	/**
	 * Adds an individual String-Command relation to the command Map. This
	 * method is useful for scripting languages that extend the MUD and
	 * implement their own commands and don't have a CommandIndexer to
	 * populate the command map with.
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
	 * @return The Command object corresponding to the string name, or the Bad command if nothing is found.
	 */
	private Command lookup(String cmd) {
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
	 * Isolates the parameters of the given parsed command string by
	 * returning a String array that removes the actual command (i.e. parsedCmdString[0])
	 * from the String array.
	 * @param parsedCmdString
	 * @return A String array containing only the command parameters.
	 */
	private String[] isolateParameters(String[] parsedCmdString) {
		if (parsedCmdString.length == 1) return null;
		
		String[] params = new String[parsedCmdString.length - 1];
		
		for (int c = 1; c < parsedCmdString.length; c++) {
			params[c - 1] = parsedCmdString[c];
		}
		
		return params;
	}


	/**
	 * Sends a command to this CommandHandler. This method assumes that
	 * the CommandSender was the one to send this command. Technically, it
	 * is possible to have an external entity send a command to a any CommandHandler,
	 * but only if they can actually access that handler.
	 * @param command
	 * @return the CommandResult containing results of the command.
	 */
	public CommandResult sendCommand(String command) {
		log.info("received from [" + sender.toString() + "]: " + command);
		
		String[] parsedCmd = parseCommandString(command);
		Command cmd = lookup(parsedCmd[0]);
		CommandParameters params = new CommandParameters(isolateParameters(parsedCmd), sender);
		log.fine("Made cmd object");

		//actually do the command.
		CommandResult cr = handleCommand(cmd, params);
		log.info("handled command [" + command + "] from " + sender.toString());
		return cr;
	}

	/**
	 * This method returns a command name based on a fragment given to it. It
	 * returns the first command found in the set. If no suitable command can be
	 * found, it returns null.
	 */
	private Command completeCommand(String fragment) {
		//if the fragment is only 1 letter, it's not something we should bother
		//looking up; there is TOO much ambiguity. 1 letter commands are 
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
	 * Invokes a Command with the specified parametes.
	 * @param cmd
	 * @param params
	 * @return the result of the command.
	 */
	private CommandResult handleCommand(Command cmd, CommandParameters params) {
		return cmd.execute(sender, params);
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
