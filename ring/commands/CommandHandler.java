package ring.commands;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.spells.*;
import ring.effects.*;
import ring.entities.*;
import ring.movement.*;
import ring.mobiles.*;
import ring.mobiles.backbone.Equipment;
import ring.mobiles.backbone.Inventory;
import ring.skills.*;
import ring.world.*;
import ring.players.*;
import ring.util.*;
import java.lang.reflect.*;
import java.util.*;
import java.beans.*;
import java.io.*;
import ring.resources.ClassFeatureLoader;

public final class CommandHandler {
	// This is the class that contains code for all commands. It returns a
	// CommandResult object with a
	// boolean value and a String. The boolean indicates if the command
	// "failed." The String is what gets
	// sent back to the mobile if it is a PlayerCharacter.

	// Instance variables.
	private CommandSender sender;// The Command-Sending object this handler is
									// linked to.
	private TreeMap<String, Method> commands;// TreeMap of all the commands.
												// super quick lookup.
	private TreeMap<String, String> alternateCommands;// TreeMap of all the
														// alternate commands.

	// Internal constants.

	public CommandHandler(CommandSender sender) {
		this.sender = sender;

		alternateCommands = new TreeMap<String, String>();
		// Set up the command list.
		commands = new TreeMap<String, Method>();
		Class<?> c = this.getClass();
		Method[] m = c.getDeclaredMethods();
		for (int x = 0; x < m.length; x++) {
			String name = m[x].toString();
			// Stores the Method object into a tree with the name "CMD_cmdhere"
			// as key.
			if (name.indexOf(Command.CMD) != -1) {
				int startPos = name.indexOf(Command.CMD);
				int endPos = name.indexOf("(");
				commands.put((name.substring(startPos, endPos)), m[x]);
			}
		}

		// Register the alternate commands.
		// These commands are checked BEFORE command completion
		registerAlternateCommand("north", "n");
		registerAlternateCommand("south", "s");
		registerAlternateCommand("west", "w");
		registerAlternateCommand("east", "e");
		registerAlternateCommand("up", "u");
		registerAlternateCommand("down", "d");
		registerAlternateCommand("look", "l");
		registerAlternateCommand("look", "lo");
		registerAlternateCommand("say", "\'");
		registerAlternateCommand("inventory", "inv");
		registerAlternateCommand("equipment", "eq");
		registerAlternateCommand("prepare", "prep");
		registerAlternateCommand("movesilently", "ms");
	}

	// sendCommand method.
	// This method splits a command up and calls handleCommand(). It returns a
	// CommandResult that the
	// sender can use.
	public CommandResult sendCommand(String command) {
		System.out.println("received: " + command);
		// Make the command object.
		Command cmd = new Command(command, Command.CMD);
		System.out.println("Made cmd object");

		// We have a hierarchy of command checking:
		// 1. We check to see if it's an aliased command.
		// 2. Failing that, we try to complete the command.
		// 3. Failing THAT, we see if it's a class feature command.
		if (!checkAlternateCommand(cmd))
			if (!completeCommand(cmd))
				if (findClassFeature(cmd))
					cmd = new Command("classfeature " + command, Command.CMD);

		System.out.println("checked alternates");
		// actually do the command.
		CommandResult cr = handleCommand(cmd);
		System.out.println("handled command");
		return cr;
	}

	private boolean findClassFeature(Command comm) {
		String cmd = comm.getActualCommand();

		ClassFeature cf = ClassFeatureLoader.getClassFeatureByName(cmd);

		if (cf != null)
			return true;
		else
			return false;
	}

	// completeCommand method.
	// This method returns a command name based on a fragment given to it. It
	// returns by alphabetical priority
	// for ambiguous fragments. If no suitable command can be found, it returns
	// null.
	private boolean completeCommand(Command comm) {
		// set up some initial information, like a list of all available
		// commands.
		String fragment = "CMD_" + comm.getActualCommand();

		// if the fragment is only 1 letter, it's not something we should bother
		// looking
		// up; there is TOO much ambiguity. 1 letter commands are registered as
		// alternate commands.
		if (fragment.length() <= 5) { // the 5 includes the CMD_
			comm.setActualCommand("bad");
			return false;
		}

		// if the command is >= 2 letters, we can proceed with completion.
		Set<String> keys = commands.keySet();

		// now, loop through all available command names
		// and see if we can find an available full command
		for (String key : keys) {
			if (key.startsWith(fragment)) {
				// substring(4) is to get rid of "CMD_"
				comm.setActualCommand(key.substring(4));
				return true;
			}
		}

		// nothing was found; return false.
		return false;
	}

	// handleCommand method.
	// This method invokes the current command entered.
	private synchronized CommandResult handleCommand(Command cmd) {
		CommandResult res = new CommandResult();
		String methodName = Command.CMD + cmd.getActualCommand();
		Method m;// The method to be executed.

		// Get the list of declared methods... will probably more efficient
		// later by creating it on
		// instantiation.
		m = (Method) commands.get(methodName);

		// If m is null it obviously doesn't exist so.... EXECUTE THE BAD
		// COMMAND METHOD! :D
		if (m == null) {
			// The line below sets the command sent itself to "bad" in order to
			// get rid of the parameters
			// Of the original command so there will be no exceptions.
			cmd = new Command(new String[] { "bad" }, Command.CMD);
			m = (Method) commands.get("CMD_bad");
		}

		// Determine the type of command and parameters.
		// Set up the parameters object.
		CommandParameters parameters = new CommandParameters(cmd
				.getParameters(), sender);
		// System.out.println("params: " + parameters);
		int nMethodParams = TextParser.countParameters(m.toString());

		// Invoke the method!
		try {
			// cover both no-parameter and parametered commands

			// commands with no paramteres
			if (nMethodParams == 0) {
				System.out.println("no params");
				res = (CommandResult) m.invoke(this);
			}

			// commands with parameters. however, they may still not need
			// parameters to operate.
			else {
				System.out.println("params");
				if (cmd.getParameterLength() == 0) {
					System.out.println("execing null param");
					res = (CommandResult) m.invoke(this, parameters);
				} else
					res = (CommandResult) m.invoke(this,
							new Object[] { parameters });
			}
		} catch (IllegalAccessException e) {
			System.out.println("UHOH1");
		} catch (InvocationTargetException e) {
			System.err
					.println("InvocationTargetException for CMD_"
							+ cmd.getActualCommand()
							+ "(). This most likely means there is a NullPointerException issue in the command code. It may also be a ClassCastException issue.");
			e.printStackTrace();
		}
		// Fix that little bug with the no-parameter methods like the
		// directional ones.
		catch (IllegalArgumentException e) {
			System.out.println("LOL ARGUMENT FAILURE :D!");
			System.out.println(e);
			e.printStackTrace();
			/*
			 * try { res = (CommandResult)m.invoke(this, (Object[])null); }
			 * catch (IllegalAccessException ex) { System.out.println("UHOH11");
			 * } catch (InvocationTargetException ex) {
			 * System.err.println("InvocationTargetException for CMD_" +
			 * cmd.getActualCommand() +
			 * "(). This most likely means there is a Null Pointer issue in the command code."
			 * ); }
			 */
		}
		// End of ye method invocation.

		// Return the text of the CommandResult to the sender.
		return res;
	}

