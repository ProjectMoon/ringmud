package ring.deployer;

import java.util.List;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import ring.persistence.ExistDB;
import ring.persistence.ExistDBStore;

public class DocumentCleanup {
	private List<String> validDocuments;
	private int cleanupCount = 0;
	
	public DocumentCleanup(List<String> validDocuments) {
		this.validDocuments = validDocuments;
	}
	
	public void cleanup() throws XMLDBException {
		ExistDB db = new ExistDB();
		Collection col = db.getCollection(ExistDBStore.STATIC_COLLECTION);

		for (String document : col.listResources()) {
			if (!validDocuments.contains(document)) {
				Resource res = col.getResource(document);
				col.removeResource(res);
				cleanupCount++;
			}
		}
		
		col.close();
	}
	
	public int getCleanupCount() {
		return cleanupCount;
	}
}
