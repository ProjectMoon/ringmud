package ring.players;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
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
	
	private transient boolean quitting;
	private transient Player controllingPlayer;

	public PlayerCharacter() {}
	
	public PlayerCharacter(String pName) {
		super.getBaseModel().setName(pName);
	}
	
	@XmlTransient
	public Player getPlayer() {
		return controllingPlayer;
	}
	
	public void setPlayer(Player player) {
		controllingPlayer = player;
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
	}

	/**
	 * Overriden to deal with player-specific options for commands.
	 * @param command
	 */
	public void doCommand(String command) {
		CommandResult res;
		// Was anything even typed?
		// If not, return null;
		if (command.length() <= 0) {
			return;
		}
		
		// Is the player locked?
		if (super.isLocked()) {
			res = new CommandResult();
			res.setFailText(super.lockMessage + " (" + super.lockTimeRemaining * 2 + " seconds left)");
			res.setSuccessful(false);
			res.send();
			return;
		}

		// Send the command.
		super.handler.sendCommand(command);
	}
	
	public String toString() {
		return getBaseModel().getName();
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
