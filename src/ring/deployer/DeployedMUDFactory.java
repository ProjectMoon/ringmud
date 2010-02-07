package ring.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import ring.system.MUDConfig;

/**
 * Factory class for getting instances of already-deployed MUDs.
 * @author projectmoon
 *
 */
public class DeployedMUDFactory {
	public static DeployedMUD getMUD(String name, String version) {
		String mudPath = MUDConfig.MUDROOT;
		mudPath += File.separator + name + File.separator + version;
		
		DeployedMUD mud = new DeployedMUD(mudPath);
		
		if (mud.exists()) {
			return mud;
		}
		else {
			return null;
		}
	}
	
	public static DeployedMUD getMUD(String name) {
		String currentVersion = null;
		String mudPath = MUDConfig.MUDROOT + File.separator + name;
		String versionPath = mudPath + File.separator + "version";
		
		Properties versionProps = new Properties();
		FileInputStream stream = null; 
		
		try {
			stream = new FileInputStream(versionPath);
			versionProps.load(stream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		currentVersion = versionProps.getProperty("current", null);
		
		if (currentVersion != null) {
			mudPath += File.separator + currentVersion;
			DeployedMUD mud = new DeployedMUD(mudPath);
			
			if (mud.exists()) {
				return mud;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
}
