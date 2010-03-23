package ring.commands.movement;

import ring.commands.interaction.RoomLookAction;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.movement.PortalNotFoundException;
import ring.movement.Room;

/**
 * A class that all movement commands delegate to. This class takes
 * the sending Movable and the direction it wishes to move in. It handles
 * invoke the actual movement method of LocationManager, and sending visual
 * stimuli to those in the rooms to notify them of leaving/arrival.
 * @author projectmoon
 *
 */
public class MoveAction {
	private Mobile mob;
	private String direction;
	
	public MoveAction(Mobile mob, String direction) {
		this.mob = mob;
		this.direction = direction;
	}
	
	public boolean doMove() throws PortalNotFoundException {
		Portal destination = LocationManager.getPortal(mob.getLocation(), direction);
		Room destinationRoom = destination.getDestination();
		Room currentRoom = mob.getLocation();
		
		synchronized (currentRoom) {
			synchronized (destinationRoom) {
				boolean success = LocationManager.move(mob, destination);
				
				if (success) {
					sendLeaveMessages(currentRoom, destination);
					sendArriveMessages(destinationRoom, destination);
					return true;
				}
				else {
					return false;
				}
			}
		}
	}
	
	private void sendLeaveMessages(Room room, Portal to) {
		System.out.println(mob + " leaves from " + room);
			
		// reset the character's search, listen, and spot checks.
		//TODO pending skills rewrite
		
		// default arrive and leave text to broadcast to others
		String leaveText = mob.getBaseModel().getName() + " [R][WHITE]leaves to the "
				+ to.getDisplayName() + ".";
		
		//Send visual notification to those in the room.
		VisualStimulus stim = new VisualStimulus();
		stim.setDepiction(leaveText);
		stim.setBlindDepiction("You hear the sounds of someone leaving."); //TODO deaf characters should not process this.
		StimulusSender.sendStimulus(room, stim, mob);
	}
	
	private void sendArriveMessages(Room room, Portal from) {
		System.out.println("new room: " + room);
		
		// default arrive and leave text to broadcast to others
		String arriveText = mob.getBaseModel().getName() + " [R][WHITE]arrives from the "
				+ from.getDisplayName() + ".";


		//Reset certain skill checks.
		//TODO pending skills rewrite

		//Send visual notification to all in the room.
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction(arriveText);
		stimulus.setBlindDepiction("You hear the sounds of someone arriving."); //TODO deaf characters should not have this register.
		StimulusSender.sendStimulus(room, stimulus, mob);


		//Send visual stimulus to the user who entered the room.
		//We can delegate to RoomLookAction here.
		new RoomLookAction(mob).doLook();
		
		// subtract the right amount of move points.
		mob.getDynamicModel().changeCurrentMV(-1);
	}
}
