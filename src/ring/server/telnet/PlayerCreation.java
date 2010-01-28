package ring.server.telnet;

import java.util.Date;

import ring.players.Player;
import ring.server.Communicator;
import ring.util.UserUtilities;

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

	public PlayerCreation(Communicator comms, String username, String pw) {
		this.comms = comms;
		this.username = username;
		this.pw = pw;
	}

	public Player doCreatePlayer() {
		String welcomeText = "It seems you are a new user. First you will need to verify your password." +
							 "Then you will be taken into character creation mode to create your first character." +
							 "When you log in to the game again, you will have a character list, " +
							 "and your newly created character will appear on it.";
			
		comms.println(welcomeText);
		
		//There is no way a password should be blank here.
		assert (pw != "");
		
		//Verify password.
		String realPassword = verifyPassword();
		
		//Create new player object and set its properties.
		Player player = new Player();

		player.setName(username);
		player.setPassword(UserUtilities.sha1Hash(realPassword));
		player.setID(username);
		player.setLastLogon(new Date());
		
		return player;
	}
	
	private String verifyPassword() {
		comms.print("Verify password: ");
		String password = comms.receiveData();
		
		if (password.equals(pw)) {
			return password;
		}
		else {
			comms.println("[B][RED]Your passwords do not match. Please try again.");
			comms.println();
			
			//Enter loop where user needs to input password.
			String verify = "";
			while (!password.equals(verify)) {
				comms.print("Password: " );
				password = comms.receiveData();
				comms.print("Verify password: ");
				verify = comms.receiveData();
			}
			
			return password;
		}
	}
}
