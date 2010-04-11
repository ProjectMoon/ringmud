package ring.aspects;

import ring.players.PlayerCharacter;
import ring.server.shells.PlayerShell;
import ring.commands.Command;
import ring.commands.CommandSender;
import ring.commands.CommandResult;
import ring.commands.CommandParameters;
import ring.mobiles.senses.DepictionHandler;
import ring.mobiles.senses.SensesGroup;
import ring.mobiles.senses.handlers.CommandResultHandler;

/**
 * This aspect deals with routing Communicator objects to various places
 * in the system. For example, CommandResult objects need a Communicator
 * in order to send data back to the client. However, they do not normally
 * have access to an existing Communicator at their scope. This aspect
 * provides CommandResults with access to Communicators.
 * <br/><br/>
 * Each {@link ring.server.shells.PlayerShell} has its own routing aspect
 * that takes care of this cross-cutting concern.
 * @author projectmoon
 *
 */
public privileged aspect CommunicatorRouting percflow(within(PlayerShell)) {
	//Used by several pointcuts.
	private CommandResult result;
	
	//Used for senses group routing.
	private DepictionHandler oldHandler;
	
	
	/**
	 * Pointcut that matches whenever a CommandResult is created under the scope of the
	 * specified PlayerShell.
	 * @param shell The shell.
	 */
	pointcut createCommandResult(PlayerShell shell):
		call(public CommandResult.new(..)) &&
		cflow(call(public void CommandSender.doCommand(String)) && within(PlayerShell) && this(shell));
	
	
	/**
	 * Route Communicators to CommandResults for when commands are executed. This
	 * advice operates after a CommandResult is constructed.
	 */
	after(PlayerShell shell) returning(CommandResult cr): createCommandResult(shell) { 
		System.out.println("Command result " + cr + " was created under " + shell + " for " + shell.player);
		cr.setCommunicator(shell.comms);
		
		//This allows the result to be used by other pointcuts.
		//Not all will necessarily use it, however.
		this.result = cr;
	}
	
	/**
	 * Pointcut that matches whenever a sense stimulus is consumed within the context of a
	 * command. This overrides the default InterjectionHandler for the senses group and instead
	 * uses a CommandResultHandler.
	 * @param shell
	 * @param group
	 */
	pointcut consumeSenseInCommand(PlayerShell shell, SensesGroup group):
		(call(public void SensesGroup.consume(..)) && target(group)) &&
		cflow(call(public void CommandSender.doCommand(String)) && within(PlayerShell) && this(shell));
	
	before(PlayerShell shell, SensesGroup group): consumeSenseInCommand(shell, group) {
		System.out.println("Setting up handler for: " + shell + ", " + result + ", " + group);
		
		CommandResultHandler handler = new CommandResultHandler(result);
		
		oldHandler = group.getDepictionHandler();
		group.setDepictionHandler(handler);
	}
	
	after(PlayerShell shell, SensesGroup group): consumeSenseInCommand(shell, group) {
		System.out.println("Restoring old handler...");
		group.setDepictionHandler(oldHandler);
	}
}
