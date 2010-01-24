package ring.server.telnet;

import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;
import ring.commands.CommandResult;
import ring.movement.LocationManager;
import ring.movement.Room;
import ring.players.PlayerCharacter;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import ring.world.World;

public class PlayerShell implements Shell {
	//"System-level" things we might care about
	private Connection connection;
	private TelnetStreamCommunicator comms;
	
	//Shell variables
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
		
		//There is no way this should ever be null.
		//Login shells take care of setting it up.
		assert(mudConnection != null);
		
		player = mudConnection.getPlayerCharacter();
		
		
		//Initialize the communicator.
		comms = new TelnetStreamCommunicator(new TelnetInputStream(connection.getTerminalIO()),
				new TelnetOutputStream(connection.getTerminalIO()));
	}
	
	private void gameLoop() {
		/*
		// Start up character.
		log.info("Creating player in the world: " + getBaseModel().getName());
		World.getWorld().getTicker().addTickerListener(this, "PULSE");
		// Set location.
		Room room = (Room)LocationManager.getOrigin();
		room.addMobile(this);
		setLocation(room);
		*/

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
			
			CommandResult res = player.doCommand(command);
			
			if (res.hasReturnableData()) {
				String result = res.getText();
				comms.print(result);
			}

			// Only update last command if the last command wasn't !!
			if (!command.equals("!!"))
				lastCommand = command;

			// Necessary in case of updates to prompt info.
			comms.setSuffix(player.getPrompt());
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
