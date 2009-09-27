package ring.commands.nc;

import java.util.List;
import java.util.Properties;

/**
 * Interface representing a CommandIndexer. CommandIndexers are used to load and index Command
 * objects from various sources. The two built-in indexers for RingMUD are PackageIndexer for
 * indexing commands from Java packages, and JythonIndexer for indexing commands from Jython
 * script files. It is possible to write custom implementations of this interface in order to
 * load commands from other sources such as other scripting languages or across a network.
 * @author projectmoon
 *
 */
public interface CommandIndexer {
	/**
	 * Performs the actual indexing operation.
	 * @throws IllegalStateException if index() is called before the properties have been set.
	 */
	public void index() throws IllegalStateException;
	
	/**
	 * Returns the list of commands. If the index() method has not been
	 * called, this method will automatically call it.
	 * @return The list of commands created by this indexer.
	 * @throws IllegalStateException if properties have not been set.
	 */
	public List<Command> getCommands() throws IllegalStateException;
	
	/**
	 * Sets the properties for this CommandIndexer. This serves as a unified
	 * way to pass implementation-specific parameters to each indexer.
	 * @param props
	 */
	public void setProperties(Properties props);
	
	/**
	 * Gets the properties object that was passed.
	 * @return
	 */
	public Properties getProperties();
}
