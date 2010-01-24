package ring.commands.character;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;

public class Score implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		// Make the CommandResult object.
		CommandResult res = new CommandResult();
		res.setFailText("[B]FATAL ERROR IN SCORE METHOD.[R]");
		Mobile mob = (Mobile) sender;
		String score = "\n[R][WHITE]Score and Statistics for [B][GREEN]"
				+ mob.getBaseModel().getName()
				+ "[R][WHITE]"
				+ "\nAlignment: "
				+ mob.getBaseModel().getAlignment().toString()
				+ "\nHP: [B][RED]"
				+ mob.getCombatModel().getCurrentHPString()
				+ "/"
				+ mob.getCombatModel().getMaxHPString()
				+ " [R][WHITE]Class: "
				+ mob.getBaseModel().getMobileClass().getDisplayName()
				+ " [R][WHITE]Level: [B][MAGENTA]"
				+ mob.getBaseModel().getLevel()
				+ "\nCurrent XP Amount: [YELLOW]0% [WHITE]Guild: [B][GREEN]None[R][WHITE]";

		res.setText(score);
		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "score";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
