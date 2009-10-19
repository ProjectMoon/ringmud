package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.commands.CommandSender;
import ring.world.*;

import java.io.PrintStream;
import java.util.*;

public class NPC extends Mobile implements CommandSender, TickerListener {
	public static final long serialVersionUID = 1;
	// Instance variables specific to this mobile.
	// NPC Constants.
	// This is how often the NPC will do something. The higher the number, the
	// lesser of a chance it has to do something.
	public static final int AI_CHANCE = 30;
	// The random number generator to determine actions.
	Random rand;

	//A test stream used sometimes. Not really all that pertinent otherwise.
	//Will be removed in "production" code when we have actual unit tests.
	public static PrintStream theStream;
	
	public NPC() {
		super();
		rand = new Random(System.currentTimeMillis());
	}

	// Make another constructor for full stats.

	// #####
	// METHODS

	public synchronized void processTick(TickerEvent e) {
		// Process the effects list.
		/*
		 * for (int c = 0; c < super.effectsList.size(); c++) { Effect effect =
		 * (Effect)super.effectsList.get(c); if (effect.isDead()) {
		 * super.effectsList.removeElement(effect); } else
		 * effect.decrementTimer(); }
		 */
		super.processTick(e);

		int number = rand.nextInt(AI_CHANCE);
		if (number == 1) {
			doCommand("say Hohoho I'm " + getName());
		}

		if (number == 2) {
			doCommand("north");
		}

		if (number == 3) {
			doCommand("south");
		}

		if (number == 3) {
			doCommand("west");
		}

		if (number == 4) {
			doCommand("east");
		}

		if (number == 5) {
			doCommand("up");
		}

		if (number == 6) {
			doCommand("down");
		}
	}

	public void sendData(String data) {
		
	}
	public void doCommand(String command) {
		// Was anything even typed?
		if (command.length() <= 0)
			return;

		// Send the command.
		ring.commands.CommandResult res = super.handler.sendCommand(command);
		sendData(res.getText());
	}

	public String toString() {
		return super.getName();
	}
}
