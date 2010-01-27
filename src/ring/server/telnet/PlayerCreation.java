package ring.server.telnet;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import ring.players.Player;
import ring.server.Communicator;
import ring.util.UserUtilities;

import org.apache.commons.codec.binary.Hex;

/**
 * Class to create a new player object. A user logs in as a player and then
 * chooses a character.
 * 
 * @author projectmoon
 * 
 */
public class PlayerCreation {
	private Communicator comms;
	private String username;
	private String pw;

	public PlayerCreation(String username, String pw) {
		this.username = username;
		this.pw = pw;
	}

	public Player doCreatePlayer() {
		Player player = new Player();

		player.setName(username);
		player.setPassword(UserUtilities.sha1Hash(pw));
		player.setID(username);
		player.setLastLogon(new Date());
		
		return player;
	}
}
