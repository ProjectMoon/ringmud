package ring.compiler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ring.main.RingModule;

import java.io.*;
import java.util.*;

/**
 * Provides methods for packing and unpacking RingMUD .mud files. A .mud file
 * is actually a fancy zip file, similar to a Java jar file. A compiled MUD
 * can be deployed to the existing MUD server, or run in "development" mode, where
 * the database is not used. Instead, everything will be loaded into memory so the
 * MUD can be tested easily. 
 * @author projectmoon
 *
 */
public class Compiler implements RingModule {
	private String mudProjectDir;
	
	private int errorCount;
	private int stripIndex;
	private MUDFile mudFile = null;
	
	@Override
	public void execute(String[] args) {
		//Read some options
		
		System.out.println("Compiling mud in " + args[0] + "...");
		
		mudProjectDir = args[0];
		//Validate that necessary files exist.
		validateFileExistence();
		
		//Create the temporary mud file.
		try {
			mudFile = generateFile(args[0]);
		}
		catch (IOException e) {
			System.err.println("Could not generate mudfile: " + e);
			return;
		}
		
		//Validate mud properties.
		validateProperties();
		
		//Convert data/*.py to mud entries
		//TODO run some scripting here?
		
		//Validate data/*.xml
		validateDocuments();
		
		//Validate ID uniqueness.
		validateIDs();
		
		//Validate python?
		
		if (errorCount == 0) {
			outputFile();
		}
		else {
			System.out.println();
			System.out.println(errorCount + " error(s) found. Please fix them and recompile.");
		}
		
	}

	@Override
	public boolean usesDatabase() {
		return false;
	}
	
	/**
	 * The following files MUST be present for a mud to actually run:
	 * ./mud.properties
	 *
	 */
	private void validateFileExistence() {
		String propsPath = mudProjectDir + File.separator + "mud.properties";
		File propsFile = new File(propsPath);

		if (!propsFile.exists()) {
			severeError("mud.properties does not exist in directory.");
		}
	}
	
	private void validateProperties() {
		String mudName = mudFile.getName();
		String mudVersion = mudFile.getVersion();
		
		//Name and version cannot be blank.
		if (mudName.equals("")) {
			error("mud.properties", "You must specify the \"name\" property.");
		}
		if (mudVersion.equals("")) {
			error("mud.properties", "You must specify the \"version\" property.");
		}
	}
	
	private void validateDocuments() {
		for (FileEntry entry : mudFile.getEntries("data")) {
			try {
				//Only operate on XML documents.
				if (entry.getEntryName().endsWith(".xml")) {
					DocumentValidator dv = new DocumentValidator();
					if (!dv.validate(entry.getFile())) {
						List<ValidationError> errors = dv.getErrors();
						
						for (ValidationError error : errors) {
							error(entry.getEntryName(), error);	
						}
						
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void validateIDs() {
		try {
			Set<String> duplicateChecker = new HashSet<String>();
			
			for (FileEntry entry : mudFile.getEntries("data")) {
				if (entry.getEntryName().endsWith(".xml")) {
					IDFinder finder = new IDFinder();
					XMLReader parser = XMLReaderFactory.createXMLReader();
					parser.setContentHandler(finder);
					FileInputStream input = new FileInputStream(entry.getFile());
					InputSource src = new InputSource(new BufferedInputStream(input));
					parser.parse(src);
					
					for (String id : finder.getIDs()) {
						if (!duplicateChecker.add(id)) {
							error(entry.getEntryName(), "Duplicate object ID: " + id);
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Generates an error message, but signifies that the mud compiler should continue
	 * in order to possibly find more errors.
	 * @param scope The place where the error occurred. Generally a filename.
	 * @param message
	 */
	private void error(String scope, String message) {
		System.out.println();
		System.out.println(scope + ": " + message);
		errorCount++;
	}
	
	/**
	 * Overloaded error method that takes a ValidationError instead of a string for its
	 * message. This special method displays line numbers.
	 * @param scope
	 * @param message
	 */
	private void error(String scope, ValidationError message) {
		System.out.println();
		System.out.println(scope + "(" + message.getLine() + "):\n   " + message.getError());
		errorCount++;
	}	
	
	/**
	 * Generates an error message, and shuts down the JVM. A severe error means validation
	 * cannot continue.
	 * @param message
	 */
	private void severeError(String message) {
		System.out.println("Error (Severe): " + message);
		System.exit(1);
	}
	
	public void outputFile() {
		try {
			String dir = System.getProperty("user.dir");
			
			//Generate the default filename, with all spaces stripped.
			String filename = mudFile.getName();
			filename += "-" + mudFile.getVersion() + ".mud";
			filename.replaceAll(" ", "");
			
			FileOutputStream out = new FileOutputStream(dir + File.separator + filename);
			mudFile.writeTo(out);
			System.out.println("Wrote \"" + filename + "\"");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MUDFile generateFile(String path) throws IOException {
		File dir = new File(path).getCanonicalFile();
		stripIndex = dir.getPath().length() + File.separator.length();
		
		MUDFile mudFile = new MUDFile();
		
		File[] files = dir.listFiles();
		
		for (File file : files) {
			file = file.getCanonicalFile();
			if (file.isDirectory()) {
				List<FileEntry> entries = generateEntries(file);
				for (FileEntry entry : entries) {
					mudFile.addEntry(entry);
				}
			}
		}
		
		Properties props = new Properties();
		props.load(new FileInputStream(dir.getPath() + "/mud.properties"));
		mudFile.setProperties(props);
		
		return mudFile;
		
	}
	
	public List<FileEntry> generateEntries(File dir) throws IOException {
		List<FileEntry> entries = new ArrayList<FileEntry>();
		for (File file : dir.listFiles()) {
			file = file.getCanonicalFile();
			
			if (file.isDirectory()) {
				entries.addAll(generateEntries(file));
			}
			else {
				FileEntry entry = new FileEntry(file);
				String prefix = file.getPath().substring(stripIndex);
				entry.setEntryName(prefix);
				entries.add(entry);
			}
		}
		
		return entries;
	}
}
