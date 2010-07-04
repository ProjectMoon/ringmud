package ring.commands;

/**
 * Skeletal implementation of Command class to provide command name functionality
 * @author projectmoon
 *
 */
public abstract class AbstractCommand implements Command {
	private String commandName;

	@Override
	public abstract void execute(CommandSender sender, CommandArguments params);

	@Override
	public abstract void rollback();	
	
	@Override
	public String getCommandName() {
		return commandName;
	}


	@Override
	public void setCommandName(String name) {
		commandName = name;
	}

}
