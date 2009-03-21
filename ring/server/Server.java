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
import ring.players.*;


//Java imports.
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
  //This is the world object. Everything is stored in it.
  private static World world;

  //The server socket that players connect on.
  private static ServerSocket socket;

  public Server() {}

  public static void main(String[] args) {
  	String addr = "localhost";
  	if (args.length > 0) { //check for options
  		if (args[0].equals("-a"))
  			addr = args[1];
  		else
  			addr = "localhost";
  	}
  	
    Socket playerSocket;
    world = new World();
    System.out.println("Attempting to bind to IP: " + addr);
    try {
      socket = new ServerSocket(23, 50, InetAddress.getByName(addr));//192.168.1.6 for home -- 71.191.150.213 is the internet-visible one.
    } catch(IOException e) {System.out.println("Hit an IO road bump..."); e.printStackTrace();}
      catch(SecurityException se) {System.out.println("Hit a Security road bump...");}

    System.out.println("Waiting on connections...");
    while (true) {
            try {
              playerSocket = socket.accept();
              System.out.println("Socket: " + playerSocket);

              System.out.println("Player connected [" + playerSocket.getInetAddress() + "]");

              // create a new player thread to run the player seperately
              Thread playerThread = new Thread(world.getPlayerThreadGroup(), new PlayerLogon(playerSocket, null), "Logon " + playerSocket.getInetAddress().toString());

              playerThread.setDaemon(true);
              playerThread.start();
              playerThread = null;

            }
            catch (InterruptedIOException iioe) {System.out.println("I//O interrupted!");}
            catch (IOException ioe) {System.out.println("Error connecting player.");}
            catch (NullPointerException e) {
              System.out.println("--------------ERROR-------------");
              //System.out.println("playerSocket:" + playerSocket);
              System.out.println("Fatal IP Binding Error. Shutting down the server.");
              System.out.println("socket: " + socket);
              System.out.println("--------------------------------");
              System.exit(0);
            }

            // check ticker still running - respawn if dead
            //world.checkTicker();
        }

  }
}
