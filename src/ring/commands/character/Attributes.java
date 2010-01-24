package ring.commands.character;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Attributes implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();

		Mobile mob = (Mobile) sender;

		String attributes = "[R][WHITE]Character Attributes for [B][GREEN]"
				+ mob.getBaseModel().getName()
				+ "[R][WHITE]:\nStrength: "
				+ mob.getBaseModel().getStrength()
				+ " Intelligence: "
				+ mob.getBaseModel().getIntelligence()
				+ " Wisdom: "
				+ mob.getBaseModel().getWisdom()
				+ " Dexterity: "
				+ mob.getBaseModel().getDexterity()
				+ " Charisma: "
				+ mob.getBaseModel().getCharisma()
				+ " Constitution: "
				+ mob.getBaseModel().getConstitution()
				+ "\n\nSaving Throws: Fort [B][YELLOW]+0[R][WHITE] Ref [B][YELLOW]+0[R][WHITE] Will [[B][YELLOW]+0]"
				+ "\n[R][WHITE]Spell Resistance: [MAGENTA]0[WHITE] Armor Class: [B][WHITE]"
				+ mob.getCombatModel().getCurrentAC() + "[R][WHITE]";

		res.setText(attributes);
		res.setSuccessful(true);
		return res;

	}

	public String getCommandName() {
		return "attributes";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
