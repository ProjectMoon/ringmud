package ring.nrapi.modules;

import java.io.File;

import ring.main.RingModule;
import ring.nrapi.data.DataStore;
import ring.nrapi.data.DataStoreFactory;

/**
 * Imports XML documents into the database.
 * @author projectmoon
 *
 */
public class ImportModule implements RingModule {

	@Override
	public void start(String[] args) {
		System.out.println("Beginning import of " + args.length + " documents.");
		
		DataStore ds = DataStoreFactory.getDefaultStore();
		int docCount = 0;
		for (String arg : args) {
			File file = new File(arg);
			System.out.print("Importing " + arg + " [" + (docCount + 1) + "/" + args.length + "]");
			if (ds.importDocument(file)) {
				System.out.println(" -- Imported");
				docCount++;
			}
			else {
				System.out.println(" -- IMPORT FAILED");
			}
		}
		
		System.out.println("Finished. " + docCount + "/" + args.length + " imported.");
	}

	@Override
	public void stop() {
		
	}
	
}
