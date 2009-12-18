package ring.nrapi.business;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import ring.main.RingModule;
import ring.nrapi.entities.Entity;
import ring.nrapi.entities.Item;
import ring.nrapi.mobiles.Body;
import ring.nrapi.mobiles.Mobile;
import ring.nrapi.movement.Room;

//TODO implement RefListener for referential object logic
public class TestDriver implements RingModule {
	@Override
	public void start(String[] args) {
		/*
		Mobile m = new Mobile();
		m.setID("an id");
		Item i = new Item();
		i.setReferential(true);
		m.equip(Body.FACE, i);
		String xml = m.toXML();
		*/
		
		Room r = new Room();
		Entity e = new Entity();
		e.setID("entid");
		r.setID("room id");
		r.addEntity(e);
		String xml = r.toXML();
		
		System.out.println(xml);
		System.out.println();
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(Room.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			Room r1 = (Room)um.unmarshal(new StreamSource(new StringReader(xml)));
			System.out.println("Got a room.");
			
			System.out.println(r1);
			System.out.println(r1.getID());
			System.out.println(r1.getModel().getEntityIDs().get(0));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new TestDriver().start(null);
	}
}