	// checkAlternateCommand method.
	// This method checks the current command to see if it is an alternate
	// command.
	// Manipulates the command array directly. Returns true if successful, false
	// if not.
	private boolean checkAlternateCommand(Command cmd) {
		String methodName = Command.CMD + cmd.getActualCommand();
		Method m;// The method to be executed.

		// Get the list of declared methods.
		m = (Method) commands.get(methodName);

		// Is m null? If so perhaps it is an alternate command.
		if (m == null) {
			String realCmd = (String) alternateCommands.get(cmd
					.getActualCommand());
			if (!(cmd.getActualCommand().equals(realCmd)) && realCmd != null) {
				cmd.setActualCommand(realCmd);
				return true;
			}
		}

		// it wasn't successful
		return false;
	}

	// registerAlternateCommand method.
	// This method registers an alternate--often shortened--command that is
	// linked to another command.
	// This is used for things like "north" and "n" being the same in-game. This
	// is a crude system and
	// I plan to implement an auto-text algorithm with priorities built in
	// later.
	//
	// Returns true if succeeds, and false if fails.
	public boolean registerAlternateCommand(String origCmd, String newCmd) {
		alternateCommands.put(newCmd, origCmd);
		// code here.
		return true;
	}

	// ###########################################################
	// #HERE BEGINS THE CODE FOR ALL OF THE COMMANDS IN THE GAME.#
	// ###########################################################

	// All commands have parameters in the method signature that match the
	// MAXIMUM possible parameters
	// they can have in-game. Since we are not using JDK 1.5, a limitation of
	// this system is that we
	// cannot have commands with infinite parameters without a complicated
	// implementation, although
	// I doubt we shall actually need that.

	// If the parameter is not used in the in-game command, the call to the
	// method should simply
	// send a null parameter and the code will handle it accordingly.

