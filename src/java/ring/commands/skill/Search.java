package ring.commands.skill;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.Portal;
import ring.movement.PortalNotFoundException;
import ring.movement.Room;
import ring.skills.Skill;

//TODO refactor search
public class Search implements Command {

	public void execute(CommandSender sender, CommandArguments params) {
		throw new UnsupportedOperationException("Search to be implemented in python");
		/*
		params.init(CommandType.TEXT);
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

			try {
				Portal checkPort = mob.getLocation().getPortal(dir);
				// first, look for hidden exits and set the mobile's current hidden
				// exit search check to the result of the
				// search check.
				mob.hiddenExitSearchCheck = check;
				if (check >= checkPort.getSearchDC())
					searchText += "Hidden Exits: " + dir + "\n";
				
			}
			//we can silently ignore this because there's simply nothing to find.
			catch (PortalNotFoundException e) {}
			
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
		*/
	}

	public String getCommandName() {
		return "search";
	}

	public void rollback() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCommandName(String name) {
		// TODO Auto-generated method stub
		
	}

}
