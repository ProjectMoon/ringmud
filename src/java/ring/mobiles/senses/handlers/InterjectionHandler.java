package ring.mobiles.senses.handlers;

import ring.comms.Communicator;
import ring.mobiles.senses.DepictionHandler;
import ring.mobiles.senses.ProcessedDepiction;

/**
 * Depicts a ProcessDepiction of a Stimulus to a player.
 * Practically speaking, it forwards text to the player's
 * communicator object.
 * @author projectmoon
 *
 */
public class InterjectionHandler implements DepictionHandler {
	private Communicator communicator;
	
	public InterjectionHandler() {}
	
	public InterjectionHandler(Communicator comms) {
		communicator = comms;
	}
	
	@Override
	public void handle(ProcessedDepiction depiction) {
		//Use printNoSuffix here, otherwise we get more than one
		//prompt.
		//communicator.printNoSuffixWithPreline(depiction.getDepiction());
		communicator.interject(depiction.getDepiction());
	}
	
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void setCommunicator(Communicator comms) {
		communicator = comms;
	}
	
}
