package ring.nrapi.business;

import java.util.Scanner;

import org.xmldb.api.base.XMLDBException;

import ring.main.RingModule;
import ring.nrapi.data.ExistDB;
import ring.nrapi.data.DataStore;
import ring.nrapi.data.DataStoreFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import javax.xml.bind.*;
import ring.nrapi.entities.*;
import ring.nrapi.movement.*;

public class TestDriver implements RingModule {
	@Override
	public void start(String[] args) {
		//XML Test
		RoomModel model = new RoomModel();
		Zone z = new Zone();
		Room room = new Room();
		
		model.setDepth(1);
		model.setLength(2);
		model.setWidth(4);
		model.setTitle("A test room");
		model.setDescription("A description for the test room.");
		
		room.setID("roomID1");
		
		Entity e1 = new Entity();
		Entity e2 = new Entity();
		e1.setID("e1");
		e2.setID("e2");
		
		room.setModel(model);
		room.setZone(z);
		
		room.addEntity(e1);
		room.addEntity(e2);
		
		String xml = room.toXML();
		
		System.out.println(xml);
		System.out.println();
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(Room.class);
			Unmarshaller um = ctx.createUnmarshaller();
			System.out.println("Have unmarshaller...");
			Room r = (Room)um.unmarshal(new StreamSource(new StringReader(xml)));
			System.out.println("Got a room.");
			
			System.out.println(r);
			System.out.println(r.getModel().getTitle());
			System.out.println(r.getModel().getEntityIDs().get(0));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
