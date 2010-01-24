package ring.commands.skill;

import ring.commands.Command;
import ring.commands.CommandParameters;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.skills.Skill;

public class MoveSilently implements Command {

	public CommandResult execute(CommandSender sender, CommandParameters params) {
		throw new UnsupportedOperationException("Move silently to be implemented in python");
		/*
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
		res.setText("[R][WHITE]You are now [B][YELLOW]moving silently[R][WHITE]! (check: "
						+ check + ")");
		res.setSuccessful(true);

		return res;
		*/
	}

	public String getCommandName() {
		return "movesilently";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

}
