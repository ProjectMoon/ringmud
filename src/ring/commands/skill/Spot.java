package ring.commands.skill;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.skills.Skill;
import ring.skills.SkillList;

public class Spot implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		CommandResult res = new CommandResult();
		res.setFailText("You spot something off in the distance.");

		Mobile mob = (Mobile) sender;
		// is our spotter blind? if so, he can't really spot anything can he?
		if (mob.isBlind) {
			res.setFailText("[R][WHITE]You might have better luck spotting things if you weren't blind...");
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
		res.setText("[R][WHITE]You begin scanning the area for anything interesting... (check: " + check + ")");
		res.setSuccessful(true);
		return res;
	}

	public String getCommandName() {
		return "spot";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
