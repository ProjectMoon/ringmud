package ring.movement.nm;

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
		
		System.out.println("Room information:");
		System.out.println("Room1: " + r1);
		System.out.println("Room2: " + r2);
		System.out.println("Room3: " + r3);
		
		separator();
		
		//Room portals.
		//Room 2 is north of room 1.
		Portal r2port = new Portal(r2, "north");
		
		//Room 3 is east of room 2.
		Portal r3port = new Portal(r3, "east");
		
		//Room 1 is southwest of room 3.
		Portal r1port = new Portal(r1, "southwest");
		
		System.out.println("Portal information: ");
		System.out.println(r2port);
		System.out.println(r3port);
		System.out.println(r1port);
		
		separator();
		
		//Link the rooms.
		System.out.println("Link room 1 to room 2: " + LocationManager.addToGrid(r1, r2port));
		System.out.println("Link room 2 to room 3: " + LocationManager.addToGrid(r2, r3port));
		System.out.println("Link room 3 to room 1: " + LocationManager.addToGrid(r3, r1port));
		
		separator();
		
		//Exit string test
		System.out.println("Exit string test:");
		System.out.println("Room 1 exits: " + r1.getExitsString());
		System.out.println("Room 2 exits: " + r2.getExitsString());
		System.out.println("Room 3 exits: " + r3.getExitsString());
		
		separator();
		
		//Destination test.
		System.out.println("Destination test:");
		System.out.println("Room 1 north destination: " + r1.getDestination("north"));
		System.out.println("Room 2 east destination: " + r2.getDestination("east"));
		System.out.println("Room 3 southwest destination: " + r3.getDestination("southwest"));
		
		separator();
		
		//Traversal test
		Mobile m = new Mobile();
		//m.hiddenExitSearchCheck = 4999;
		//r2port.setSearchDC(5000);
		
		//Start at r1, then move to r2, then to r3, then back to r1
		m.setLocation(r1);
		boolean success = LocationManager.move(m, r1.getPortal("north"));
		if (success) {
			success = LocationManager.move(m, r2.getPortal("east"));
			if (success) {
				success = LocationManager.move(m, r3.getPortal("southwest"));
				if (!success) {
					System.out.println("Traversal died on room 3 -> room1");
				}
			}
			else {
				System.out.println("Traversal died on room 2 -> room 3");
			}
		}
		else {
			System.out.println("Traversal died on room 1 -> room 2");
		}
	}
	
	public static void separator() {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println();
	}
}
