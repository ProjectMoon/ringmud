package ring.installer;

/**
 * An installer for the MUD. This installer sets up the directory structure, samples, etc
 * for the mud. Generally, one implementation exists for each OS.
 * @author projectmoon
 *
 */
public interface Installer {
	public void createConfigDirectory() throws InstallationException;
	public void copyDefaultConfig() throws InstallationException;
	
	/**
	 * This method is a bit different than the others in that it returns
	 * a boolean value indicating whether or not the database was actually
	 * connected to. Needed for the MUD to shut down the database if it needs to.
	 * @return true or false
	 * @throws InstallationException
	 */
	public boolean setUpDatabase() throws InstallationException;
	public void finish() throws InstallationException;
}
