package ring.server;

import java.io.IOException;

/**
 * Interface for new server system. This interface is implemented by classes that wish
 * to use an external server system (a telnet or SSH library, for example) to manage
 * MUD connections.
 * @author projectmoon
 *
 */
public interface Server {
	public void start() throws IOException;
	public void stop() throws IOException;
}
