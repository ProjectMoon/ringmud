package ring.nrapi.business;

import org.xmldb.api.base.XMLDBException;

import ring.main.RingModule;
import ring.nrapi.players.Player;
import ring.nrapi.players.PlayerCharacter;
import ring.persistence.DataStore;
import ring.persistence.DataStoreFactory;
import ring.persistence.ExistDB;

//TODO implement RefListener for referential object logic
public class TestDriver implements RingModule {
	@Override
	public void execute(String[] args) {
		System.out.println("Setting up database.");
		ExistDB db = new ExistDB();
		try {
			db.removeAllResources();
			db.createRingDatabase();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Player p = new Player();
		p.setID("aUser");
		PlayerCharacter pc = new PlayerCharacter();
		pc.getBaseModel().setName("Bob");
		pc.getBaseModel().setDescription("This guy is awesome.");
		
		p.addCharacter(pc);
		
		System.out.println("Storing player and character");
		DataStore ds = DataStoreFactory.getDefaultStore();
		ds.storePersistable(p);
		ds.storePersistable(pc);
		
		System.out.println("Reading back out");
		Player p2 = ds.retrievePlayer("aUser");
		PlayerCharacter pc2 = p2.getCharacter("Bob");
		
		System.out.println("p2 id = " + p2.getID());
		System.out.println("pc2 id = " + pc2.getID());
		System.out.println("pc2 name = " + pc2.getBaseModel().getName());
		
	}
	
	public static void main(String[] args) throws XMLDBException {
		new TestDriver().execute(null);
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}
}
