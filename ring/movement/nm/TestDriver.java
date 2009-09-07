package ring.movement.nm;

import java.util.Scanner;

/**
 * Test driver for the new movement system that uses the Room-Portal-Destination graph
 * model.
 * @author jeff
 *
 */
public class TestDriver {
	public static void main(String[] args) {
		//Create three rooms, link them together in a triangle, and traverse each one.
		
		//Room creation
		Room r1 = new Room("Room 1", "This is the description for room 1");
		Room r2 = new Room("Room 2", "This is the description for room 2");
		Room r3 = new Room("Room 3", "This is the description for room 3");
		Room r4 = new Room("Room 4", "This is the description for room 4");
		
		System.out.println("Room information:");
		System.out.println("Room1: " + r1);
		System.out.println("Room2: " + r2);
		System.out.println("Room3: " + r3);
		System.out.println("Room4: " + r4);
		
		separator();
		
		//Room portals.
		//Room 2 is north of room 1.
		Portal r2port = new Portal(r2, "north");
		
		//Room 3 is east of room 2.
		Portal r3port = new Portal(r3, "east");
		
		//Room 4 is south of room 3.
		Portal r4port = new Portal(r4, "south");
		
		//Room 1 is west of room 4.
		Portal r1port = new Portal(r1, "west");
		
		System.out.println("Portal information: ");
		System.out.println(r2port);
		System.out.println(r3port);
		System.out.println(r4port);
		System.out.println(r1port);
		
		separator();
		
		//Link the rooms.
		System.out.println("Link room 1 to room 2: " + LocationManager.addToGrid(r1, r2port, true));
		System.out.println("Link room 2 to room 3: " + LocationManager.addToGrid(r2, r3port, true));
		System.out.println("Link room 3 to room 4: " + LocationManager.addToGrid(r3, r4port, true));
		System.out.println("Link room 4 to room 1: " + LocationManager.addToGrid(r4, r1port, true));
		
		separator();
		
		//Exit string test
		System.out.println("Exit string test:");
		System.out.println("Room 1 exits: " + r1.getExitsString());
		System.out.println("Room 2 exits: " + r2.getExitsString());
		System.out.println("Room 3 exits: " + r3.getExitsString());
		System.out.println("Room 4 exits: " + r4.getExitsString());
		
		separator();
		
		//Destination test.
		System.out.println("Destination test:");
		System.out.println("Room 1 north destination: " + r1.getPortal("north").getDestination());
		System.out.println("Room 2 east destination: " + r2.getPortal("east").getDestination());
		System.out.println("Room 3 south destination: " + r3.getPortal("south").getDestination());
		System.out.println("Room 4 west destination: " + r4.getPortal("west").getDestination());
		
		separator();
		
		//Traversal test
		Mobile m = new Mobile();
		//m.hiddenExitSearchCheck = 4999;
		//r2port.setSearchDC(5000);
		
		//Try allowing movement freely -- interactive testing.
		m.setLocation(r1);
		Scanner input = new Scanner(System.in);
		while (true) {
			String direction = input.nextLine();
			try {
				LocationManager.move(m, m.getLocation().getPortal(direction));
			} 
			catch (PortalNotFoundException e) {
				System.out.println("You can't go that way.");
			}
		}
		
		/*
		boolean success = LocationManager.move(m, r1.getPortal("north"));
		if (success) {
			success = LocationManager.move(m, r2.getPortal("east"));
			if (success) {
				success = LocationManager.move(m, r3.getPortal("south"));
				if (!success) {
					System.out.println("Traversal died on room 3 -> room4");
				}
			}
			else {
				System.out.println("Traversal died on room 2 -> room 3");
			}
		}
		else {
			System.out.println("Traversal died on room 1 -> room 2");
		}
		*/
	}
	
	public static void separator() {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println();
	}
}
