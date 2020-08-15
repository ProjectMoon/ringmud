package ring.deployer;

import ring.main.RingModule;
import ring.system.MUDConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * Deploys a mud file. MUDs are deployed when changes to their codebase or data have occurred. This
 * deployment module unpacks mud files and deploys them to the RingMUD server configuration root.
 * XML data is sent to the XML Database. During a MUD deploy, each entry in the .mud file is tested
 * for changes, and deployed accordingly. The Deployer makes use of an XMLDeployer and RegularDeployer
 * to implement these tests as well as deployment logic.
 * @author projectmoon
 *
 */
public class DeployModule implements RingModule {
	private DeployableMUDFile mudFile = null;
	private boolean codeUpdates = false;
	private boolean xmlUpdates = false;
	private String mudRoot;
	private String mudPath;
	
	//Cleanup related variables
	private List<String> deployedXMLDocuments = new ArrayList<String>();
	
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
			mudRoot = MUDConfig.MUDROOT + File.separator + "muds" + File.separator + mudFile.getName() + File.separator;
			mudPath = mudRoot + mudFile.getVersion();
			
			//Set up database object
			//TODO set up database object/connection here!
						
			//Create DeployedMUD from info found in mudFile.
			DeployedMUD mud = DeployedMUDFactory.getMUD(mudFile.getName(), mudFile.getVersion());
			
			//Add root to deployed XML docs. root is always there.
			deployedXMLDocuments.add("root");
			
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
				deploy(entry);
			}
			
			//Call cleanUpDatabase() to remove broken references and un-necessary documents.
			cleanUpDatabase();
			
			//updateVersionFile()
			updateVersionFile();
							
			//if codeUpdates:
			//	Restart the currently running mud, or issue a warning that the MUD must be restarted.
			//if !codeUpdates:
			//	Force all MUD objects in the currently running MUD to reload themselves from the database.
			if (codeUpdates && xmlUpdates) {
				System.out.println("There were data and code updates. You need to restart the MUD.");
			}
			else if (codeUpdates) {
				System.out.println("There were code updates. You need to restart the MUD.");
			}
			else if (xmlUpdates) {
				System.out.println("There were data updates. You need to restart the MUD.");
			}
			else {
				System.out.println("No changes detected in this deploy.");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Discrete step for setting up the database for this deployment.
	 */
	private void createCollections() {
		throw new UnsupportedOperationException("Create 'collections' or maybe we call them tables");
	}

	/**
	 * Discrete step for setting up deployment directories if necessary.
	 */
	private void setupDirectories() {
		//Create a new directory for the MUD in MUDROOT.		
		//Create a new directory for the new version of the MUD in the above directory.
		//These will silently fail if directories exist.
		File f = new File(mudPath);
		
		if (f.mkdirs()) {
			System.out.println("Creating directory " + mudPath);
		}
	}
	
	/**
	 * Deploys something that isn't an XML document. Delegates to RegularDeployer.
	 * @param entry
	 */
	private void deploy(DeployableFileEntry entry) {
		//This deploys "everything else" that isn't XML data.
		String fullPath = mudPath + File.separator + stripMudPrefix(entry.getEntryName());
		File file = new File(fullPath);
		
		RegularDeployer deployer = new RegularDeployer(file, entry);
		deployer.deploy();
		if (!codeUpdates) {
			codeUpdates = deployer.codeUpdates();
		}
	}
	
	/**
	 * Discrete step to clean up the database of deleted XML documents and
	 * broken references.
	 */
	private void cleanUpDatabase() {
		//Remove old documents:
		//Delegate to DocumentCleanup.
		DocumentCleanup cleanup = new DocumentCleanup(deployedXMLDocuments);
		cleanup.cleanup();
		if (cleanup.getCleanupCount() > 0) {
			System.out.println("Removed " + cleanup.getCleanupCount() + " old XML document(s).");
		}
		
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
	
	private void updateVersionFile() throws FileNotFoundException {
		//Write version file with "current= " + mudFile.getVersion()
		String path = mudRoot + "versions";
		PrintWriter writer = new PrintWriter(path);
		writer.println("current=" + mudFile.getVersion());
		writer.close();
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}
	
	private String stripMudPrefix(String entryName) {
		int start = entryName.indexOf("mud/") + "mud/".length() - 1;
		return entryName.substring(start + 1);
	}
}
