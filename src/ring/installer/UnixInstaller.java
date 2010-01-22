package ring.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.xmldb.api.base.XMLDBException;

import ring.persistence.ExistDB;
import ring.system.PreferencesManager;

public class UnixInstaller implements Installer {
	@Override
	public void copyDefaultConfig() throws InstallationException {
		System.out.println("Extracting default mud.config");
		File cfgFile = new File("/etc/ringmud/mud.config");
		InputStream defaultCfgStream = this.getClass().getClassLoader().getResourceAsStream("ring/main/default-config.properties");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(defaultCfgStream));
			PrintWriter writer = new PrintWriter(new FileWriter(cfgFile));
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
			
			writer.close();
			reader.close();
		}
		catch (FileNotFoundException e) {
			throw new InstallationException("File not found", e);
		}
		catch (IOException e) {
			throw new InstallationException(e.getMessage(), e);
		}		
	}

	@Override
	public void createConfigDirectory() throws InstallationException {
		System.out.println("Setting up a new configuration for a UNIX system.");
		System.out.println("Configuration and data files will be stored in /etc/ringmud");
		
		File configPath = new File("/etc/ringmud/");
		
		if (configPath.mkdirs()) {
			System.out.println("Created /etc/ringmud");
		}
		else {
			String msg = "/etc/ringmud already exists, or you have insufficient permissions.\n" +
			"Please delete the directory and make sure you can create it.\n" +
			"You will probably need to run this as root to create the directory.";
			
			throw new InstallationException(msg);
		}
		
	}

	@Override
	public void finish() throws InstallationException {
		System.out.println("Finishing...");
		PreferencesManager.set("ring.system.MUDConfig.configLocation", "/etc/ringmud/");
		
	}

	@Override
	public boolean setUpDatabase() throws InstallationException {
		try {
			new ExistDB().createRingDatabase();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
