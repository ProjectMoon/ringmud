package ring.commands.skill;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.skills.Skill;

public class Listen implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		throw new UnsupportedOperationException("Listen to be implemented in Python");
		/*
		CommandResult res = new CommandResult();
		res.setFailText("You listen for the sounds of the world.");

		Mobile mob = (Mobile) sender;
		// is our listener deaf? if so, he can't really listen for anything can
		// he?
		if (mob.isDeaf) {
			res.setFailText("[R][WHITE]You might have better luck listening for things if you weren't deaf...");
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
		res.setText("[R][WHITE]You begin listening intently for any interesting (or suspicious) sounds... (check: "	+ check + ")");
		res.setSuccessful(true);
		return res;
		*/
	}

	public String getCommandName() {
		return "listen";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
