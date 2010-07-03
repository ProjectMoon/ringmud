package ring.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import ring.util.UserUtilities;

/**
 * Deploys anything that's not xml.
 * @author projectmoon
 *
 */
public class RegularDeployer {
	private File documentFile;
	private DeployableFileEntry entry;
	
	//Only need to know if one file is changed.
	private static boolean codeUpdates = false;
	
	public RegularDeployer(File documentFile, DeployableFileEntry entry) {
		this.documentFile = documentFile;
		this.entry = entry;
	}
	
	public boolean codeUpdates() {
		return codeUpdates;
	}
		
	public void deploy() {
		//We have now determined that this document needs to be deployed, or overwritten.
		if (checkCodeUpdates()) {
			if (!documentFile.exists()) {
				System.out.println("Deploying new file: " + entry.getEntryName());
			}
			else {
				System.out.println("Updating file: " + entry.getEntryName());
			}
			//directoryToCreate = IMPORTROOT + entry.getEntryName()
			//if directoryToCreate.exists() == false:
			//	directoryToCreate.mkdirs()
			if (!documentFile.getParentFile().exists()) {
				documentFile.getParentFile().mkdirs();
			}
			
			writeDocumentFile();
		}
		

		
		//Copy file over via BufferedInputStream.
		//Overwrites any existing file.
	}
	
	private boolean checkCodeUpdates() {
		//Determine if a code update happened: new files and updated files means code update.
		//if (!codeUpdates):
		//	if document does not exist:
		//		codeUpdates = true.
		//	else:
		//		if shaHash(currDocument) != shaHash(incomingDocument):
		//			codeUpdates = true
		//		else:
		//			return. this document does not need to be updated.

		if (!codeUpdates) {
			if (!documentFile.exists()) {
				codeUpdates = true;
				return true;
			}
			else {
				String fileContent = getContent(documentFile);
				String entryContent = getContent(entry);
				
				if (UserUtilities.sha1Hash(fileContent).equals(UserUtilities.sha1Hash(entryContent)) == false) {
					codeUpdates = true;
					return true;
				}
				else {
					return false; //this file does not need to be updated.
				}
			}
		}
		else {
			return true;
		}
	}
	
	private void writeDocumentFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(documentFile);
			
			String[] lines = getContent(entry).split("\n");
			for (String line : lines) {
				writer.println(line);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			close(writer);
		}
	}
	
	private String getContent(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input));
			String content = "";
			String line = "";
			
			while ((line = reader.readLine()) != null) {
				content += line + "\n"; //preserve newlines.
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
	
	private String getContent(DeployableFileEntry entry) {
		BufferedReader reader = null;
		try {
			InputStream input = entry.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			String content = "";
			String line = "";
			
			while ((line = reader.readLine()) != null) {
				content += line + "\n"; //Preserve newlines
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
	
	private void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
