package ring.deployer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeployableMUDFile {
	private Set<DeployableFileEntry> entries = new HashSet<DeployableFileEntry>();
	private final ZipFile zipFile;
	
	public DeployableMUDFile(ZipFile file) {
		this.zipFile = file;
		setEntries(createEntrySet());
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
	
	public static void main(String[] args) throws IOException {
		ZipFile file = new ZipFile("/Users/projectmoon/Programs/git/ringmud/RingMUD-1.0.2.mud");
		DeployableMUDFile mudFile = new DeployableMUDFile(file);
		
		for (DeployableFileEntry entry : mudFile.getEntries()) {
			InputStream stream = entry.getInputStream();
			InputStreamReader ir = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(ir);
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
}
