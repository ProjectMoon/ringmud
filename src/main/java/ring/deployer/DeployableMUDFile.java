package ring.deployer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeployableMUDFile {
	private Set<DeployableFileEntry> entries = new HashSet<DeployableFileEntry>();
	private final ZipFile zipFile;
	
	private Properties mudProperties = new Properties();
	
	public DeployableMUDFile(ZipFile file) throws IOException {
		this.zipFile = file;
		setEntries(createEntrySet());
		loadProperties();
	}
	
	public Set<DeployableFileEntry> getEntries() {
		return entries;
	}
	
	public Set<DeployableFileEntry> getEntries(String prefix) {
		Set<DeployableFileEntry> results = new HashSet<DeployableFileEntry>();
		
		for (DeployableFileEntry entry : getEntries()) {
			if (entry.getEntryName().startsWith(prefix)) {
				results.add(entry);
			}
		}
		
		return results;
	}	
	
	public void setEntries(Set<DeployableFileEntry> entries) {
		this.entries = entries;
	}
	
	private Set<DeployableFileEntry> createEntrySet() {
		Set<DeployableFileEntry> results = new HashSet<DeployableFileEntry>();
		
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		
		while(zipEntries.hasMoreElements()) {
			ZipEntry zipEntry = zipEntries.nextElement();
			DeployableFileEntry resultEntry = new DeployableFileEntry(zipFile, zipEntry);
			results.add(resultEntry);
		}
		
		return results;
	}
	
	public String getName() {
		return mudProperties.getProperty("name");
	}
	
	public String getAuthor() {
		return mudProperties.getProperty("author");
	}
	
	public String getVersion() {
		return mudProperties.getProperty("version");
	}
	
	public String getHash() {
		return mudProperties.getProperty("hash");
	}
	
	public Properties getProperties() {
		return mudProperties;
	}

	private void loadProperties() throws IOException {
		Set<DeployableFileEntry> propsEntry = getEntries("mud/mud.properties");
		
		if (propsEntry.size() > 1) {
			throw new IllegalStateException("Something is funky. This mudfile has more than one properties entry.");
		}
		else {
			//Looks a bit weird, but sets don't provide a .get method.
			for (DeployableFileEntry entry : propsEntry) {
				InputStream input = entry.getInputStream();
				mudProperties.load(input);
			}
		}
	}
}
