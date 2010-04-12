package ring.server.shells;

import java.net.InetAddress;

import ring.comms.Communicator;
import ring.mobiles.senses.SensesGroup;
import ring.mobiles.senses.handlers.InterjectionHandler;
import ring.movement.LocationManager;
import ring.movement.Room;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import ring.server.MUDConnectionState;
import ring.world.Ticker;

public class PlayerShell {
	private Communicator comms;
	private InetAddress clientIP;
	
	//Shell variables
	private Player user;
	private PlayerCharacter player;
	private String lastCommand;
	
	public PlayerShell(InetAddress ip, Communicator comms) {
		this.comms = comms;
		clientIP = ip;
	}
	
	public void run() {
		init();
		
		comms.println("Welcome back, [B]" + user.getName() + "[R]. You descend into the world of Orea once more as [B]" + player.getBaseModel().getName() + "[R]...");
		comms.println();
		
		gameLoop();
	}

	/**
	 * Initializes this shell for use.
	 * @param conn
	 */
	private void init() {
		MUDConnection mudConnection = MUDConnectionManager.getConnection(clientIP);
		
		//There is no way this should ever be null.
		//Login shell take care of setting it up.
		assert(mudConnection != null);
		
		user = mudConnection.getPlayer();
		player = mudConnection.getPlayerCharacter();
		
		//Set location if this player is logging in normally.
		if (mudConnection.getState() == MUDConnectionState.LOGGING_IN) {
			Room room = LocationManager.getOrigin();
			room.addMobile(player);
			player.setLocation(room);
		}
		
		//This user is now playing.
		mudConnection.setState(MUDConnectionState.PLAYING);
	}
	
	private void gameLoop() {
		Ticker.getTicker().addTickerListener(player);
		
		//Set up senses group so the player can perceive the world.
		//TODO This needs to be persistable to the db.
		InterjectionHandler handler = new InterjectionHandler(comms);
		player.getDynamicModel().setSensesGroup(SensesGroup.createDefaultSensesGroup(handler));
		
		//A player should see where they are when they log in.
		comms.setSuffix(player.getPrompt());
		player.doCommand("look");
		
		// Wait for commands.
		while (!player.isQuitting()) {
			comms.setSuffix(player.getPrompt());
			
			String command = "";
			if (command.equals("!!")) {
				command = lastCommand;
			}
			else {
				command = comms.receiveData();
			}
			
			player.doCommand(command);
			
			// Only update last command if the last command wasn't !!
			if (!command.equals("!!"))
				lastCommand = command;

			// Necessary in case of updates to prompt info.
			comms.setSuffix(player.getPrompt());
		}
		
		//Log out gracefully.
		logout();
	}
	
	private void logout() {
		System.out.println(user + "[" + player + "] logged out gracefully.");
		
		//Remove them from their current room.
		player.getLocation().removeMobile(player);
		
		//Remove them from the world ticker.
		Ticker.getTicker().removeTickerListener(player);
		
		//TODO save player's current room to restore later.
		
		MUDConnectionManager.removeConnection(clientIP);
	}
}
