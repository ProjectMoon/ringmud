package ring.daemons;

import java.io.IOException;

/**
 * An interface that allows a "daemon" to be implemented. A daemon can be pretty much
 * anything: a MUD server, a chat daemon (ex: for InterMUD), a background database process,
 * etc.
 * <br/><br/>
 * When creating a daemon, it should be written with multithreading in mind. The RingMUD engine
 * is multithreaded by nature, so when accessing mutable objects within the system, care should
 * be taken synchronize.
 * <br/><br/>
 * Most daemons run on a thread of their own. The engine provides a {@link ring.daemons.DaemonThread}
 * class to facilitate launching of daemons.
 * @author projectmoon
 *
 */
public interface Daemon {
	/**
	 * Start this daemon.
	 * @throws IOException
	 */
	public void start() throws IOException;
	
	/**
	 * Safely stop this daemon.
	 * @throws IOException
	 */
	public void stop() throws IOException;
	
	/**
	 * Immediately kill this daemon, regardless of
	 * its state.
	 * @throws IOException
	 */
	public void halt() throws IOException;
}
