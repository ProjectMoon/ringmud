package ring.commands.nc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Package-level helper class that indexes Command objects from a given Java package.
 * @author projectmoon
 *
 */
public interface CommandIndexer {
	/**
	 * Performs the actual indexing operation.
	 */
	public void index();
		
	/**
	 * Returns the list of commands. If the index() method has not been
	 * called, this method will automatically call it.
	 * @return
	 */
	public List<Command> getCommands();
}
