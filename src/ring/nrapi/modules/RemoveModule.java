package ring.nrapi.modules;

import ring.main.RingModule;
import ring.persistence.DataStoreFactory;

public class RemoveModule implements RingModule {

	@Override
	public void start(String[] args) {
		System.out.println("Beginning removal of " + args.length + " documents.");
		int docCount = 0;
		for (String docID : args) {
			System.out.print("Removing " + docID + " [" + (docCount + 1) + "/" + args.length + "]");
			if (DataStoreFactory.getDefaultStore().removeDocument(docID)) {
				docCount++;
				System.out.println(" -- Removed");
			}
			else {
				System.out.println(" -- REMOVE FAILED");
			}
		}
		
		System.out.println("Finished. " + docCount + "/" + args.length + " removed.");
	}

	@Override
	public void stop() {
		
	}

}
