package ring.server;

import java.net.InetAddress;
import java.util.TimerTask;

import ring.players.Player;
import ring.players.PlayerCharacter;

/**
 * Class that handles a MUD connection timeout by removing the character
 * from the game world and cleaning the MUDConnectionManager's cache of 
 * the connection information. This is spawned as a thread by server implementations
 * when they receive an event that the player has disconnected or timed out. It is
 * a countdown when a player forcibly disconnects. If a player times out, this is called
 * immediately and they are removed from the game.
 * @author projectmoon
 *
 */
public class MUDConnectionTimeout extends TimerTask {
	private InetAddress ip;
	
	public MUDConnectionTimeout(InetAddress ip) {
		this.ip = ip;
	}
	
	@Override
	public void run() {
		System.out.println("Running timeout task for " + ip);
		MUDConnection conn = MUDConnectionManager.getConnection(ip);
		
		//Check null because it is possible to time out at the login screen
		//with no actual connection set up.
		if (conn != null) {
			Player player = conn.getPlayer();
			PlayerCharacter pc = conn.getPlayerCharacter();
			
			//Save player and PC
			player.save();
			pc.save();
			
			//Clear connection state.
			MUDConnectionManager.removeConnection(ip);
		}
	}

}
