package ring.server.shells;

import java.io.IOException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class SimpleShell implements Shell {

	private Connection m_Connection;
	private BasicTerminalIO m_IO;

	public void run(Connection con) {
		m_Connection = con;
		m_IO = m_Connection.getTerminalIO();
		// register the connection listener
		m_Connection.addConnectionListener(this);

		try {
			m_IO.eraseScreen(); // erase the screen
			m_IO.homeCursor(); // place the cursor in home position
			m_IO.write("Dummy Shell. Thanks for connecting.\r\n"); // some output
			m_IO.flush(); // flush the output to ensure it is sent
		}
		catch (IOException e) { e.printStackTrace(); }
	}// run

	public void connectionTimedOut(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_TIMEDOUT");
			m_IO.flush();
			// close connection
			m_Connection.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}// connectionTimedOut

	public void connectionIdle(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_IDLE");
			m_IO.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}// connectionIdle

	public void connectionLogoutRequest(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_LOGOUTREQUEST");
			m_IO.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}// connectionLogout

	public void connectionSentBreak(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_BREAK");
			m_IO.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}// connectionSentBreak

	public static Shell createShell() {
		return new SimpleShell();
	}// createShell

}// class SimpleShell
