package ring.senses.handlers;

import ring.mobiles.senses.DepictionHandler;
import ring.mobiles.senses.ProcessedDepiction;
import ring.server.Communicator;

/**
 * Depicts a ProcessDepiction of a Stimulus to a player.
 * Practically speaking, it forwards text to the player's
 * communicator object.
 * @author projectmoon
 *
 */
public class PlayerDepictionHandler implements DepictionHandler {
	private Communicator communicator;
	
	public PlayerDepictionHandler() {}
	
	public PlayerDepictionHandler(Communicator comms) {
		communicator = comms;
	}
	
	@Override
	public void handle(ProcessedDepiction depiction) {
		communicator.println(depiction.getDepiction());
	}
	
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void setCommunicator(Communicator comms) {
		communicator = comms;
	}
	
}
