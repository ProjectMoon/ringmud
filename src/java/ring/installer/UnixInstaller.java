package ring.installer;

import java.io.BufferedReader;
import java.io.Console;
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
	private String DEFAULT_INSTALL_LOCATION = "/etc/ringmud/";
	private String installLocation;
	
	@Override
	public void createConfigDirectory() throws InstallationException {
		System.out.print("Directory to install RingMUD [" + DEFAULT_INSTALL_LOCATION + "]: ");
		installLocation = ensureSlash(readLine(DEFAULT_INSTALL_LOCATION));
		
		System.out.println("Setting up a new installation for a UNIX system.");
		System.out.println("Configuration and data files will be stored in " + installLocation);
		
		File configPath = new File(installLocation);
		
		if (configPath.mkdirs()) {
			System.out.println("Created " + installLocation);
		}
		else {
			String msg = installLocation + " already exists, or you have insufficient permissions.\n" +
			"Please delete the directory and make sure you can create it.";
			
			throw new InstallationException(msg);
		}
		
	}
	
	@Override
	public void copyDefaultConfig() throws InstallationException {
		System.out.println("Copying default mud.config");	
		File cfgFile = new File(installLocation + "mud.config");
		
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
	public void finish() throws InstallationException {
		System.out.println("Finishing...");
		PreferencesManager.set("ring.system.MUDConfig.configLocation", installLocation);
		
	}

	@Override
	public boolean setUpDatabase() throws InstallationException {
		try {
			Console console = System.console();
			
			System.out.print("eXist DB URI [default: xmldb:exist://localhost:8080/exist/xmlrpc/]: ");
			String uri = console.readLine();
			if (uri.equals("")) uri = "xmldb:exist://localhost:8080/exist/xmlrpc/";
			System.out.print("eXist DB username: ");
			String user = console.readLine();
			
			System.out.print("eXist DB password: ");
			String password = console.readLine();
				
			new ExistDB(uri, user, password).createRingDatabase();
			
			System.out.println("Database creation complete.");
			System.out.println("NOTE: You will need to copy the username/password to mud.config!");
			System.out.println("The installer will NOT do it for you.");
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private String readLine(String defaultValue) {
		String result = System.console().readLine();
		
		if (result.equals("")) {
			result = defaultValue;
		}
		
		return result;
	}
	
	private String ensureSlash(String path) {
		if (!path.endsWith("/")) {
			path += "/"; 
		}
		
		return path;
	}
}
