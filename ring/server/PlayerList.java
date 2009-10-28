package ring.server;

import ring.mobiles.Mobile;
import ring.players.PlayerCharacter;

import java.util.List;
import java.util.ArrayList;

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
			if (player.getType() == Mobile.MORTAL) {
				mortals.add(player);
			}
		}
		
		return mortals;
	}
	
	public List<PlayerCharacter> getAdmins() {
		ArrayList<PlayerCharacter> admins = new ArrayList<PlayerCharacter>();
		for (PlayerCharacter player : getPlayers()) {
			if (player.getType() >= Mobile.LESSER_GOD) {
				admins.add(player);
			}
		}
		
		return admins;
	}
}
