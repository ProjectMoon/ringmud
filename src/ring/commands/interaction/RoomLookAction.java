package ring.commands.interaction;

import ring.mobiles.Mobile;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.movement.Room;

/**
 * Class to handle looking at a room. Centralized in one place because several different commands
 * or actions make use of it.
 * @author projectmoon
 *
 */
public class RoomLookAction {
	private Mobile mob;
	
	public RoomLookAction(Mobile mob) {
		this.mob = mob;
	}
	
	public void doLook() {
		Room room = mob.getLocation();

		String lookText = "[CYAN]" + room.getModel().getTitle() + "[R]\n"
			+ room.getModel().getDescription() + "\n"
			+ room.getExitsString(0)
			+ "\n" + room.getMobileList(mob, 0)
			+ room.getEntityList();
			
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction(lookText);
		stimulus.setBlindDepiction("You see nothing, for you are blind!");
		
		mob.getDynamicModel().getSensesGroup().consume(stimulus);
	}
	
	public Mobile getMobile() {
		return mob;
	}
}
