package ring.aspects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.comms.Communicator;
import ring.mobiles.senses.DepictionHandler;
import ring.mobiles.senses.SensesGroup;
import ring.mobiles.senses.handlers.CommandResultHandler;
import ring.mobiles.senses.StimulusSender;
import ring.players.Player;
import ring.server.shells.PlayerShell;
import ring.intermud3.Intermud3Client;

/**
 * This aspect deals with routing Communicator objects to various places
 * in the system. For example, CommandResult objects need a Communicator
 * in order to send data back to the client. However, they do not normally
 * have access to an existing Communicator at their scope. This aspect
 * provides CommandResults with access to Communicators. Each {@link ring.server.shells.PlayerShell} 
 * has its own routing aspect that takes care of this cross-cutting concern.
 * <br/><br/>
 * This aspect enables "magic code" in the classes that it affects. Commands
 * need not be aware of who is executing the command in order to send data back
 * to the client, for example. This magic even extends to Python classes
 * dynamically compiled at runtime.
 * @author projectmoon
 *
 */
public privileged aspect CommunicatorRouting percflow(call(void PlayerShell.run())) {
	//Shell to scope command results under
	private PlayerShell shell;
		
	//Communicator to route.
	private Communicator comms;

	//Used by several pointcuts.
	private CommandResult result;
	
	//Used for senses group routing.
	private DepictionHandler oldHandler;
	
	/**
	 * Identifies the game loop of the specified {@link PlayerShell}.
	 * This allows us to pull in the communicator object of the PlayerShell.
	 * @param shell The shell to pull the communicator from.
	 */
	pointcut gameLoopOf(PlayerShell shell):
		call(void PlayerShell.gameLoop()) && 
		this(shell);
	
	before(PlayerShell shell): gameLoopOf(shell) {
		System.out.println("Found comms: " + shell.comms);
		this.comms = shell.comms;
		this.shell = shell;
	}
	
	/**
	 * Pointcut that matches whenever a CommandResult is created within the scope of the
	 * specified PlayerShell.
	 * @param result The {@link CommandResult} to route the communicator to.
	 */
	pointcut createCommandResult(CommandResult result):
		execution(public CommandResult.new(..)) && this(result);
		
		
	pointcut withinCommandFlow():
		cflow(call(public void CommandSender.doCommand(String)));
	
	/**
	 * Route Communicators to CommandResults for when commands are executed. This
	 * advice operates after a CommandResult has been constructed.
	 */
	after(CommandResult cr): createCommandResult(cr) && withinCommandFlow() {
		cr.setCommunicator(comms);
		
		//This allows the result to be used by other pointcuts.
		//Not all will necessarily use it, however.
		this.result = cr;
	}
	
	/**
	 * Pointcut that matches whenever a sense stimulus is consumed within the context of a
	 * command. This overrides the default {@link DepictionHandler} for the senses group and instead
	 * uses a {@link CommandResultHandler}. It explicitly excludes calls to {@link StimulusSender} because
	 * those mobiles need to use their default handlers rather than command result handlers. Only
	 * the sender of the command needs to use a command result handler.
	 * @param group The SensesGroup to override.
	 */
	pointcut consumeSenseInCommand(SensesGroup group):
		(call(public void SensesGroup.consume(..)) && target(group)) &&
		!withincode(void StimulusSender.sendStimulus(..)) &&
		cflow(call(public void CommandSender.doCommand(String)));
	
	before(SensesGroup group): consumeSenseInCommand(group) {
		CommandResultHandler handler = new CommandResultHandler(result);
		
		oldHandler = group.getDepictionHandler();
		group.setDepictionHandler(handler);
	}
	
	after(SensesGroup group): consumeSenseInCommand(group) {
		group.setDepictionHandler(oldHandler);
	}
}
