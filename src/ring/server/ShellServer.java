package ring.server;

/**
 * Interface for new server system. This interface is implemented by classes that wish
 * to use an external server system (a telnet or SSH library, for example) to manage
 * MUD connections.
 * @author projectmoon
 *
 */
public interface ShellServer {
	public void acceptConnection();
	public void start();
	public void stop();
	public void incrementConnections();
	public void decrementConnectins();
}
