package ring.commands.spell;

import java.util.logging.Logger;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.commands.CommandParameters.CommandType;
import ring.effects.Affectable;
import ring.effects.Effect;
import ring.mobiles.Mobile;
import ring.spells.Spell;
import ring.spells.SpellList;
import ring.world.WorldObject;

public class Cast implements Command {
	private static final Logger log = Logger.getLogger(Cast.class.getName());

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		params.init(CommandType.SPELL);

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
			log.fine("[" + sender.toString() + "] Casting: step 1");
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

			log.fine("[" + sender.toString() + "] Casting: step 2");
			Affectable target = (Affectable) t;
			Effect e = spell.generateEffect();
			e.setTarget(target);

			// is target vald?
			// does target exist?
			// casting...

			// remove spell from memory
			// casting delay
			// check the resistance and stuff...
			log.fine("[" + sender.toString() + "] Casting: step 3");
			mob.sendData("[R][WHITE]You begin chanting...\n");
			e.begin();
			log.fine("[" + sender.toString() + "] Casting: started effect");
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

	public String getCommandName() {
		return "cast";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
