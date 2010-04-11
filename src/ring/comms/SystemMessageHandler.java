package ring.comms;

/**
 * This class specifically handles "system messages" sent to players.
 * Unlike CommandResults or sense stimuli, system messages operate in
 * the scope of the player, rather than his/her character. Currently,
 * each {@link ring.players.Player} has a SystemMessageHandler attached
 * to it. This is subject to change in future builds, but the methodology
 * of using the SystemMessageHandler will be the same.
 * <br/><br/>
 * The SystemMessageHandler is very similar in usage to the Senses API.
 * Messages sent are interjected into the target client's current prompt
 * instead of being a defined result of a command.
 * @author projectmoon
 *
 */
public class SystemMessageHandler {
	private Communicator comms;
	
	public SystemMessageHandler(Communicator comms) {
		this.comms = comms;
	}
	
	public void sendMessage(String message) {
		comms.printWithPreline(message);
	}
	
	public void sendError(String error) {
		sendMessage(error);
	}
}
