package ring.nrapi.business;

import java.util.Scanner;

import org.xmldb.api.base.XMLDBException;

import ring.main.RingModule;
import ring.nrapi.aggregate.TestAggregate;
import ring.nrapi.data.ExistDB;
import ring.nrapi.data.XMLDataStore;
import ring.nrapi.data.XMLDataStoreFactory;
import ring.nrapi.resources.TestResource;

public class TestDriver implements RingModule {
	@Override
	public void start(String[] args) {
//		//Set up db
		System.out.println("Resetting database.");
		ExistDB db = new ExistDB();
		try {
			db.removeAllResources();
			db.createRingDatabase();
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
		
		//Populate db
		System.out.println("Creating test aggregate");
		TestResource tr = new TestResource();
		tr.setAmount(500);
		tr.setName("a test resource");
		
		TestAggregate agg = new TestAggregate();
		agg.setID("tr1");
		agg.setTestResource(tr);
		
		System.out.println(agg.toXML());
		
		System.out.println("Storing...");
		XMLDataStore store = XMLDataStoreFactory.getDefaultStore();
		System.out.println("Stored? " + store.storeAggregate(agg));
		
		//Read from db
		Test t = new Test();
		if (t.load("tr1")) {
			System.out.println("Loaded!");
			System.out.println(t.getTestResource().getAmount());
			System.out.println(t.getTestResource().getName());
			//Make some changes...
			System.out.println("Making a change, and saving...");
			t.getTestResource().setAmount(600);
			t.save();
		}
		
		System.out.println("Query the database!");
		
		String input = "";
		Scanner scan = new Scanner(System.in);
		while (input.equals("q") == false && input.equals("quit") == false) {
			input = scan.nextLine();
			if (input.equals("q") == false && input.equals("quit") == false) {
				try {
					db.testQuery(input);
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
