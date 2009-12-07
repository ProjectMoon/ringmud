package ring.server.callbacks;

import ring.server.Communicator;
import ring.server.Server;

public class DefaultCallback implements Callback {
	public void execute(CallbackEvent event) {
		if (event.equals(CallbackEvent.CONNECTED)) {
			System.out.println("DLC: incrementing connections");
			Server.incrementConnections();
		}
		else if (event.wasDisconnected()) {
			System.out.println("DLC: decrementing connections.");
			Server.decrementConnections();
		}
	}
}
