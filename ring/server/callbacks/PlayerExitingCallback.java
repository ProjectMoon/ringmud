package ring.server.callbacks;

import ring.players.PlayerCharacter;
import ring.server.Server;

public class PlayerExitingCallback implements Callback {
	private PlayerCharacter player;
	
	public PlayerExitingCallback(PlayerCharacter pc) {
		player = pc;
	}
	
	public void execute(CallbackEvent event) {
		//Save player.
		System.out.println("PEC: decrementing connections");
		
		//Remove player.
		
		//Decrement connections.
		Server.decrementConnections();
	}
}