	// Bad command.
	// This isn't really a command. It's just a method to execute in order to
	// return an error message
	// of sorts when a player types in something wrong so the MUD won't blow up.
	// Though theoretically someone
	// could type it and get the "What?" output...
	private CommandResult CMD_bad() {
		CommandResult res = new CommandResult();
		res.setText("[R][GREEN]What?");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_classfeature(CommandParameters params) {
		params.init(Command.CMD);
		CommandResult res = new CommandResult();

		res.setFailText("You don't have that class feature!");

		// This is the actual command the user typed in.
		// When this method is called, the classfeature command gets prepended
		// to the command string.
		String classFeatureName = (String) params.getParameterAsText(0);

		Mobile mob = (Mobile) sender;

		ClassFeature cf = mob.getMobileClass()
				.getClassFeature(classFeatureName);

		// They don't have that class feature, or it doesn't exist.
		if (cf == null)
			return res;

		Affectable target = (Affectable) params.getParameter(1);

		cf.chooseFeature(mob.getLevel());
		if (cf.getTargetType().equals(ClassFeature.SELF_ONLY)) {
			System.out.println("Executing rage with " + mob);
			cf.execute(mob);
		} else
			cf.execute(target);

		res.setText(cf.getOutputText());
		res.setSuccessful(true);

		return res;
	}

	// Look command.
	// This command looks around the current room of the sender, or it looks at
	// a specified object.
	private CommandResult CMD_look(CommandParameters params) {
		System.out.println("in look");
		System.out.println("sender in Look: " + sender);
		params.init(Command.CMD);
		System.out.println("param created");
		Object t = params.getParameter(0);
		System.out.println("got param");

		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You don't see that here.");

		// set up an object for our "looker"
		Mobile mob = (Mobile) sender;

		// First check to see if we are just looking at the room. ie: sending
		// just the "look" command.
		if (t == null) {
			Location loc = mob.getLocation();

			if (loc instanceof Room) {
				Room room = (Room) loc;

				if (mob.isBlind) {
					res.setFailText("You see nothing, for you are blind!");
					World.sendVisualToLocation(mob, mob.getName()
							+ " stumbles about blindly.", null);
					return res;
				}

				else {
					String lookText = room.getTitle() + "\n"
							+ room.getDescription() + "\n"
							+ room.getExitsString(mob.hiddenExitSearchCheck)
							+ "\n" + room.getMobileList(mob, mob.spotCheck)
							+ room.getEntityList();

					res.setText(lookText);
					res.setSuccessful(true);
				}
			}
		}

		// Now check to see if we are looking at something specific, if true,
		// return the look description
		// of the thing being looked at it.
		else {
			if ((!(t instanceof Affectable)) && (t != null))
				return res;

			// is our looker blind?
			if (mob.isBlind) {
				res
						.setFailText("You have nothing to look at, for you are blind!");
				return res;
			}

			Affectable target = (Affectable) t;
			// Get the look text of the thing being looked at.
			String lookText = target.getLongDescription();
			// add it to the CommandResult.
			res.setText("You look at " + target.getName() + " and see:\n"
					+ lookText);
			// The command succeeded.
			res.setSuccessful(true);
		}

		// Return CommandResult.
		return res;
	}

	// Say command.
	// This command says something from the sender to all other mobiles in the
	// room.
	// This method is a special case and will not get called by the
	// handleCommand method.
	private CommandResult CMD_say(CommandParameters params) {
		// Make the CommandResult object.
		params.init(Command.TXT);
		CommandResult res = new CommandResult();
		res.setFailText("You cannot talk!");
		Mobile mob = (Mobile) sender;

		// Say code here.
		String message = "";
		String textBackToPlayer;

		// check for speaker's deafness.
		if (mob.isDeaf)
			textBackToPlayer = "You say something, but you're not entirely sure what since you cannot hear!";
		else
			textBackToPlayer = "You say, \"";

		String textToOtherPlayers = mob.getName() + " says, \"";

		// Get the "parameters" as words.
		int length = params.length();
		for (int c = 0; c < length; c++) {
			message += (String) params.getParameter(c);

			if (c != length - 1) {
				message += " ";
			}
		}

		// check for speaker's deafness deafness again.
		if (!mob.isDeaf) {
			textBackToPlayer += message;
			textBackToPlayer += "\"";
		}

		textToOtherPlayers += message;
		textToOtherPlayers += "\"";

		res.setText(textBackToPlayer);

		World.sendAudioToLocation(mob, textToOtherPlayers, mob.getName()
				+ " says something, but you cannot hear it!\n");

		res.setSuccessful(true);
		// Return the CommandResult.
		return res;
	}

	/**
	 * Godvoice command: This command uses the voice of the gods to notify
	 * players of something. Usable only by immortals.
	 * 
	 * @param params
	 * @return A confirmation that the voice of the gods was heard.
	 */
	private CommandResult CMD_godvoice(CommandParameters params) {
		// Make the CommandResult object.
		params.init(Command.TXT);
		CommandResult res = new CommandResult();
		res.setFailText("You don't feel particularly divine...");
		Mobile mob = (Mobile) sender;

		// Say code here.
		String message = "";
		String textBackToPlayer;

		// check for speaker's deafness.
		textBackToPlayer = "You project your voice across the cosmos, saying, \"";

		String textToOtherPlayers = "The voice of the gods rumbles in the sky! \"";

		// Get the "parameters" as words.
		int length = params.length();
		for (int c = 0; c < length; c++) {
			message += (String) params.getParameter(c);

			if (c != length - 1) {
				message += " ";
			}
		}

		// check for speaker's deafness deafness again.
		if (!mob.isDeaf) {
			textBackToPlayer += message;
			textBackToPlayer += "\"";
		}

		textToOtherPlayers += message;
		textToOtherPlayers += "\"";

		res.setText(textBackToPlayer);

		World.notifyPlayersAtLocation(mob, textToOtherPlayers);

		res.setSuccessful(true);
		// Return the CommandResult.
		return res;
	}

	// North command.
	private CommandResult CMD_north() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.NORTH);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// South command.
	private CommandResult CMD_south() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.SOUTH);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// West command.
	private CommandResult CMD_west() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.WEST);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// East command.
	private CommandResult CMD_east() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.EAST);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// Up command.
	private CommandResult CMD_up() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.UP);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// Down command.
	private CommandResult CMD_down() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[GREEN]You can't go that way.[WHITE]");
		Mobile mob = (Mobile) sender;

		// is the mobile too low on MV to move?
		if (mob.getCurrentMV() - 1 <= 0) {
			res.setFailText("[R][WHITE]You are too exhausted to move any further. Rest for awhile to regain your strength.");
			return res;
		}

		boolean success = mob.move(LocationManager.DOWN);
		res.setSuccessful(success);
		if (success)
			res.setText("");

		// Return the CommandResult.
		return res;
	}

	// Score command.
	// This shows the score of the mobile enacting it... Which is usually a
	// player. What the heck would
	// an NPC need to know their score for?
	private CommandResult CMD_score() {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[B]FATAL ERROR IN SCORE METHOD.[R]");
		Mobile mob = (Mobile) sender;
		String score = "\n[R][WHITE]Score and Statistics for [B][GREEN]"
				+ mob.getName()
				+ "[R][WHITE]"
				+ "\nAlignment: "
				+ mob.getAlignment().getAlignmentString()
				+ "\nHP: [B][RED]"
				+ mob.getCurrentHPString()
				+ "/"
				+ mob.getMaxHPString()
				+ " [R][WHITE]Class: "
				+ mob.getMobileClass().getDisplayName()
				+ " [R][WHITE]Level: [B][MAGENTA]"
				+ mob.getLevel()
				+ "[R][WHITE] Class Type: "
				+ mob.getMobileClass().getClassification().getDisplayName()
				+ "\nCurrent XP Amount: [YELLOW]0% [WHITE]Guild: [B][GREEN]None[R][WHITE]";

		res.setText(score);
		res.setSuccessful(true);
		return res;

	}

	private CommandResult CMD_get(CommandParameters params) {
		params.init(Command.CMD);
		Object target = params.getParameter(0);

		CommandResult res = new CommandResult();
		res.setFailText("[R][GREEN]You can't get that.[WHITE]");

		if (target == null) {
			res.setFailText("[R][WHITE]You don't see that here.");
			return res;
		}

		Mobile getter = (Mobile) sender;
		WorldObject thing = (WorldObject) target;

		// Check if thing is gettable.
		if (!thing.isItem())
			return res;

		// If yes, remove thing from room and add to getter's inventory.
		Item i = (Item) thing;
		getter.addItemToInventory((Item) thing);

		// Remove the item from the room.
		Room room = (Room)getter.getLocation();
		room.removeEntity((Item) thing);

		res.setText("You get " + i.getIndefiniteDescriptor().toLowerCase()
				+ " " + i.getName() + "[R][WHITE].");

		// Notify other people in room
		World.sendVisualToLocation(getter, getter.getName() + " picks up "
				+ i.getIndefiniteDescriptor().toLowerCase() + " " + i.getName()
				+ "[R][WHITE].", null);

		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_drop(CommandParameters params) {
		System.out.println("initting");
		params.init(Command.INV);
		System.out.println("initted");
		Object target = params.getParameter(0);
		System.out.println("got param: " + target);

		CommandResult res = new CommandResult();

		if (target == null) {
			res.setFailText("[R][WHITE]Drop what?");
			return res;
		}

		if (!(target instanceof Affectable)) {
			res.setFailText("[R][WHITE]You don\'t have that item.");
			return res;
		}

		Item item = (Item) target;
		Mobile mob = (Mobile) sender;

		// Remove the item from the inventory.
		System.out.println("removing...");
		System.out.println("removed? " + mob.removeItemFromInventory(item));

		// Put it back in the room the mobile is in.
		System.out.println("attempting to add");
		Room room = (Room)mob.getLocation();
		room.addEntity(item);
		System.out.println("added.");

		// Set the text.
		res.setText("[R][WHITE]You drop "
				+ item.getIndefiniteDescriptor().toLowerCase() + " "
				+ item.getName() + "[R][WHITE].");

		// Notify other people in the world.
		World.sendVisualToLocation(mob, mob.getName() + " drops "
				+ item.getIndefiniteDescriptor().toLowerCase() + " "
				+ item.getName() + "[R][WHITE].",
				"\nYou hear the thud of something being dropped.\n");

		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_inventory() {
		CommandResult res = new CommandResult();
		res.setFailText("You are currently carrying:\nNothing.");
		String text = "You are currently carrying:\n";
		Mobile mob = (Mobile) sender;
		Inventory inventory = mob.getInventory();

		if ((inventory == null) || (inventory.size() == 0))
			return res;

		for (Item item : inventory) {
			text += item.getName() + "\n";
		}

		res.setText(text);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_cast(CommandParameters params) {
		System.out.println("cast call");
		CommandResult res = new CommandResult();
		params.init(Command.SPL);

		res.setFailText("[R][WHITE]You don't have that spell.");
		// UPDATE TO THIS ALGORITHM:
		// Due to the way the new Spell system works, this command does not take
		// a Spell as a
		// parameter. It will have to take a String as the name of the spell and
		// look it up via
		// the SpellList of the sender.

		String spellName = params.constructSpellName();
		Mobile mob = (Mobile) sender;

		// Make a SpellList for usage.
		SpellList spells;

		// Check if the caster actually has the spell.

		spells = mob.getMobileClass().getAvailableSpells();

		// NEEDS TO BE CHECKED LATER WITH A "SPELLCASTING" SKILL!!!!!!!!
		if (spells == null) {
			res.setFailText("[R][WHITE]You can't cast spells!");
			return res;
		}

		spellName = spellName.trim();
		Spell spell = spells.getSpellByName(spellName);

		if (spell == null) {
			System.out.println("You don't have that spell.");
			res.setFailText("[R][WHITE]You don't have that spell.");
			return res;
		}

		// Has the caster memorized the spell?
		spells = mob.getMobileClass().getMemorizedSpells();
		spell = spells.getSpellByName(spellName);

		if (spell == null) {
			res.setFailText("[R][WHITE]You haven't prepared that spell.");
			return res;
		}

		// These need to go into the single target thing.

		// Check if they're ABLE to cast the spell. (Not paralyzed, etc)
		// Later.....

		// Check the target type of the spell. If is "ROOM" then leave the
		// "target"
		// parameter alone and go to CASTING PROCEDURES. If it isn't "ROOM"
		// we must go into the targeting check procedures.

		if (spell.getTargetType() == Spell.SINGLE_TARGET) {
			System.out.println("1");
			WorldObject t = (WorldObject) params.lastParameter();
			if (t == null) {
				System.out.println("You don't see that here.");
				res.setFailText("[R][WHITE]You don't see that here.");
				return res;
			}

			if (!(t instanceof Affectable)) {
				res.setFailText("[R][WHITE]That is an invalid target.");
				return res;
			}

			System.out.println("2");
			Affectable target = (Affectable) t;
			Effect e = spell.generateEffect();
			e.setTarget(target);

			// is target valid?
			// does target exist?
			// casting...

			// remove spell from memory
			// casting delay
			// check the resistance and stuff...
			System.out.println("3");
			mob.sendData("[R][WHITE]You begin chanting...\n");
			e.startEffect();
			System.out.println("started effect");
			res.setText("[R][WHITE]You cast " + spell.getName() + " at "
					+ target.getName() + ".");
			spells.removeSpell(spell);
			res.setSuccessful(true);
		}

		// TARGET CHECKING PROCEDURES:
		// Check if "target" is null or not.
		// If "target" exists, make sure it is a valid target for the spell.
		// If "target" doesn't exist, return proper failure message.
		// If all of the conditions are met, move to CASTING PROCEDURES.

		// CASTING PROCEEDURES:
		// Remove the spell from the caster's memorized spell slot.
		// Engage in the casting time delay.
		// Choose random, valid targets. (if the spell is "ROOM")
		// Loop through each target/thing affected by the spell and check spell
		// resistance and such.
		// In this loop: Each target gets their saving throw and the like.
		// In this loop: APPLY THE EFFECTS OF THE SPELL TO THE TARGET IF THEY
		// ARE AFFECTED.
		// End the loop.
		// Finish this method.

		return res;
	}

	private CommandResult CMD_wear(CommandParameters params) {
		params.init(Command.INV);
		Object t = params.getParameter(0);

		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You can't wear that!");

		if (t == null) {
			res.setFailText("[R][WHITE]Wear what?");
			return res;
		}
		if ((!(t instanceof Item)) && (t != null))
			return res;

		Item target = (Item) t;
		if (!target.isWearable())
			return res;

		// We've checked everything dealing with if the thing is wearable or
		// not.
		Mobile mob = (Mobile) sender;
		Body mobBody = mob.getBody();

		// Check if the target is wearable.
		if (!(target.isWearable()))
			return res;
		System.out.println("Passed step 1...");

		// Check if the wearer meets all the requirements to wear target
		// (alignment, class, etc.)

		// Check if wearer has the actual body part to wear the target on. The
		// method that will do
		// this actually returns an integer corresponding to the number of
		// eligible body parts
		// on the wearer's body. If this number is greater than one, the next
		// step has a special
		// addition.
		BodyPart[] eligibleParts = mobBody.getEligibleBodyParts(target);

		// Were there any parts actually found?
		if (eligibleParts == null)
			return res;
		System.out.println("Passed step 2...");

		// Check if the wearer has room on the body part. If the number from the
		// previous step was
		// greater than 1, start a for loop equal to the amount of body parts
		// remaining (# of them - 1).
		// Try to equip on each of those body parts.
		boolean success = false;
		BodyPart thePart = null;

		for (int c = 0; c < eligibleParts.length; c++) {
			thePart = eligibleParts[c];
			success = mob.equip(thePart, target);
			if (success)
				break;
		}

		// Remove the target from the wearer's inventory.
		mob.removeItemFromInventory(target);

		// Set text and stuff.
		res.setText("[R][WHITE]You wear "
				+ target.getIndefiniteDescriptor().toLowerCase() + " "
				+ target.getName() + " on your "
				+ thePart.getName().toLowerCase() + ".");
		res.setSuccessful(true);

		// Notify other players.
		World.sendVisualToLocation(mob, "[R][WHITE]" + mob.getName()
				+ " wears " + target.getIndefiniteDescriptor().toLowerCase()
				+ " " + target.getName() + " on your "
				+ thePart.getName().toLowerCase() + ".", null);

		return res;
	}

	private CommandResult CMD_remove(CommandParameters params) {
		params.init(Command.EQP);
		Object t = params.getParameter(0);

		CommandResult res = new CommandResult();

		// Check if the thing is an actual item...
		if (t == null) {
			res.setFailText("[R][WHITE]Remove what?");
			return res;
		}

		if (!(t instanceof Affectable)) {
			res.setFailText("[R][WHITE]You aren't wearing that!");
			return res;
		}

		// If yes, make it an item.
		Item item = (Item) t;

		BodyPart part = item.getPartWornOn();
		Mobile mob = (Mobile) sender;

		mob.dequip(part);
		mob.addItemToInventory(item);

		res.setText("[R][WHITE]You remove "
				+ item.getIndefiniteDescriptor().toLowerCase() + " "
				+ item.getName() + " from your " + part.getName().toLowerCase()
				+ ".");

		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_search(CommandParameters params) {
		params.init(Command.TXT);
		String dir = (String) params.getParameter(0); // the direction to search
														// in

		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You cannot search.");

		Mobile mob = (Mobile) sender;
		SkillList mobSkills = mob.getSkillList();
		Skill search = mobSkills.getSkillByName("search");

		if (search == null)
			return res;
		int check = search.makeCheck();
		Room room = (Room)mob.getLocation();

		// The search command behaves differently for mobiles who are blind.
		// Since the look command returns no useful information for them, they
		// can
		// use the search command to help find their way around the world,
		// although in a
		// very poor fashion. A -4 penalty is applied to the check as per the
		// d20 rules
		// on blindness. The search check total reveals more information as it
		// goes higher:
		// 10: 2 random exits.
		// 15: 4 random exits.
		// 20: 6 random exits. (maximum)
		// For every 1 above 25, one random mobile or entity is unveiled. Their
		// descriptions should be vague.

		// Blind search version
		if (mob.isBlind) {
			check -= 4; // apply -4 penalty
			if (check < 10) {
				res
						.setFailText("[R][WHITE]You're just as lost as you were when you began this search.");
				return res;
			}

			res
					.setText("[R][WHITE]You cautiously begin to move around the room, trying to get a sense of its layout through your non-visual senses. (check: "
							+ check + ")");

			// exits first: CURRENTLY THIS IS NOT IMPLEMENTED
			String searchText = "After feeling your way around the room, you [B]THINK[R] you find the following:\nExits: "; // +
																															// room.generateBlindExitsString(check);

			// now mobiles/entities
			searchText += "\nOccupants:\n"
					+ room.generateBlindRandomOccupantList(check);

			mob
					.setLockMessage("[R][WHITE]You are still searching the room (blindly, no less)!");
			mob.setLockFinishedMessage(searchText);
			int lockTime = (int) ((room.length() * room.width()) / 25);
			if (lockTime > 15)
				lockTime = 15; // 15 is the maximum.
			if (lockTime < 2)
				lockTime = 2; // 2 is the minimum.

			lockTime = lockTime * 2;
			mob.increaseLockTime(lockTime);
			res.setSuccessful(true);
		}

		// Regular search version
		else {
			res.setText("[R][WHITE]You begin [B]searching[R] the " + dir
					+ " part of the room (check: " + check + ")");
			// this will be sent back as a lock finish message for the player.
			String searchText = "[R][WHITE]Your [B]search[R] turns up the following:\n";

			Room checkRoom = (Room)LocationManager.getDestination(mob.getLocation(), dir);
			// first, look for hidden exits and set the mobile's current hidden
			// exit search check to the result of the
			// search check.
			mob.hiddenExitSearchCheck = check;
			if (check >= checkRoom.getSearchDC())
				searchText += "Hidden Exits: " + dir + "\n";

			// second, look for traps
			searchText += "Traps: IMPLEMENT LATER!\n";

			// third, if player has the Trapfinding class feature, look for DC
			// 20+ traps.
			searchText += "Difficult Traps: IMPLEMENT LATER!";

			// now, we need to calculate a lock time based on the square footage
			// of the room.
			// this calculation is currently based on the fact that a 5x5 (25
			// square feet) area takes 6 seconds (3 ticks)
			// to search in the D&D system (see search skill description). The
			// number may be adjusted later if it is
			// too long or too short. The lock time should always be at least 2
			// ticks.
			int lockTime = (int) ((room.length() * room.width()) / 25);
			if (lockTime > 15)
				lockTime = 8; // 8 is the maximum.
			if (lockTime < 2)
				lockTime = 2; // 2 is the minimum.
			mob.setLockMessage("You are still searching the room!");
			mob.setLockFinishedMessage(searchText);
			mob.increaseLockTime(lockTime);

			res.setSuccessful(true);
		}

		return res;
	}

	private CommandResult CMD_searchcheck() {
		CommandResult res = new CommandResult();
		Mobile mob = (Mobile) sender;
		res.setText("Your current search check: " + mob.hiddenExitSearchCheck);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_hide() {
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You don't seem to be one for hiding...");

		Mobile mob = (Mobile) sender;

		// is the mobile already hiding? if so, stop hiding.
		if (mob.hideCheck > 0) {
			res.setText("[R][WHITE]You stop hiding.");
			mob.hideCheck = 0;
			res.setSuccessful(true);
			return res;
		}

		// apparently they're not hiding. try to set up the skill.
		SkillList skills = mob.getSkillList();
		Skill hide = skills.getSkillByName("hide");

		// can they even hide?
		if (hide == null) {
			return res;
		}

		// can't hide while fighting!
		if (mob.isFighting) {
			res.setFailText("[B][YELLOW]You can't hide in combat![R][WHITE]");
			return res;
		}

		int check = hide.makeCheck();

		// Apply some bonuses for people who bother to sit down or go prone.
		// This falls in the +2/-2 rule for the d20 system (i.e. favorable
		// conditions grant up to a +2 bonus)
		// It's not an official rule, but we're using it here.
		if (mob.isSitting)
			check += 1;
		if (mob.isProne)
			check += 2;
		if (mob.isLyingDown)
			check += 2;

		// This mobile is now hiding. Hidden mobiles do not show up on the list
		// of room occupants
		// and they do not broadcast SPECIFIC (i.e. show their name) messages
		// when leaving or arriving
		// into a room. However, other mobiles can defeat this with a spot check
		// that is higher than this
		// mobile's hide check. A successful spot check will show the mobile on
		// the room occupant list as well
		// as show his leaving/arrival messages in rooms. All of this is handled
		// in the direction command
		// methods (north, south, etc) and the look command. However, spotting
		// and listening work like search:
		// there are commands for each and only then will a mobile possibly be
		// able to see/hear a hiding mobile.
		//
		// In addition, see CMD_movesilently(). This allows a mobile to move
		// silently. If the mob is
		// NOT moving silently but is hiding, vague room leave/arrive messages
		// will be displayed.
		// Move silently removes the broadcasts.

		mob.hideCheck = check;
		mob.increaseMovementMultiplier(1); // the mobile should move a bit
											// "slower" when hiding.
		res
				.setText("[R][WHITE]You are now [B][YELLOW]hiding[R][WHITE]! (check: "
						+ check + ")");
		res.setSuccessful(true);

		return res;
	}

	private CommandResult CMD_movesilently() {
		CommandResult res = new CommandResult();
		res
				.setFailText("[R][WHITE]You don't seem to be one for sneaking around silently...");

		Mobile mob = (Mobile) sender;

		// is the mobile already sneaking about? if so, stop doing it.
		if (mob.moveSilentlyCheck > 0) {
			res.setText("[R][WHITE]You stop moving silently.");
			mob.moveSilentlyCheck = 0;
			res.setSuccessful(true);
			return res;
		}

		// apparently they're not hiding. try to set up the skill.
		SkillList skills = mob.getSkillList();
		Skill moveSilently = skills.getSkillByName("move silently");

		// can they even move silently?
		if (moveSilently == null) {
			return res;
		}

		// can't move silently while fighting!
		if (mob.isFighting) {
			res
					.setFailText("[B][YELLOW]Moving around silently won't help in combat![R][WHITE]");
			return res;
		}

		int check = moveSilently.makeCheck();

		// It will be necessary to implement penalties based on the terrain the
		// mobile is in.
		// That is later though. The d20 SRD specifies the following penalties
		// for terrain:
		// Noisy (scree, shallow or deep bog, undergrowth, dense rubble): -2
		// penalty
		// Very noisy (dense undergrowth, deep snow): -5 penalty

		// This mobile is now moving silently. Mobiles sneaking about do not
		// broadcast room leave/arrive
		// messages. However, other mobiles can defeat this with a listen check
		// that is higher than this mobile's
		// move silently check. A successful listen check will cause the mobile
		// to broadcast arrive/leave messages
		// in the room of the listeninng mobile. All of this is handled in the
		// direction command methods
		// (north, south, etc) and the look command. However, listening works
		// like search:
		// there is a listen command and it resets every time a mobile goes into
		// a new room. Once the command is
		// activated, only then will a mobile possibly be able to hear a moving
		// silently mobile.
		//
		// In addition, see CMD_hide(). This allows a mobile to hide. If the mob
		// is
		// NOT hiding but is moving silently, they will show up on room occupant
		// lists with a look
		// command. However, there will be broadcasting of arrive/leave
		// messages.

		mob.moveSilentlyCheck = check;
		mob.increaseMovementMultiplier(1); // the mobile should move a bit
											// "slower" when moving silently.
		res
				.setText("[R][WHITE]You are now [B][YELLOW]moving silently[R][WHITE]! (check: "
						+ check + ")");
		res.setSuccessful(true);

		return res;
	}

	private CommandResult CMD_spot() {
		CommandResult res = new CommandResult();
		res.setFailText("You spot something off in the distance.");

		Mobile mob = (Mobile) sender;
		// is our spotter blind? if so, he can't really spot anything can he?
		if (mob.isBlind) {
			res
					.setFailText("[R][WHITE]You might have better luck spotting things if you weren't blind...");
			return res;
		}

		// ok, try to set up the skill
		SkillList skills = mob.getSkillList();
		Skill spot = skills.getSkillByName("spot");

		// do they even have spot?
		if (spot == null)
			return res;

		// ok, so they do...
		int check = spot.makeCheck();
		mob.spotCheck = check;
		res
				.setText("[R][WHITE]You begin scanning the area for anything interesting... (check: "
						+ check + ")");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_listen() {
		CommandResult res = new CommandResult();
		res.setFailText("You listen for the sounds of the world.");

		Mobile mob = (Mobile) sender;
		// is our listener deaf? if so, he can't really listen for anything can
		// he?
		if (mob.isDeaf) {
			res
					.setFailText("[R][WHITE]You might have better luck listening for things if you weren't deaf...");
			return res;
		}

		// ok, try to set up the skill
		SkillList skills = mob.getSkillList();
		Skill listen = skills.getSkillByName("listen");

		// do they even have listen?
		if (listen == null)
			return res;

		// ok, so they do...
		int check = listen.makeCheck();
		mob.listenCheck = check;
		res
				.setText("[R][WHITE]You begin listening intently for any interesting (or suspicious) sounds... (check: "
						+ check + ")");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_emote(CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("Emote error.");
		params.init(Command.TXT);

		String emoteText = params.paramString();

		// did they actually type something to emote?
		if (emoteText == null) {
			res.setFailText("[R][GREEN]What do you want to emote?[WHITE]");
			return res;
		}

		// so they did.
		Mobile mob = (Mobile) sender;
		emoteText = mob.getName() + " " + emoteText;

		// broadcast to the world and player.
		World.notifyPlayersAtLocation(mob, emoteText);
		res.setText(emoteText);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_amote(CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("A-emote error.");
		params.init(Command.TXT);

		String emoteText = params.paramString();

		// did they actually type something to emote?
		if (emoteText == null) {
			res
					.setFailText("[R][GREEN]What do you want to apostrophe emote?[WHITE]");
			return res;
		}

		// so they did.
		Mobile mob = (Mobile) sender;
		emoteText = mob.getName() + "'s " + emoteText;

		// broadcast to the world and player.
		World.notifyPlayersAtLocation(mob, emoteText);
		res.setText(emoteText);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_who(CommandParameters params) {
		CommandResult res = new CommandResult();
		res
				.setFailText("[R][WHITE]Please use the command 'help who' for information on how to use the who command.");

		// Parse the parameters.
		// Current acceptable parameters include the following:
		// class names, player names, race names, individual level (a number), a
		// level range (#-#),
		// rp, lfg, chaotic, lawful, neutral, good, evil, god, staff, immortal,
		// mortal,
		// forger, greater, lesser, admin, areas, rpgod, code, zone, invis
		// (<-THIS LINE FOR GODS ONLY!!)

		// IF AN ILLEGAL PARAMETER IS FOUND, simply return res and it will
		// return the failed
		// CommandResult with the error message. Illegal parameters are:
		// Anything that's not in the above list.
		// Anything a player should not have access to (i.e. mortals doing
		// "who zone" or "who admin")

		// Produce a String that meets all acceptable parameters.
		// To do this, run through the list of all players and test their whotag
		// against the parameter list string of
		// this command. A player's whotag has the following format:
		// "playername racename level classname zonename ethical moral <god status> <player set status> <admin set status>"
		// For example:
		// "ao human 13 fighter Waterdeep neutral evil forger rp code areas"

		// A simple indexOf test or something similar against all whotags will
		// allow us to construct the list of
		// players meeting the user's selected parameters.

		// Set the CommandResult's text to the string.

		// Return the result.
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_equipment() {
		CommandResult res = new CommandResult();
		res.setFailText("[R][WHITE]You are wearing:\nNothing.");

		String equipment = "[R][WHITE]You are wearing:\n";
		Mobile mob = (Mobile) sender;
		Equipment mobEquipment = mob.getEquipment();

		if ((mobEquipment == null) || (mobEquipment.size() == 0))
			return res;

		for (Item item : mobEquipment) {
			if (item != null) {
				equipment += "<worn on " + item.getPartWornOn().getName()
						+ "> " + item.getName() + "\n";
			}
		}

		res.setText(equipment);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_attributes() {
		CommandResult res = new CommandResult();

		Mobile mob = (Mobile) sender;

		String attributes = "[R][WHITE]Character Attributes for [B][GREEN]"
				+ mob.getName()
				+ "[R][WHITE]:\nStrength: "
				+ mob.getStat(Mobile.STRENGTH)
				+ " Intelligence: "
				+ mob.getStat(Mobile.INTELLIGENCE)
				+ " Wisdom: "
				+ mob.getStat(Mobile.WISDOM)
				+ " Dexterity: "
				+ mob.getStat(Mobile.DEXTERITY)
				+ " Charisma: "
				+ mob.getStat(Mobile.CHARISMA)
				+ " Constitution: "
				+ mob.getStat(Mobile.CONSTITUTION)
				+ "\n\nSaving Throws: Fort [B][YELLOW]+0[R][WHITE] Ref [B][YELLOW]+0[R][WHITE] Will [[B][YELLOW]+0]"
				+ "\n[R][WHITE]Spell Resistance: [MAGENTA]0[WHITE] Armor Class: [B][WHITE]"
				+ mob.getAC() + "[R][WHITE]";

		res.setText(attributes);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_levelup() {
		CommandResult res = new CommandResult();
		System.out.println("1...");

		Mobile mob = (Mobile) sender;
		System.out.println("2...");

		mob.levelup();
		System.out.println("3...");
		res.setText("Leveled up...");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_prepare(CommandParameters params) {
		params.init(Command.TXT);
		CommandResult res = new CommandResult();
		Mobile mob = (Mobile) sender;
		String spellNameAsString = params.paramString();

		// Did they actually even send a spell?
		if (spellNameAsString == null) {
			// This later needs to be changed to prepare the quickprep list.
			res.setFailText("[R][GREEN]Prepare what?");
			return res;
		}

		System.out.println("Spell name: " + spellNameAsString);

		// Check to see if the mobile has the spell.
		SpellList spells = mob.getMobileClass().getAvailableSpells();
		System.out.println("1....");
		Spell spell = spells.getSpellByName(spellNameAsString);
		System.out.println("2....");

		if (spell == null) {
			res
					.setFailText("[GREEN]You don't have that spell in your spellbook.");
			return res;
		}

		// If they do, check to see if they have an available spell slot.
		// THIS NEEDS TO BE IMPLEMENTED !!!

		// Are they actually able to prepare the spell? (Not in battle, not
		// incapacitated)?
		// ALONG WITH THIS!!!

		// Memorize it!
		SpellList memorizedSpells = mob.getMobileClass().getMemorizedSpells();
		System.out.println("3....");

		// Need to implement some sort of countdown or something!!
		System.out.println("spelllist: " + memorizedSpells);
		mob.increaseLockTime(2);
		mob.setLockMessage("[GREEN]You are busy preparing [B]"
				+ spell.getName() + "[R]![WHITE]");
		mob.setLockFinishedMessage("[GREEN]You finish preparing [B]"
				+ spell.getName() + "[R]![WHITE]");
		System.out.println(memorizedSpells.addSpell(spell));
		System.out.println("3.5....");
		res.setText("[GREEN]You begin studying [B]" + spell.getName()
				+ "[R].[WHITE]");
		res.setSuccessful(true);
		System.out.println("4!");

		return res;
	}

	/*
	 * private CommandResult CMD_bash(CommandParameters params) {
	 * params.init(Command.CMD); Object t = params.getParameter(0);
	 * 
	 * CommandResult res = new CommandResult();
	 * res.setFailText("[R][WHITE]You don't see that here.");
	 * 
	 * //Do they even have the skill? Mobile mob = (Mobile)sender; MobileClass
	 * mobClass = mob.getMobileClass(); ClassFeature bash =
	 * mobClass.getFeatureByName("bash");
	 * 
	 * if (bash == null) {
	 * res.setFailText("[R][WHITE]You don't have that class feature."); return
	 * res; }
	 * 
	 * //Target stuff. try { Affectable ta = (Affectable)t; if
	 * (ta.getEntityType() == Affectable.NON_LIVING) {
	 * res.setFailText("[R][WHITE]You can't bash that!"); return res; } } catch
	 * (Exception e) {return res;}
	 * 
	 * 
	 * //If all conditions are met, make the bash attack roll and then if
	 * successful, bash the target. Affectable target = (Affectable)t; boolean
	 * hit = mob.attack(target, true); if (hit) { bash.registerTarget(target);
	 * bash.activate(); res.setText("[R][WHITE]" + target.getName() +
	 * " is knocked prone by your powerful [B]BASH[R]!");
	 * res.setSuccessful(true); }
	 * 
	 * else { res.setFailText("[R][WHITE]Your bash misses " + target.getName());
	 * } return res; }
	 * 
	 * private CommandResult CMD_rage() { CommandResult res = new
	 * CommandResult();
	 * res.setFailText("[R][WHITE]You can't go into a rage right now.");
	 * 
	 * //Do they even have the class feature? Mobile mob = (Mobile)sender;
	 * MobileClass mobClass = mob.getMobileClass();
	 * 
	 * ClassFeature rage = mobClass.getFeatureByName("rage");
	 * System.out.println("instantiated rage CF");
	 * 
	 * if (rage == null) {
	 * res.setFailText("[R][WHITE]You don't have that class feature."); return
	 * res; }
	 * 
	 * System.out.println("Found the feature");
	 * 
	 * //No target stuff, so directly on to the effects!
	 * System.out.println("activating...."); rage.registerTarget(mob);
	 * rage.registerTimer((mob.getModifier(Mobile.CONSTITUTION) > 0) ? (3 *
	 * (mob.getModifier(Mobile.CONSTITUTION) + 2)) : 3);
	 * System.out.println("set target"); rage.activate();
	 * System.out.println("activated");
	 * 
	 * 
	 * res.setText("[R][WHITE]You fly into a [B][RED]bloodthirsty rage![R][WHITE]"
	 * ); res.setSuccessful(true);
	 * 
	 * return res; }
	 */

	private CommandResult CMD_detect(String[] detectWhat) {
		return null;
	}

	private CommandResult CMD_setdesc(CommandParameters params) {
		CommandResult res = new CommandResult();
		params.init(Command.TXT);
		String text = params.paramString();
		res.setFailText("[R][WHITE]You didn't enter anything!");
		if (text == null)
			return res;
		Mobile mob = (Mobile) sender;
		mob.setLongDescription(text);
		res.setText("[R][WHITE]Description changed to: " + text);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_save() {
		CommandResult res = new CommandResult();
		res
				.setFailText("[B][RED]SAVING FAILED!! PLEASE NOTIFY AN ADMINISTRATOR!![R][WHITE]");
		Mobile mob = (Mobile) sender;

		MobileLoader.saveMobile(mob.getName() + ".mob", mob);

		res.setText("[R][WHITE]Character saved.");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_load() {
		CommandResult res = new CommandResult();
		res.setFailText("Character loading failed.");

		Mobile mob = (Mobile) sender;

		try {
			XMLDecoder e = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(mob.getName() + ".xml")));
			PlayerCharacter r = (PlayerCharacter) e.readObject();
			mob = r;
		}

		catch (Exception e) {
			System.out.println("Exception");
		}

		res.setText("Character loaded.");
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_tickerlist() {
		CommandResult res = new CommandResult();
		res.setFailText("[B][RED]FATAL ERROR.[R][WHITE]");
		String resText = "";

		Ticker ticker = World.getWorld().getTicker();
		resText = "[R][WHITE]Ticker Information for **WORLD TICKER**:\n";
		resText += ticker.tickerList();

		res.setText(resText);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_deaf() {
		CommandResult res = new CommandResult();
		Mobile mob = (Mobile) sender;
		if (mob.isDeaf)
			mob.isDeaf = false;
		else
			mob.isDeaf = true;
		res.setFailText("Couldn't make you deaf (or not deaf).");
		res.setText("Your deafness: " + mob.isDeaf);
		res.setSuccessful(true);
		return res;
	}

	private CommandResult CMD_blind() {
		CommandResult res = new CommandResult();
		Mobile mob = (Mobile) sender;
		if (mob.isBlind)
			mob.isBlind = false;
		else
			mob.isBlind = true;
		res.setFailText("Couldn't make you blind (or not blind).");
		res.setText("Your blindess: " + mob.isBlind);
		res.setSuccessful(true);
		return res;
	}
}
