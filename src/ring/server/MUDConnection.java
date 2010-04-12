package ring.server;

import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.world.Ticker;

/**
 * Implements information about a player's connection to the MUD. This is sort of a
 * "global" class that allows the servers to share information. So, if a player is
 * forcibly disconnected from the server, his MUDConnection will still linger for a time,
 * and will be available again if they reconnect quickly enough.
 * @author projectmoon
 *
 */
public class MUDConnection {
	private Player player;
	private PlayerCharacter pc;
	private MUDConnectionState state;
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public PlayerCharacter getPlayerCharacter() {
		return pc;
	}
	
	public void setPlayerCharacter(PlayerCharacter playerCharacter) {
		this.pc = playerCharacter;
	}
	
	public MUDConnectionState getState() {
		return state;
	}
	
	public void setState(MUDConnectionState state) {
		this.state = state;
	}
	
	public void cleanup() {
		System.out.println("Cleaning up resources for " + player);
		
		//Save player and PC
		player.save();
		pc.save();
		
		//Log the player out gracefully.
		
		//Send a message to other players at the location.
		VisualStimulus stim = new VisualStimulus();
		stim.setDepiction(pc.getBaseModel().getName() + " fades away into nothingness...");
		stim.setBlindDepiction("");
		StimulusSender.sendStimulus(pc.getLocation(), stim, pc);
		
		//Remove them from their current room.
		pc.getLocation().removeMobile(pc);
		
		//Remove them from the world ticker.
		Ticker.getTicker().removeTickerListener(pc);
	}
}
