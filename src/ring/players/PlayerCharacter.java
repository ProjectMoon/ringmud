package ring.players;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.server.Communicator;
import ring.world.TickerEvent;
import ring.world.TickerListener;

@XmlRootElement(name = "playerCharacter")
/**
 * A class representing a PlayerCharacter in the world. This particular version
 * of Mobile implements client-server communication by having a Communicator.
 * 
 * @author projectmoon
 * 
 */
public class PlayerCharacter extends Mobile implements CommandSender, TickerListener {
	private static final long serialVersionUID = 1L;
	
	//Player-server connection reference
	private Communicator communicator;
	
	// Other variables
	private transient boolean quitting;
	private String password;

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
	 * Returns the prompt for this player.
	 * @return The prompt.
	 */
	@XmlTransient
	public String getPrompt() {
		return "\n\n[B][GREEN]HP: " + getCombatModel().getCurrentHPString() + "/"
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
			communicator.print(super.lockFinishedMessage);
		}
	}

	/**
	 * Overriden to deal with player-specific options for commands.
	 * @param command
	 */
	public CommandResult doCommand(String command) {
		CommandResult res;
		// Was anything even typed?
		// If not, return a blank anonymous result.
		if (command.length() <= 0) {
			res = new CommandResult();
			res.setText("");
			res.setSuccessful(true);
			return res;
		}
		
		// Is the player locked?
		if (super.isLocked()) {
			res = new CommandResult();
			res.setFailText(super.lockMessage + " (" + super.lockTimeRemaining * 2 + " seconds left)");
			res.setSuccessful(false);
			return res;
		}

		// Send the command.
		res = super.handler.sendCommand(command);

		return res;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
