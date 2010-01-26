package ring.server.telnet;

import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;
import ring.commands.CommandResult;
import ring.movement.LocationManager;
import ring.movement.Room;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import ring.server.MUDConnectionState;
import ring.world.World;

public class PlayerShell implements Shell {
	//"System-level" things we might care about
	private Connection connection;
	private TelnetStreamCommunicator comms;
	
	//Shell variables
	private Player user;
	private PlayerCharacter player;
	private String lastCommand;
	
	public static Shell createShell() {
		return new PlayerShell();
	}
	
	@Override
	public void run(Connection conn) {
		System.out.println("-------HI FROM PLAYER SHELL-------");
		init(conn);
		gameLoop();
	}

	/**
	 * Initializes this shell for use.
	 * @param conn
	 */
	private void init(Connection conn) {
		connection = conn;
		connection.addConnectionListener(this);		
				
		MUDConnection mudConnection = MUDConnectionManager.getConnection(connection.getConnectionData().getInetAddress());
		mudConnection.setState(MUDConnectionState.PLAYING);
		
		//There is no way this should ever be null.
		//Login shells take care of setting it up.
		assert(mudConnection != null);
		
		user = mudConnection.getPlayer();
		player = mudConnection.getPlayerCharacter();
		
		System.out.println("Player: " + user + "[" + player + "]");
		
		
		//Initialize the communicator.
		comms = new TelnetStreamCommunicator(new TelnetInputStream(connection.getTerminalIO()),
				new TelnetOutputStream(connection.getTerminalIO()));
	}
	
	private void gameLoop() {
		World.getWorld().getTicker().addTickerListener(player, "PULSE");
		// Set location.
		Room room = LocationManager.getOrigin();
		room.addMobile(player);
		player.setLocation(room);
		
		//A player should see where they are when they log in.
		sendCommandResult(player.doCommand("look"));
		
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
			
			CommandResult res = player.doCommand(command);
			sendCommandResult(res);

			// Only update last command if the last command wasn't !!
			if (!command.equals("!!"))
				lastCommand = command;

			// Necessary in case of updates to prompt info.
			comms.setSuffix(player.getPrompt());
		}
	}
	
	private void sendCommandResult(CommandResult res) {
		if (res.hasReturnableData()) {
			String result = res.getText();
			System.out.println(result);
			comms.setSuffix(player.getPrompt());
			comms.print(result);
		}
	
	}

	@Override
	public void connectionIdle(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionLogoutRequest(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionSentBreak(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionTimedOut(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
