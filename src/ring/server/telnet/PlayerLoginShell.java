package ring.server.telnet;

import ring.persistence.DataStore;
import ring.persistence.DataStoreFactory;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import ring.server.MUDConnectionState;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class PlayerLoginShell implements Shell {
	private Connection connection;
	private TelnetStreamCommunicator comms;
	
	public static Shell createShell() {
		return new PlayerLoginShell();
	}
	
	@Override
	public void run(Connection conn) {
		init(conn);
		
		//First check for exisitng connection.
		//If so, forward directly to player shell.
		MUDConnection mudConnection = MUDConnectionManager.getConnection(connection.getConnectionData().getInetAddress());
		if (mudConnection != null) {
			connection.setNextShell("player");
			return;
		}
		else {
			MUDConnection mc = doShell();
			MUDConnectionManager.addConnection(connection.getConnectionData().getInetAddress(), mc);
			connection.setNextShell("player");
		}
		
	}
	
	private void init(Connection conn) {
		connection = conn;
		connection.addConnectionListener(this);		
		
		//Initialize the communicator.
		comms = new TelnetStreamCommunicator(new TelnetInputStream(connection.getTerminalIO()),
				new TelnetOutputStream(connection.getTerminalIO()));
	}
	
	private MUDConnection doShell() {
		DataStore ds = DataStoreFactory.getDefaultStore();
		comms.println("Enter username:");
		String playerID = comms.receiveData();
		
		Player player = ds.retrievePlayer(playerID);
		PlayerCharacter pc = null;
		
		if (player != null) {
			//Load player character list
		}
		else {
			comms.println("[R][WHITE]Entering new character creation mode...");
			comms.print("Enter a character name: ");
			String playerName = comms.receiveData();
			comms.println();
			
			PlayerCharacterCreation creation = new PlayerCharacterCreation(comms);
			pc = creation.doCreateNewCharacter(playerName);
			comms.println("sup " + pc);
		}
		
		MUDConnection mc = new MUDConnection();
		mc.setPlayer(player);
		mc.setPlayerCharacter(pc);
		mc.setState(MUDConnectionState.LOGGING_IN);
		
		return mc;
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
