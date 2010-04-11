package ring.server.telnet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import ring.comms.Communicator;
import ring.comms.telnet.TelnetInputStream;
import ring.comms.telnet.TelnetOutputStream;
import ring.comms.telnet.TelnetStreamCommunicator;
import ring.server.MUDConnectionManager;
import ring.server.MUDConnectionTimeout;
import ring.server.shells.PlayerLoginShell;
import ring.server.shells.PlayerShell;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class TelnetShell implements Shell {
	private Connection connection;
	private Communicator comms;
	
	public static Shell createShell() {
		return new TelnetShell();
	}

	@Override
	public void run(Connection conn) {
		init(conn);
		
		//Clear screen
		try {
			conn.getTerminalIO().eraseScreen();
			conn.getTerminalIO().homeCursor();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Delegate to PlayerLoginShell.
		new PlayerLoginShell(connection.getConnectionData().getInetAddress(), comms).run();
		
		//Note: the login shell always takes care of setting up the player, assuming it needs to happen.
		//Otherwise, the player is cached by the mud connection manager.
		
		//Then delegate to PlayerShell
		new PlayerShell(connection.getConnectionData().getInetAddress(), comms).run();
		
	}
	
	private void init(Connection conn) {
		connection = conn;
		connection.addConnectionListener(this);
		
		//Clear out the timer if they have one
		MUDConnectionManager.deleteTimer(connection.getConnectionData().getInetAddress());
				
		//Initialize the communicator.
		TelnetStreamCommunicator c = new TelnetStreamCommunicator(new TelnetInputStream(connection.getTerminalIO()),
				new TelnetOutputStream(connection.getTerminalIO()));
		
		c.setEchoReturn(true);
		comms = c;
	}

	@Override
	public void connectionIdle(ConnectionEvent arg0) {
		System.out.println("Idle connection for " + connection);
		InetAddress ip = connection.getConnectionData().getInetAddress();
		TimerTask task = new MUDConnectionTimeout(ip);
		Timer timer = MUDConnectionManager.createTimer(ip);
		//timer.schedule(task, 1000);
		timer.schedule(task, 300000); //5 minutes = 300,000
		
	}

	@Override
	public void connectionLogoutRequest(ConnectionEvent arg0) {
		System.out.println("Logout request for " + connection);
	}

	@Override
	public void connectionSentBreak(ConnectionEvent arg0) {
		System.out.println("Connection break for " + connection);
	}

	@Override
	public void connectionTimedOut(ConnectionEvent arg0) {
		System.out.println("Timeout for " + connection);
	}

}
