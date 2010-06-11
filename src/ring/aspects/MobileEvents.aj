package ring.aspects;

import ring.mobiles.Mobile;
import ring.mobiles.npc.NPC;
import ring.players.PlayerCharacter;

/**
 * Aspect for dealing with movement events. The movement system itself is only concerned with
 * the logic of moving a {@link Mobile} from one place to another. This aspect deals with the crosscutting
 * concern firing program events and sending stimuli to other players when a given mobile moves.
 * @author projectmoon
 *
 */
public aspect MobileEvents perthis(createMob()) {
	pointcut createMob(): (execution(public NPC.new(..)) || execution(public PlayerCharacter.new(..)));
	pointcut constructorFinished(NPC mob): this(mob) && (execution(public NPC.new(..)));

	private Mobile mobile;
	
	public MobileEvents() {
		System.out.println("Created new mobile events aspect.");
	}
	
	after() returning(NPC mob): constructorFinished(mob) {
		System.out.println("What is mob? " + mob);
	}
}
