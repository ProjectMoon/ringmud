package ring.installer;

/**
 * An installer for the MUD. This installer sets up the directory structure, samples, etc
 * for the mud. Generally, one implementation exists for each OS.
 * @author projectmoon
 *
 */
public interface Installer {
	/**
	 * Performs the initial setup of where to put configuration data.
	 * This method should ask the user where to install.
	 * @throws InstallationException
	 */
	public void createConfigDirectory() throws InstallationException;
	
	/**
	 * Copies the default configuration file from the jar to the location
	 * determined in createConfigDirectory().
	 * @throws InstallationException
	 */
	public void copyDefaultConfig() throws InstallationException;
	
	/**
	 * This method is a bit different than the others in that it returns
	 * a boolean value indicating whether or not the database was actually
	 * connected to. Needed for the MUD to shut down the database if it needs to.
	 * @return true or false
	 * @throws InstallationException
	 */
	public boolean setUpDatabase() throws InstallationException;
	
	/**
	 * Performs any leftover steps, such as setting the preferences
	 * manager location of the config.
	 * @throws InstallationException
	 */
	public void finish() throws InstallationException;
}
