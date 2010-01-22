package ring.nrapi.modules;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ring.main.RingModule;
import ring.nrapi.xml.RingDocument;
import ring.persistence.DataStore;
import ring.persistence.DataStoreFactory;

/**
 * Imports XML documents into the database.
 * @author projectmoon
 *
 */
public class ImportModule implements RingModule {
	@Override
	public void execute(String[] args) {
		//TODO use schema validation instead of "unmarshalling validation"
		System.out.println("Beginning import of " + args.length + " documents.");
		
		DataStore ds = DataStoreFactory.getDefaultStore();
		int docCount = 0;
		for (String arg : args) {
			File file = new File(arg);
			System.out.print("Importing " + arg + " [" + (docCount + 1) + "/" + args.length + "]");
			
			if (validate(file)) {
				if (ds.importDocument(file)) {
					System.out.println(" -- Imported");
					docCount++;
				}
				else {
					System.out.println(" -- IMPORT FAILED");
				}
			}
		}
		
		System.out.println("Finished. " + docCount + "/" + args.length + " imported.");
	}

	private boolean validate(File f) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(RingDocument.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.unmarshal(f);
			return true;
		}
		catch (JAXBException e) {
			if (e.getCause() != null) {
				System.out.println(" -- VALIDATION FAILED: " + e.getCause().getMessage());
			}
			else {
				System.out.println(" -- VALIDATION FAILED: " + e);
			}
			return false;
		}
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}
	
}
