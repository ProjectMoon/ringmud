package ring.nrapi.players;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.nrapi.mobiles.Mobile;
import ring.nrapi.movement.LocationManager;
import ring.nrapi.movement.Room;
import ring.server.CommunicationException;
import ring.server.Communicator;
import ring.world.TickerEvent;
import ring.world.TickerListener;
import ring.world.World;

@XmlRootElement(name = "playerCharacter")
/**
 * A class representing a PlayerCharacter in the world. This particular version
 * of Mobile implements client-server communication by having a Communicator.
 * 
 * @author projectmoon
 * 
 */
public class PlayerCharacter extends Mobile implements Runnable, CommandSender,
		TickerListener {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PlayerCharacter.class
			.getName());

	// Player-Server connection.
	private transient Communicator communicator;

	// Other variables
	private transient String lastCommand = null;
	private transient boolean quitting;

	public PlayerCharacter() {
		super();
	}

	public PlayerCharacter(Communicator existing, String pName) {
		communicator = existing;
		super.getBaseModel().setName(pName);
	}
	
	@Override 
	/**
	 * Explicitly overriden to make sure that PC IDs and names are the same.
	 */
	public String getID() {
		return getBaseModel().getName();
	}
	
	@Override
	/**
	 * Explicitly overriden to make sure that PC IDs and names are the same.
	 */
	public void setID(String id) {
		super.setID(id);
		getBaseModel().setName(id);
	}
	
	// processTick method.
	public synchronized void processTick(TickerEvent e) {
		super.processTick(e);
	}

	/**
	 * Starts up the player and then goes into a loop waiting for commands.
	 */
	public void run() {
		// Start up character.
		log.info("Creating player in the world: " + getBaseModel().getName());
		World.getWorld().getTicker().addTickerListener(this, "PULSE");
		// Set location.
		Room room = (Room)LocationManager.getOrigin();
		room.addMobile(this);
		setLocation(room);

		// The player has to poof into existence!
		//TODO pending NRAPI integration
		/*
		World.sendVisualToLocation(this,
						"There is a loud bang and a puff of smoke, and "
								+ getBaseModel().getName()
								+ " appears in the world once more!",
						"You hear a loud bang and smell acrid smoke. Someone has appeared in the world once more!");
		*/
		
		gameLoop();

		// Close the player's connection once their loop is done.
		// Handle either graceful quit or forced quit/disconnect.
		if (quitting && !communicator.isCommunicationError()) {
			// Save and REMOVE player from world.
			// Send quit message
			communicator.send("You have successfully quit. Good-bye.");
			communicator.close();
			log.info(this + " quit gracefully");
			//communicator.getDisconnectCallback().execute(CallbackEvent.GRACEFUL_QUIT);
			return;
		}
		else if (communicator.isCommunicationError()) {
			//TODO wait for a certain amount of time for the person to
			//come back. If so, restart their game loop.
			// Save player.
			log.info(this + " experienced disconnect/forced quit.");
			communicator.close();
			//communicator.getDisconnectCallback().execute(CallbackEvent.UNEXPECTED_QUIT);
			return;
		}
	}
	
	/**
	 * The main game loop for a player. Begins with a look command, and
	 * then waits for further commands.
	 */
	private void gameLoop() {
		doCommand("look");
		// Wait for commands.
		while (!quitting && !communicator.isCommunicationError()) {
			Thread.yield();
			communicator.setSuffix(getPrompt()); // Necessary in case of updates
													// to prompt info.
			try {
				doCommand(communicator.receiveData());
			} 
			catch (CommunicationException e) {
				log.info("There was a socket error for " + this);
				break;
			}
		}
	}

	/**
	 * Returns the prompt for this player. Protected access because only this package
	 * cares about this method.
	 * @return The prompt.
	 */
	@XmlTransient
	protected String getPrompt() {
		return "\n\n[B][GREEN]HP: " + getCombatModel().getCurrentHPString() + "/"
				+ getCombatModel().getMaxHPString() + " MV: " + getDynamicModel().getCurrentMV() + "/"
				+ getDynamicModel().getMaxMV() + " ]> [R][WHITE]";
	}

	/**
	 * Returns the same as getPrompt, but with one newline instead of two at the front.
	 * @return The prompt.
	 */
	@XmlTransient
	private String getSingleLinePrompt() {
		return "\n[B][GREEN]HP: " + getCombatModel().getCurrentHPString() + "/"
		+ getCombatModel().getMaxHPString() + " MV: " + getDynamicModel().getCurrentMV() + "/"
		+ getDynamicModel().getMaxMV() + " ]> [R][WHITE]";
	}

	/**
	 * Overriden to actually send data to the player indicating their
	 * lock is done.
	 */
	@Override
	public void decrementLockTime() {
		super.decrementLockTime();
		if (super.lockTimeRemaining <= 0) {
			communicator.send(super.lockFinishedMessage);
		}
	}

	/**
	 * Overriden to deal with player-specific options for commands.
	 * @param command
	 */
	public void doCommand(String command) {
		// Was anything even typed?
		if (command.length() <= 0) {
			communicator.setSuffix(getSingleLinePrompt());
			communicator.send("");
			communicator.setSuffix(getPrompt());
			return;
		}

		// Is the player locked?
		if (super.isLocked()) {
			communicator.send(super.lockMessage + " ("
					+ super.lockTimeRemaining * 2 + " seconds left)");
			return;
		}

		// Is the player requesting to repeat the last command?
		if (command.equals("!!"))
			command = lastCommand;

		// Send the command.
		CommandResult res = super.handler.sendCommand(command);

		if (res.getReturnData()) {
			String result = res.getText();
			communicator.send(result);
		}

		// Only update last command if the last command wasn't !!
		if (!command.equals("!!"))
			lastCommand = command;

		// Necessary in case of updates to prompt info.
		communicator.setSuffix(getPrompt()); 
	}

	/**
	 * Sends some data to the player.
	 */
	public void sendData(String data) {
		communicator.send(data);
	}

	public String toString() {
		return getBaseModel().getName();
	}

	@XmlTransient
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void setCommunicator(Communicator c) {
		communicator = c;
	}

	public void quit() {
		quitting = true;
	}

	@XmlTransient
	public boolean isQuitting() {
		return quitting;
	}

	/**
	 * Returns this player's current whotag, based on a number of different
	 * factors.
	 * 
	 * @return The whotag.
	 */
	@XmlTransient
	public String getWhoTag() {
		// "playername racename level classname zonename ethical moral <god status> <player set status> <admin set status>"
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseModel().getName()).append(' ');
		sb.append(getBaseModel().getRace().getName()).append(' ');
		sb.append(getBaseModel().getLevel()).append(' ');
		sb.append(getBaseModel().getMobileClass().getName()).append(' ');
		// sb.append(getZone()).append(' ');
		sb.append(getBaseModel().getAlignment().toNonFormattedString()).append(' ');
		sb.append(getBaseModel().getType().getName()).append(' ');

		return sb.toString().toLowerCase();
	}
}
