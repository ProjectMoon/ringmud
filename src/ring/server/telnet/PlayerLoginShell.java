package ring.server.telnet;

import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
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
		connection = conn;
		
		//First check for exisitng connection.
		//If so, forward directly to player shell.
		MUDConnection mudConnection = MUDConnectionManager.getConnection(connection.getConnectionData().getInetAddress());
		if (mudConnection != null) {
			connection.setNextShell("player");
			return;
		}
		else {
			doShell();
		}
		
	}
	
	private void init(Connection conn) {
		connection = conn;
		connection.addConnectionListener(this);		
		
		//Initialize the communicator.
		comms = new TelnetStreamCommunicator(new TelnetInputStream(connection.getTerminalIO()),
				new TelnetOutputStream(connection.getTerminalIO()));
	}
	
	private void doShell() {
		PlayerCharacterCreation creation = new PlayerCharacterCreation(comms);
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
