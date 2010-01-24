package ring.server;

import ring.nrapi.players.Player;
import ring.nrapi.players.PlayerCharacter;

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
	private PlayerCharacter playerCharacter;
	private MUDConnectionState state;
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public PlayerCharacter getPlayerCharacter() {
		return playerCharacter;
	}
	
	public void setPlayerCharacter(PlayerCharacter playerCharacter) {
		this.playerCharacter = playerCharacter;
	}
	
	public MUDConnectionState getState() {
		return state;
	}
	
	public void setState(MUDConnectionState state) {
		this.state = state;
	}
}
