package ring.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipFile;

import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import ring.main.RingModule;
import ring.persistence.ExistDB;
import ring.system.MUDConfig;

public class DeployModule implements RingModule {
	private DeployableMUDFile mudFile = null;
	private boolean codeUpdates = false;
	private ExistDB db = null;
	
	public static void main(String[] args) {
		MUDConfig.loadProperties();
		String[] args2 = new String[] { "/Users/projectmoon/Programs/git/ringmud/RingMUD-1.0.2.mud" };
		new DeployModule().execute(args2);		
	}

	@Override
	public void execute(String[] args) {
		try {
			//Create DeployableMUDFile from args[0]: This is the mud to be imported.
			ZipFile zip = new ZipFile(args[0]);
			mudFile = new DeployableMUDFile(zip);
			
			//Set up database object
			ExistDB.setRootCollectionURI("db/" + mudFile.getName());
			db = new ExistDB();
						
			//Create DeployedMUD from info found in mudFile.
			DeployedMUD mud = DeployedMUDFactory.getMUD(mudFile.getName(), mudFile.getVersion());
			
			//Check hash in property file.
			//	If hashes are different || deployed mud == null:
			//		setupDirectories()
			if ((mud == null) || (mud.getHash() != mudFile.getHash())) {
				setupDirectories();
			}
			
			//Create collections for this MUD.
			createCollections();
			
			//For each entry in the set:
			//	If endsWith .xml, delegate to deployXML.
			//	Else, delegate to deploy.
			for (DeployableFileEntry entry : mudFile.getEntries()) {
				if (entry.getEntryName().endsWith(".xml") || entry.getEntryName().endsWith(".XML")) {
					deployXMLDocument(entry);
				}
				else {
					deploy(entry);
				}
			}
			
			//Call cleanUpDatabase() to remove broken references.
			cleanUpDatabase();
			
			//updateVersionFile()
			updateVersionFile();
				
			System.out.println("MUD deploy complete.");
			
			//if codeUpdates:
			//	Restart the currently running mud, or issue a warning that the MUD must be restarted.
			//if !codeUpdates:
			//	Force all MUD objects in the currently running MUD to reload themselves from the database.
			if (codeUpdates) {
				System.out.println("There were code updates. You need to restart the MUD.");
			}
			else {
				System.out.println("There were updates. You need to restart the MUD.");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	private void createCollections() throws XMLDBException {
		db.createRingDatabase();
	}

	private void setupDirectories() {
		//Create a new directory for the MUD in MUDROOT.		
		//Create a new directory for the new version of the MUD in the above directory.
		//These will silently fail if directories exist.
		String mudPath = MUDConfig.MUDROOT;
		mudPath += File.separator + "muds" + File.separator + mudFile.getName() + File.separator + mudFile.getVersion();
		System.out.println("Creating directory " + mudPath);
		File f = new File(mudPath);
		f.mkdirs();
	}
	
	private void deployXMLDocument(DeployableFileEntry entry) throws XMLDBException, SAXException, IOException {
		XMLDeployer deployer = new XMLDeployer(db, entry);
		deployer.deploy();
	}
	
	private void deploy(DeployableFileEntry entry) {
		//This deploys "everything else" that isn't XML data.
		
		//Determine if a code update happened: new files and updated files means code update.
		//if (!codeUpdates):
		//	if document does not exist:
		//		codeUpdates = true.
		//	else:
		//		if shaHash(currDocument) != shaHash(incomingDocument):
		//			codeUpdates = true
		//		else:
		//			return. this document does not need to be updated.

		//We have now determined that this document needs to be deployed, or overwritten.
		
		//directoryToCreate = IMPORTROOT + entry.getEntryName()
		//if directoryToCreate.exists() == false:
		//	directoryToCreate.mkdirs()
		
		//Copy file over via BufferedInputStream.
		//Overwrites any existing file.
	}
	
	private void cleanUpDatabase() {
		//Clean up database by removing broken references.
		//This will operate across all collections for the imported MUD.
		
		//Using XQuery, find a list of IDs that have ref=true attributes.
		//For each ID:
		//	attempt to directly load its associated object.
		//	if returned object == null:
		//		delete referential definition
		//		increment brokenRefCount
		
		//System.out.println("Removed " + brokenRefCount + " broken references.");
	}
	
	private void updateVersionFile() {
		//Write version file with "current= " + mudFile.getVersion()
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}

	public static String stripEntryPrefix(String entryName) {
		int start = entryName.lastIndexOf('/');
		return entryName.substring(start + 1);
	}
	
	public static String getContent(DeployableFileEntry entry) {
		BufferedReader reader = null;
		try {
			InputStream input = entry.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			String content = "";
			String line = "";
			
			while ((line = reader.readLine()) != null) {
				content += line;
			}
			
			return content;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			close(reader);
		}
		
		//Nothing to return
		return null;
	}
	
	private static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
