package ring.nrapi.modules;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ring.main.RingModule;
import ring.nrapi.xml.RingDocument;
import ring.persistence.ExistDB;

/**
 * Imports XML documents into the database.
 * @author projectmoon
 *
 */
@Deprecated
public class CreateModule implements RingModule {
	@Override
	public void execute(String[] args) {
		System.out.println("Clearing database...");
		try {
			ExistDB db = new ExistDB();
			db.removeAllResources();
			db.createRingDatabase();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}
	
}

