package ring.nrapi.business;

import java.io.StringReader;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import ring.main.RingModule;
import ring.nrapi.data.DataStore;
import ring.nrapi.data.DataStoreFactory;
import ring.nrapi.data.ExistDB;
import ring.nrapi.data.Loadpoint;
import ring.nrapi.entities.Entity;
import ring.nrapi.entities.Item;
import ring.nrapi.mobiles.Body;
import ring.nrapi.mobiles.Mobile;
import ring.nrapi.mobiles.Alignment.Ethical;
import ring.nrapi.mobiles.Alignment.Moral;
import ring.nrapi.movement.Room;

//TODO implement RefListener for referential object logic
public class TestDriver implements RingModule {
	@Override
	public void start(String[] args) {
		/*
		ExistDB db = new ExistDB();
		try {
			db.removeAllResources();
			db.createRingDatabase();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Room r = new Room();
		Entity e = new Entity();
		e.setReferential(true);
		e.setID("entid");
		r.setID("room id");
		r.addEntity(e);
		r.save();
		
		try {
			Collection col = db.getCollection("static");
			System.out.println(col.getResource("room id.xml").getContent());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}	
		
		DataStore ds = DataStoreFactory.getDefaultStore();
		Room r2 = ds.retrieveRoom("room id");
		System.out.println(r2.getID());
		r2.getEntities().get(0).setID("new entid");
		r2.save();
		
		Room r3 = ds.retrieveRoom("room id");
		System.out.println(r3.getEntities().get(0).getID());
	
		try {
		Collection col = db.getCollection("static");
		
		System.out.println(col.getResource("room id.xml").getContent());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		*/
		
		/*
		Room r = DataStoreFactory.getDefaultStore().retrieveRoom("roomid");
		System.out.println("Found r: " + r);
		System.out.println("r.id = " + r.getID());
		r.getModel().setDepth(5);
		r.getModel().setDescription("A room");
		r.getModel().setTitle("A room's title");
		r.save();
		
		Room r2 = DataStoreFactory.getDefaultStore().retrieveRoom("roomid");
		System.out.println("Found r2: " + r2);
		System.out.println("r2.id = " + r2.getID());
		System.out.println("r2.model.description = " + r2.getModel().getDescription());
		*/
		
		DataStore ds = DataStoreFactory.getDefaultStore();
		ds.setLoadpoint(Loadpoint.STATIC);
		Mobile m = DataStoreFactory.getDefaultStore().retrieveMobile("mob1");
		System.out.println("m.id = " + m.getID());
		System.out.println("item.id = " + m.getDynamicModel().getInventory().getItems().get(0).getID());
		m.save();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws XMLDBException {
		new TestDriver().start(null);
	}
}
