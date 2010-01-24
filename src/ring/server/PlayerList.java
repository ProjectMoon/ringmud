package ring.server;

import java.util.ArrayList;
import java.util.List;

import ring.mobiles.MobileBaseModel;
import ring.players.PlayerCharacter;

/**
 * Class that holds logged-in player information.
 * @author jeff
 */
public class PlayerList {
	//TODO set capacity to max connections.
	private List<PlayerCharacter> players = new ArrayList<PlayerCharacter>();
	
	public PlayerList() {
		
	}
	
	public void addPlayer(PlayerCharacter player) {
		players.add(player);
	}
	
	public boolean removePlayer(PlayerCharacter player) {
		return players.remove(player);
	}
	
	public List<PlayerCharacter> getPlayers() {
		return players;
	}
	
	public List<PlayerCharacter> getMortals() {
		ArrayList<PlayerCharacter> mortals = new ArrayList<PlayerCharacter>();
		
		for (PlayerCharacter player : getPlayers()) {
			if (player.getBaseModel().getType() == MobileBaseModel.Type.MORTAL) {
				mortals.add(player);
			}
		}
		
		return mortals;
	}
	
	public List<PlayerCharacter> getAdmins() {
		ArrayList<PlayerCharacter> admins = new ArrayList<PlayerCharacter>();
		for (PlayerCharacter player : getPlayers()) {
			if (player.getBaseModel().getType().isGod()) {
				admins.add(player);
			}
		}
		
		return admins;
	}
}
