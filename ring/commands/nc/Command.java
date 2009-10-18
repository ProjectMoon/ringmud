package ring.commands.nc;

import ring.commands.CommandSender;

/**
 * The interface representing a command in the game. The built-in
 * commands implement this interface, and scripted commands can extend
 * it. The MUD will "index" all Command objects from known locations
 * when the MUD boots. This initial index requires reflection, but
 * from that point on, command lookup is quick as a HashMap is maintained
 * for all Command objects.
 * @author projectmoon
 *
 */
public interface Command {
	/**
	 * Execute this command.
	 * @return A CommandResult describing whether or not the command succeeded, and the result
	 * text, if any.
	 */
	public CommandResult execute(CommandSender sender, CommandParameters params);
	
	/**
	 * Attempt to roll back the doings of this command in case of strange behavior or failures.
	 * Reserved for future use.
	 */
	public void rollback();
	
	/**
	 * Returns the actual command name that players would type in to use this Command.
	 * @return
	 */
	public String getCommandName();
}
