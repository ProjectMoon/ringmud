package ring.load;

import ring.mobiles.*;
import java.io.*;
import ring.entities.*;
public class Loader {
	/*
	This class handles the loading of the MUD from various files through serialization.
	It loads things in a very specific order:
	* Scripts:
		-System Scripts
		-Effect Scripts
		-Spell Scripts
		-Item Scripts
		-Mobile Scripts
	* World:
		-If world.state exists, it will attempt to de-serialize it
		-If world.state doesn't exist, it will compile the world from scripts.
		-If world.state was loaded, it will then check for any new zones that have been added since the last boot
		 and add them to the world.
	*/
	
	public static void main(String [] args) {

	}
}
