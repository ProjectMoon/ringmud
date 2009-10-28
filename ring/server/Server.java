package ring.server;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

//MUD imports.
import ring.world.*;
import ring.main.RingModule;
import ring.players.*;

//Java imports.
import java.net.*;
import java.io.*;
import ring.system.MUDBoot;
import ring.system.MUDConfig;

public class Server implements RingModule {
	// The server socket that players connect on.
	private static ServerSocket socket;
	
	private static PlayerList playerList = new PlayerList();
	private static int currentConnections = 0;

	public Server() {}
	
	public static PlayerList getPlayerList() {
		return playerList;
	}

	public static void main(String[] args) {
		MUDConfig.loadProperties();
		new Server().start(new String[] { "-a", "localhost" });
	}
	
	public static void incrementConnections() {
		currentConnections++;
	}
	
	public static void decrementConnections() {
		currentConnections--;
	}

	public void start(String[] args) {
		String addr = "localhost";
		if (args.length > 0) { // check for options
			if (args[0].equals("-a"))
				addr = args[1];
			else
				addr = MUDConfig.getServerIP();
		}
		
		int port = MUDConfig.getServerPort();

		Socket playerSocket;

		// Boot the mud
		MUDBoot.boot();
		new World(); //no assignment because world assigns itself.

		// Bind the IP
		System.out.println("Attempting to bind to IP: " + addr + ":" + port);
		try {
			socket = new ServerSocket(port, 50, InetAddress.getByName(addr));
		} catch (IOException e) {
			System.out.println("Hit an IO road bump...");
			e.printStackTrace();
		} catch (SecurityException se) {
			System.out.println("Hit a Security road bump...");
		}

		System.out.println("Waiting on connections...");
		while (true) {
			try {
				playerSocket = socket.accept();
				System.out.println("Socket: " + playerSocket);

				System.out.println("Player connected ["
						+ playerSocket.getInetAddress() + "]");

				//only start a new login thread if the number of max connections is not
				//violated.
				if (MUDConfig.getServerMaxConnections() == 0 ||
						currentConnections + 1 < MUDConfig.getServerMaxConnections()) {
					Thread playerThread = new Thread(World.getWorld().getPlayerThreadGroup(),
							new PlayerLogon(playerSocket), "Logon "
									+ playerSocket.getInetAddress().toString());

					playerThread.setDaemon(true);
					playerThread.start();
					playerThread = null;
					incrementConnections();
				}
				else {
					System.out.println("Max number of connections reached. Closing connection for [" + playerSocket.getInetAddress() + "]");
					playerSocket.close();
				}

			} catch (InterruptedIOException iioe) {
				System.out.println("I//O interrupted!");
			} catch (IOException ioe) {
				System.out.println("Error connecting player.");
			} catch (NullPointerException e) {
				System.out.println("--------------ERROR-------------");
				// System.out.println("playerSocket:" + playerSocket);
				System.out
						.println("Fatal IP Binding Error. Shutting down the server.");
				System.out.println("socket: " + socket);
				System.out.println("--------------------------------");
				System.exit(0);
			}

			// check ticker still running - respawn if dead
			// world.checkTicker();
		}

	}

	public void stop() {
		// TODO Auto-generated method stub

	}
}
