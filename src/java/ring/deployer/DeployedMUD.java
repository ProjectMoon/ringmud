package ring.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class representing a deployed MUD.
 * @author projectmoon
 *
 */
public class DeployedMUD {
	private Properties mudProperties = new Properties();
	
	private File location;
	
	protected DeployedMUD(String path) {
		try {
			location = new File(path).getCanonicalFile();
			if (location.exists()) {
				loadProperties();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean exists() {
		return location.exists();
	}
	
	public String getLocation() {
		return location.getAbsolutePath();
	}
	
	private void loadProperties() throws IOException {
		String propsPath = location + File.separator + "mud.properties";
		FileInputStream input = new FileInputStream(propsPath);
		mudProperties.load(input);
	}
	
	public String getName() {
		return mudProperties.getProperty("name");
	}
	
	public String getAuthor() {
		return mudProperties.getProperty("author");
	}
	
	public String getVersion() {
		return mudProperties.getProperty("version");
	}
	
	public String getHash() {
		return mudProperties.getProperty("hash");
	}
	
	public InputStream getMain() throws FileNotFoundException {
		InputStream stream = new FileInputStream(location + File.separator + "main.py");
		return stream;
	}
}
