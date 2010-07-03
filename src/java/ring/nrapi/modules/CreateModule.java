package ring.nrapi.modules;

import ring.main.RingModule;
import ring.persistence.ExistDB;

public class CreateModule implements RingModule {
	@Override
	public void execute(String[] args) {
		System.out.println("Clearing database...");
		try {
			ExistDB db = new ExistDB();
			db.removeAllResources();
			//db.createRingDatabase();
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