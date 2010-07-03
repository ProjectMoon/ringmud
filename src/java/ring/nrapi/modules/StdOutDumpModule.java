package ring.nrapi.modules;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.main.RingModule;
import ring.persistence.ExistDB;

public class StdOutDumpModule implements RingModule {

	@Override
	public void execute(String[] args) {
		try {
			ExistDB db = new ExistDB();
			Collection col = db.getCollection(args[0]);
			
			for (String resName : col.listResources()) {
				XMLResource res = (XMLResource)col.getResource(resName);
				
				System.out.println(res.getDocumentId());
				System.out.println();
				System.out.println(res.getContent());
				System.out.println("--------------");
			}
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean usesDatabase() {
		return true;
	}
}
