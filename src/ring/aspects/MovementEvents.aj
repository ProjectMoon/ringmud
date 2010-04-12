package ring.aspects;

import ring.commands.interaction.RoomLookAction;
import ring.events.EventDispatcher;
import ring.events.EventEnum;
import ring.mobiles.Mobile;
import ring.mobiles.senses.StimulusSender;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.movement.*;

/**
 * Aspect for dealing with movement events. The movement system itself is only concerned with
 * the logic of moving a {@link Mobile} from one place to another. This aspect deals with the crosscutting
 * concern firing program events and sending stimuli to other players when a given mobile moves.
 * @author projectmoon
 *
 */
public aspect MovementEvents percflow(call(static boolean LocationManager.move(..))) {
	public enum MovementEvent implements EventEnum {
		ROOM_ON_LEAVES("onMobileLeaves"), ROOM_ON_ARRIVES("onMobileArrives"), MOBILE_ON_MOVE("onMove");

		private String name;
		private MovementEvent(String name) { this.name = name; }
		@Override
		public String getEventName() {
			return name;
		} 
	}
	
	private Mobile mobile;
	private Room leavingFrom;
	private Room enteringInto;
	
	/**
	 * Send leave messages to the room the mobile has left.
	 * @param to
	 */
	private void sendLeaveMessages(Portal to) {
		// reset the character's search, listen, and spot checks.
		//TODO pending skills rewrite
		
		// default arrive and leave text to broadcast to others
		String leaveText = mobile.getBaseModel().getName() + " [R][WHITE]leaves to the "
				+ to.getDisplayName() + ".";
		
		//Send visual notification to those in the room.
		VisualStimulus stim = new VisualStimulus();
		stim.setDepiction(leaveText);
		stim.setBlindDepiction("You hear the sounds of someone leaving."); //TODO deaf characters should not process this.
		StimulusSender.sendStimulus(leavingFrom, stim, mobile);
	}
	
	/**
	 * Send arrive messages to the room the mobile has entered.
	 * @param from
	 */
	private void sendArriveMessages(Portal from) {		
		// default arrive and leave text to broadcast to others
		String arrivesFrom = from.getDisplayName();
		
		//Reverse only the names of standard directions.
		if (LocationManager.isStandardDirection(arrivesFrom)) {
			arrivesFrom = LocationManager.getOppositeDirection(arrivesFrom);
		}
		
		String arriveText = mobile.getBaseModel().getName() + " [R][WHITE]arrives from the "
				+ arrivesFrom + ".";


		//Reset certain skill checks.
		//TODO pending skills rewrite

		//Send visual notification to all in the room.
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction(arriveText);
		stimulus.setBlindDepiction("You hear the sounds of someone arriving."); //TODO deaf characters should not have this register.
		StimulusSender.sendStimulus(enteringInto, stimulus, mobile);
		
		// subtract the right amount of move points.
		mobile.getDynamicModel().changeCurrentMV(-1);
	}
	
	/**
	 * Pointcut for when a mobile leaves a room. Sets up initial state by
	 * gathering the mobile and its current location (which is the location it
	 * is leaving). The advice associated with this pointcut is executed around
	 * the default removeMobile method. It then delegates to the regular method.
	 * @param mob The mobile leaving.
	 */
	pointcut mobileLeaves(Mobile mob):
		(call(boolean Room.removeMobile(Mobile)) && args(mob)) &&
		withincode(static boolean LocationManager.move(Mobile, Portal));

	boolean around(Mobile mob): mobileLeaves(mob) {
		mobile = mob;
		leavingFrom = mob.getLocation();
		return proceed(mob);
	}
	
	/**
	 * Pointcut that describes a mobile entering into a room. Since the
	 * mobile was already gathered in the {@link #mobileLeaves(Mobile)} pointcut,
	 * we are only interested in the actual room the mobile is entering. The advice
	 * for this poincut is executed around the regular setLocation method. It then
	 * delegates to the default setLocation method.
	 * @param room
	 */
	pointcut mobileEnters(Room room):
		(call(void Mobile.setLocation(Room)) && args(room)) &&
		withincode(static boolean LocationManager.move(Mobile, Portal));
	
	void around(Room room): mobileEnters(room) {
		enteringInto = room;
		proceed(room);
	}
	
	/**
	 * Pointcut that matches when the LocationManager's move method is done
	 * executing. Used by the advice below to complete this aspect.
	 * @param destination
	 */
	pointcut finish(Portal destination):
		call(static boolean LocationManager.move(Mobile, Portal)) &&
		args(*, destination);
	
	/**
	 * Advice that implements the firing of events based on whether the move was
	 * successful or not.
	 * @param destination The Portal that was passed to <code>LocationManager.move()</code>.
	 * @param success If the move was successful or not.
	 */
	after(Portal destination) returning(boolean success): finish(destination) {
		if (success) {
			//Send messages to other players.
			sendLeaveMessages(destination);
			sendArriveMessages(destination);
			
			//Do a look command for the player.
			new RoomLookAction(mobile).doLook();
			
			//Send internal events.
			EventDispatcher.dispatch(MovementEvent.ROOM_ON_LEAVES, leavingFrom, mobile);
			EventDispatcher.dispatch(MovementEvent.ROOM_ON_ARRIVES, enteringInto, mobile);
			EventDispatcher.dispatch(MovementEvent.MOBILE_ON_MOVE, mobile, leavingFrom, enteringInto, destination);
		}
	}
}
