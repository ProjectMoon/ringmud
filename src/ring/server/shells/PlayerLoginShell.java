package ring.server.shells;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import ring.persistence.DataStore;
import ring.persistence.DataStoreFactory;
import ring.players.Player;
import ring.players.PlayerCharacter;
import ring.server.Communicator;
import ring.server.MUDConnection;
import ring.server.MUDConnectionManager;
import ring.server.MUDConnectionState;
import ring.server.MUDConnectionTimeout;
import ring.server.telnet.TelnetInputStream;
import ring.server.telnet.TelnetOutputStream;
import ring.server.telnet.TelnetStreamCommunicator;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class PlayerLoginShell {
	private InetAddress clientIP;
	private Communicator comms;
	
	public PlayerLoginShell(InetAddress ip, Communicator comms) {
		this.comms = comms;
		clientIP = ip;
	}
	
	public void run() {
		init();
		//First check for exisitng connection.
		//If so, forward directly to player shell.
		MUDConnection mudConnection = MUDConnectionManager.getConnection(clientIP);
		if (mudConnection != null) {
			comms.println("You are already logged in.");
			return;
		}
		else {
			try {
				displayMotd();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			MUDConnection mc = doShell();
			MUDConnectionManager.addConnection(clientIP, mc);
		}
		
	}
	
	private void displayMotd() throws IOException {
		BufferedReader reader = null;
	
		try {
			InputStream motd = this.getClass().getClassLoader().getResourceAsStream("ring/server/resources/motd.txt");
			reader = new BufferedReader(new InputStreamReader(motd));
			String line = "";
			while ((line = reader.readLine()) != null) {
				comms.println(line);
			}
			
			//Two newlines after the title.
			comms.println();
			comms.println();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private void init() {
		//Clear out the timer if they have one
		MUDConnectionManager.deleteTimer(clientIP);
	}
	
	private MUDConnection doShell() {
		DataStore ds = DataStoreFactory.getDefaultStore();
		String playerID = inputUsername();
		String password = inputPassword();
		
		Player player = ds.retrievePlayer(playerID);
		PlayerCharacter pc = null;
		
		if (player != null) {
			//Load player character list
		}
		else {
			//New user creation.
			comms.println();
			comms.println("[B]Welcome!");
			
			//Go through the player creaton process.			
			PlayerCreation nuc = new PlayerCreation(comms, playerID, password);
			player = nuc.doCreatePlayer();
			
			comms.println();
			comms.println("[R][RED]Entering new character creation mode...");
						
			PlayerCharacterCreation creation = new PlayerCharacterCreation(comms);
			pc = creation.doCreateNewCharacter();
		}
				
		MUDConnection mc = new MUDConnection();
		mc.setPlayer(player);
		mc.setPlayerCharacter(pc);
		mc.setState(MUDConnectionState.LOGGING_IN);
		
		return mc;
	}
	
	private String inputUsername() {
		String line = "";
		while (line.equals("")) {
			comms.print("Username: " );
			line = comms.receiveData();
		}
		
		return line;
	}
	
	private String inputPassword() {
		String line = "";
		while (line.equals("")) {
			comms.print("Password: " );
			line = comms.receiveData();
		}
		
		return line;
	}
}
