package ring.server;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of MUDConnections. The different server shells access this manager to
 * check if there is an existing connection to the MUD for the user. This allows for
 * restoring of a player's state if the player was forcibly disconnected, for example.
 * @author projectmoon
 *
 */
public class MUDConnectionManager {
	//Synchronized because multiple threads will potentially be writing to this map at the same time. 
	private static Map<InetAddress, MUDConnection> connections = Collections.synchronizedMap(new HashMap<InetAddress, MUDConnection>());
	
	/**
	 * Gets a connection.
	 * @param ip
	 * @return The specified connection, or null if one exists.
	 */
	public static MUDConnection getConnection(InetAddress ip) {
		return connections.get(ip);
	}
	
	/**
	 * Adds a connection to the map of connections.
	 * @param ip
	 * @param conn
	 */
	public static void addConnection(InetAddress ip, MUDConnection conn) {
		connections.put(ip, conn);
	}
	
	/**
	 * Removes a connection.
	 * @param ip
	 * @return true if successful, false otherwise.
	 */
	public static boolean removeConnection(InetAddress ip) {
		return (connections.remove(ip) != null);
	}
	
	
}