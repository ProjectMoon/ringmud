package ring.server.telnet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Scanner;

import ring.nrapi.players.PlayerCharacter;
import ring.server.CommunicationException;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class PlayerShell implements Shell {
	private Connection connection;
	private PlayerCharacter player;
	
	private Scanner input;
	private TelnetInputStream llInput;
	private PrintStream output;
	
	public static Shell createShell() {
		return new PlayerShell();
	}
	
	@Override
	public void run(Connection conn) {
		System.out.println("-------HI FROM PLAYER SHELL-------");
		init(conn);
		//gameLoop();
		output.print("Type something to echo: ");
		try {
			/*
			StringBuilder echo = new StringBuilder();
		
			for (int c = 0; c < 10; c++) {
				echo.append((char)(llInput.read()));
			}
			*/
			String echo = input.nextLine();
			output.println();
			output.println("Echo: " + echo.toString());
		}
		catch (Exception e) { e.printStackTrace(); }
		output.println();
		
		input.close();
		output.close();
	}
	
	/**
	 * Initializes this shell for use.
	 * @param conn
	 */
	private void init(Connection conn) {
		connection = conn;
		connection.addConnectionListener(this);		
		
		/*
		MUDConnection mudConnection = MUDConnectionManager.getConnection(connection.getConnectionData().getInetAddress());
		
		//There is no way this should ever be null.
		//Login shells take care of setting it up.
		assert(mudConnection != null);
		
		player = mudConnection.getPlayerCharacter();
		*/
		//Initialize input and output streams
		llInput = new TelnetInputStream(connection.getTerminalIO());
		input = new Scanner(llInput);
		output = new PrintStream(new TelnetOutputStream(connection.getTerminalIO()));
	}
	
	private void gameLoop() {
		player.doCommand("look");
		
		// Wait for commands.
		while (!player.isQuitting()) {
			//communicator.setSuffix(getPrompt()); <-- Needs telnetd2 eqiuvalent
			
			String cmd = input.nextLine();
			player.doCommand(cmd);
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
