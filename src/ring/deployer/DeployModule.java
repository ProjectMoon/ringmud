package ring.deployer;

import ring.main.RingModule;

public class DeployModule implements RingModule {

	@Override
	public void execute(String[] args) {
		//Create DeployableMUDFile from args[0]: This is the mud to be imported.
		//Create DeployedMUD from args[0]: This is the currently imported mud, if it exists.
		
		//Check hash in property file.
		//	If hashes are different || deployed mud == null:
		//		setupDirectories()
		
		//For each entry in the set:
		//	If endsWith .xml, delegate to deployXML.
		//	Else, delegate to deploy.
		
		//Call cleanUpDatabase() to remove broken references.
		
		//updateVersionFile()
			
		//System.out.println("MUD deploy complete.");
		
		//if codeUpdates:
		//	Restart the currently running mud, or issue a warning that the MUD must be restarted.
		//if !codeUpdates:
		//	Force all MUD objects in the currently running MUD to reload themselves from the database.
		
	}
	
	private void setupDirectories() {
		//Create a new directory for the MUD in MUDROOT.
		//Create a new directory for the new version of the MUD in the above directory.
		
		//Both of the above will fail silently if directories exist.
	}
	
	private void deployXMLDocument() {
		//Determine if this document needs to be updated:
		//	Retrieve the document of the same name from the database.
		//	if shaHash(incomingDocument) == shaHash(documentInDB):
		//		return. the document does not need to be updated.
		
		//Now, we have determined that the document needs to be updated.
		
		//First, all clashes need to be handled.
		//For each ID found in this XML document:
		//	Atempt to retrieve an object from the database with this ID.
		//	If object returned is not null:
		//		boolean yes = idHasOwnDocument(id)
		//		if yes to ID has its own document:
		//			Delete that document.
		//		else:
		//			Replace definition for ID with a referential definition.
		
		//If a document with this name already exists in the DB:
		//	delete it.
		
		//Finally, import this document.
		
	}
	
	private void deploy() {
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
	
	private boolean idHasOwnDocument(String id) {
		//In order for an ID to be considered to have its own document, the following must be true:
		//1. It is a root element in the <ring> element.
		//2. It is the only root element in the <ring> element.
		
		//If one of these conditions is not met, it is considered embedded in another document.
		return true;
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

}
