package ring.commands.spell;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Prepare implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		throw new UnsupportedOperationException("Due to NRAPI, prepare needs to be reimplemented");
		/*
		params.init(CommandType.TEXT);
		CommandResult res = new CommandResult();
		Mobile mob = (Mobile) sender;
		String spellNameAsString = params.paramString();

		// Did they actually even send a spell?
		if (spellNameAsString == null) {
			// This later needs to be changed to prepare the quickprep list.
			res.setFailText("[R][GREEN]Prepare what?");
			return res;
		}

		// Check to see if the mobile has the spell.
		SpellList spells = mob.getMobileClass().getAvailableSpells();
		Spell spell = spells.getSpellByName(spellNameAsString);

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

		// Need to implement some sort of countdown or something!!
		mob.increaseLockTime(2);
		mob.setLockMessage("[GREEN]You are busy preparing [B]" + spell.getName() + "[R]![WHITE]");
		mob.setLockFinishedMessage("[GREEN]You finish preparing [B]" + spell.getName() + "[R]![WHITE]");
		memorizedSpells.addSpell(spell);
		res.setText("[GREEN]You begin studying [B]" + spell.getName() + "[R].[WHITE]");
		res.setSuccessful(true);

		return res;
		*/
	}

	public String getCommandName() {
		return "prepare";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
