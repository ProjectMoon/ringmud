package ring.commands.skill;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.skills.Skill;

public class Hide implements Command {

	public void execute(CommandSender sender, CommandParameters params) {
		throw new UnsupportedOperationException("Hide to be implemented in Python");
		/*
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
		res.setText("[R][WHITE]You are now [B][YELLOW]hiding[R][WHITE]! (check: "
						+ check + ")");
		res.setSuccessful(true);

		return res;
		*/
	}

	public String getCommandName() {
		return "hide";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
