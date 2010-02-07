package ring.deployer;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeployableFileEntry {
	private ZipEntry entry;
	private ZipFile parentFile;
	
	public DeployableFileEntry() {}
	
	public DeployableFileEntry(ZipFile file, ZipEntry entry) {
		this.parentFile = file;
		setEntry(entry);
	}
	
	public String getEntryName() {
		return entry.getName();
	}
	
	public ZipEntry getZipEntry() {
		return entry;
	}
	
	public void setEntry(ZipEntry entry) {
		this.entry = entry;
	}
	
	public String toString() {
		return getEntryName();
	}
	
	public InputStream getInputStream() throws IOException {
		return parentFile.getInputStream(getZipEntry());
	}
}
