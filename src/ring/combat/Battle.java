package ring.combat;

import ring.mobiles.*;
import ring.world.*;
import java.util.*;

public class Battle implements TickerListener {
	//This class abstracts combat in the MUD. It handles the tick-by-tick autoattacks of combat. It also distributes
	//experience points to those who win the Battle. Combat is a bit hard to visualize through an object-oriented
	//programming language, but we have come up with the following guidelines:
	//
	//* A Battle is initiated when a mobile (NPC or PC) executes the "kill" command or does a hostile action.
	
	//* Once initiated, the Battle is created with the "kill" command executor as the initiator and puts both the
	//	the initiator and his target as involved participants with 100% xp modifiers.
	
	//* Any other mobile that attacks either combatant is added into the Battle with a 0% xp modifier.
	//	This is to prevent non-grouped powerleveling. It also doesn't matter to NPCs since they don't level.
	//	A special case is made for groups. If, when joining a Battle, the attacker is in the initiator's group,
	//	Their XP modifier is set to (100/# players in group)%.
	
	//* NPCs can only be involved in one Battle instance at a time, players may have infinite amounts. This will
	//	hopefully reduce server resource usage.
	
	//* This class implements TickerListener to handle combat.
	
	//* When a combatant dies, XP is calculated based on its CR (level for players) and is distributed to all other
	//	participants that attempted to attack that combatant. Each participant's XP modifier will change how much
	//	XP they receive. It is then further changed based on their level relative to their target's CR.
	//	CR > participant's level = more XP, participant's level > CR = less xp. The cap for no XP is 8 levels in both
	//	directions.
	//	We are currently investigating a bell curve function to model this XP change.
	
	//* If a mobile flees from combat, they are removed from the Battle. If they later return to attack the target
	//	again, they are re-joined with the appropriate XP modifier: If they are the initiator or are part of the
	//	initiator's group, their XP modifier is reduced by 5% (note this means changing the number by a percentage,
	//	not directly subtracting 5 from the number). If they are not, their modifier is set to 0%. 
	//	Fleeing is only necessary when being attacked. Mobiles not being attacked may freely leave the room using 
	//	direction commands and remain in the Battle.
	
	//* If a mobile is not present in the room for a kill, they still get the XP as long as they actively attempted
	//	to kill the target or support the people doing so. "Supporting" includes healing, buff spells, and the like.
	//	

	//Constants: Useful constants.
	public static int MAX_PARTICIPANTS = 30;
	
	//Class variables: These are all of the variables needed by this class.
	private Vector<Mobile> mobList; //a list of the mobiles (i.e. map keys)
	private TreeMap<Mobile, Double> xpModifiers; //a list of all the mobiles' xp modifiers.
	private Mobile initiator; //the person who started this Battle. they may or may not be a participant.
	
	//######################
	//CONSTRUCTORS
	//######################
	
	//This constructor is just used to initialize the internal variables.
	//It is not called publicly, just by the other two public constructors... code re-use and all that.
	private Battle() { 
		mobList = new Vector<Mobile>(20);
		xpModifiers = new TreeMap<Mobile, Double>();
	}
	
	//this sets up a Battle with just an initiator.	
	public Battle(Mobile initiator) {
		this();
		this.initiator = initiator;
		//add the initiator.
		addParticipant(initiator, 100.0);
	}
	
	//this is the most common constructor, giving us an initiator and other participants (variable arg list for those)
	public Battle(Mobile initiator, Mobile ... others) {
		this();
		this.initiator = initiator;
		
		//add the participants
		addParticipant(initiator, 100.0);
		for (Mobile mob : others) {
			addParticipant(mob, 100.0);
		}
	}
	
	//######################
	//ADD AND REMOVE METHODS
	//######################
	
	//addParticipant method.
	//This method adds a participant to the Battle with the given XP modifier. It returns true if successful,
	//but false if the participant was not able to be added (if the mobile is dead or already in the Battle) or
	//if mob is null.
	public boolean addParticipant(Mobile mob, double xpModifier) {
		if (mob == null) return false;
		if (mob.getBaseModel().isDead()) return false;
		if (mobList.contains(mob)) return false;
		
		//all of the conditions check out so add the participant and return true.
		mobList.addElement(mob);
		xpModifiers.put(mob, xpModifier);
		return true;
	}

	//removeParticipant method.
	//This method removes a participant from the Battle. It returns true if successful. Currently there are no
	//conditions that would prevent removal that need to be specific to this function. Things such as the mobile
	//not existing are handled by exceptions thrown from Vector and TreeMap.
	public boolean removeParticipant(Mobile mob) {
		mobList.removeElement(mob);
		xpModifiers.remove(mob);
		return true;
	}
	
	//######################
	//COMBAT METHODS
	//######################
	
	//processTick method.
	//This method handles all auto-attacking between combatants. It also checks for dead status and handles
	//removal and XP payout if a target is dead.
	public void processTick(TickerEvent e) {
		//first, check death states
		for (Mobile mob : mobList) {
			if (mob.getBaseModel().isDead()) {
				giveXP(mob); //give the xp of this mobile to all participants, factoring in the xp modifiers
				removeParticipant(mob);
			}
		}
		
		//then do attacks.
		for (Mobile mob : mobList) {
			boolean hit = mob.attack(mob.getCombatModel().getTarget(), true); //later factor in the weapon they have.
			//if (hit) mob.sendData("You hit " + mob.getCombatModel().getTarget() + " for some damage."); //implement damage here!
		}
	}
	
	//giveXP method.
	//This method gives XP to other participants from the passed mobile. The mobile being passed is likely dead and thus
	//other combatants are getting XP for his death. XP modifiers are factored in here.
	private void giveXP(Mobile mob) {
		for (Mobile m : mobList) {
			if (mob != m) { //make sure we're not giving a mob XP for its own death...
				//TODO re-add mob exp calcuation back in here.
				m.gainXP(xpModifiers.get(m).intValue());
			}
		}
	}
	
}
