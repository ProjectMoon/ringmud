package ring.compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.python.util.PythonInterpreter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ring.main.RingModule;
import ring.nrapi.xml.XMLConverter;
import ring.persistence.Persistable;

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
	
	public static void main(String[] args) {
		new Compiler().execute(new String[] { "/Users/projectmoon/muddy/" });
	}
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
		
		//Convert data/*.py to XML
		convertPython();
		
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
	
	private void convertPython() {
		try {
			PythonInterpreter interp = new PythonInterpreter();
			List<FileEntry> entriesToRemove = new ArrayList<FileEntry>();
			List<FileEntry> entriesToAdd = new ArrayList<FileEntry>();
			
			for (FileEntry entry : mudFile.getEntries("data")) {
				if (entry.getEntryName().endsWith(".py")) {
					//Execute script
					interp.execfile(new FileInputStream(entry.getFile()));
					interp.cleanup();
					
					//Then convert to XML
					List<Persistable> persistables = XMLConverter.getPersistables();
					
					//More efficient to have one string, rather than create it
					//in each iteration below. This will be the file name of the python file,
					//minus the .py extension.
					String fileRoot = entry.getEntryName();
					fileRoot = fileRoot.substring(fileRoot.lastIndexOf("/"), fileRoot.length() - 3);
					
					//Generate a unique document in the form pythonFileName + "-" + persistable.getID() + ".xml"
					for (Persistable persistable : persistables) {
						String prefix = fileRoot + "-" + persistable.getID();

						File tmp = File.createTempFile(prefix, ".xml");
						FileOutputStream fileOut = new FileOutputStream(tmp);
						BufferedOutputStream buffer = new BufferedOutputStream(fileOut);
						PrintStream out = new PrintStream(buffer);
						
						String xml = persistable.toXMLDocument();
						out.println(xml);
						
						out.close();
						buffer.close();
						fileOut.close();
						
						//Add this entry to the list of entries to remove
						//and create a new entry to be added to the mudfile.
						entriesToRemove.add(entry);
						FileEntry fe = new FileEntry();
						fe.setFile(tmp);
						fe.setEntryName("data/" + prefix + ".xml");
						entriesToAdd.add(fe);
					}
	
					XMLConverter.clear();
				}
			}
			
			//Modify the mudfile to remove the python data files and replace them with the
			//generated xml files.
			mudFile.getEntries().removeAll(entriesToRemove);
			mudFile.getEntries().addAll(entriesToAdd);
		}
		catch (IOException e) {
			e.printStackTrace();
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
