package ring.server.shells;

import java.util.List;

import ring.comms.Communicator;
import ring.persistence.DataStoreFactory;
import ring.players.Player;
import ring.players.PlayerCharacter;

/**
 * Presents a list of characters for a player to choose from.
 * @author projectmoon
 *
 */
public class PlayerCharacterList {
	private Communicator comms;
	private Player player;
		
	public PlayerCharacterList(Communicator comms, Player player) {
		this.comms = comms;
		this.player = player;
	}
	
	public PlayerCharacter doPlayerList() {
		comms.println("Select your character:");
		PlayerCharacter pc = null;
		
		List<String> characterIDs = player.getCharacterIDs();
		
		int c = 0;
		for (String id : characterIDs) {
			comms.println((c + 1) + ". " + id);
			c++;
		}
		
		comms.println();
		while (pc == null) {
			comms.print("Enter the number of your desired character: ");
			String input = comms.receiveData();
			comms.println();
			
			int index = 0;
			
			//First check: Make sure it's actually a number.
			try {
				index = Integer.parseInt(input);
			}
			catch (NumberFormatException e) {
				comms.println("[B][RED]Please enter a number.");
				continue; //lol controlling flow in a catch block
			}
			
			//Second check: Make sure it's within range.
			if (index < 1 || index > c) {
				comms.println("[B][RED]Please enter a number between 1 and " + (c + 1));
				continue;
			}
			
			index--; //computers count from zero!
			
			//Finally, load the PlayerCharacter.
			String pcID = characterIDs.get(index);
			pc = DataStoreFactory.getDefaultStore().retrievePlayerCharacter(pcID);
		}
		
		return pc;
	}
}
