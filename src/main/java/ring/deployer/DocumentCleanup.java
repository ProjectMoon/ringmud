package ring.deployer;

import java.util.List;

public class DocumentCleanup {
	private List<String> validDocuments;
	private int cleanupCount = 0;
	
	public DocumentCleanup(List<String> validDocuments) {
		this.validDocuments = validDocuments;
	}
	
	public void cleanup()  {
		throw new UnsupportedOperationException("Implement 'document cleanup' by changing it to db cleanup and perhaps dropping tables or entire db?");
	}
	
	public int getCleanupCount() {
		return cleanupCount;
	}
}
