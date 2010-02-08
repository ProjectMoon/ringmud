package ring.compiler;

import java.io.File;

/**
 * Abstract superclass for an entry in a .mud file. Currently two
 * entries are supported: python files and xml files. Python is generally
 * for game code, and the XML files are for data. However, Python can be used
 * to describe game data by using special code to generate XML.
 * @author projectmoon
 *
 */
public class FileEntry {
	private File filePath;
	private String entryName;
	
	public FileEntry() {}
	
	public FileEntry(File path) {
		setFile(path);
	}
	
	public FileEntry(File path, String entryPrefix) {
		setFile(path);
		setEntryName(entryPrefix);
	}
	
	public File getFile() {
		return filePath;
	}
	
	public void setFile(File file) {
		if (!isValid(file)) {
			throw new IllegalArgumentException("Files must be XML or Python.");
		}
		
		filePath = file;
	}
	
	private boolean isValid(File file) {
		return (file.getName().endsWith(".xml") || file.getName().endsWith(".py"));
	}
	
	public String getEntryName() {
		return entryName;
	}
	
	public void setEntryName(String name) {
		entryName = name;
	}
	
	public String toString() {
		return filePath.getPath();
	}
}
