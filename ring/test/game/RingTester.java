package ring.test.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import ring.effects.Effect;
import ring.effects.EffectCreatorParameters;
import ring.effects.library.HPChange;
import ring.entities.Armor;
import ring.mobiles.Body;
import ring.mobiles.BodyFactory;
import ring.mobiles.Mobile;
import ring.mobiles.MobileClass;
import ring.mobiles.MobileClassFactory;
import ring.mobiles.NPC;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.Room;

public class RingTester {
	public static void main(String[] args) {
		Room r = new Room("A room", "lol");
		Room r2 = new Room("Room 2", "hidden!");
		Portal r2port = new Portal(r2, "north");
		r2port.setSearchDC(25);
		
		LocationManager.addToGrid(r, r2port, true);
		
		NPC mob = new NPC();
		MobileClass mc = MobileClassFactory.makeBarbarian();
		mob.setMobileClass(mc);
		
		mob.setBody(BodyFactory.createMediumHumanoidBody());
		
		Effect hpe = new Effect(Effect.Duration.PERMANENT, 0);
		hpe.addEffectCreator("hpchange", new HPChange());
		hpe.addParameter("hpchange", "amount", 40);
		
		Armor armor = new Armor(40, hpe, Body.HEAD, "[B][CYAN]Head Helm [RED]X90[R][WHITE]", "A", "lies here, gleaming");
		
		//Set up everything
		r.addEntity(armor);
		r.addMobile(mob);
		mob.setLocation(r);
		
		//Redirect useless debug output
		PrintStream realOut = System.out;
		try {
			//System.setOut(new PrintStream("blah"));
			NPC.theStream = realOut;
			mob.doCommand("get helm");
			mob.doCommand("wear helm");
			System.out.println("MOB HP: " + mob.getMaxHP());
		}
		catch (Exception e) {
			System.err.println("Test failed.");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
