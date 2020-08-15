package ring.persistence;

import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.movement.Room;
import ring.movement.Zone;
import ring.players.Player;
import ring.players.PlayerCharacter;

import java.io.File;

public class DataStoreFactory {
	public static DataStore getDefaultStore() {
		return new DataStore() {
			@Override
			public boolean storePersistable(Persistable p) {
				return false;
			}

			@Override
			public boolean importDocument(File file) {
				return false;
			}

			@Override
			public boolean removeDocument(String docID) {
				return false;
			}

			@Override
			public void setLoadpoint(Loadpoint point) {

			}

			@Override
			public Loadpoint getLoadpoint() {
				return null;
			}

			@Override
			public Entity retrieveEntity(String id) {
				return null;
			}

			@Override
			public Item retrieveItem(String id) {
				return null;
			}

			@Override
			public Room retrieveRoom(String id) {
				return null;
			}

			@Override
			public Zone retrieveZone(String id) {
				return null;
			}

			@Override
			public Mobile retrieveMobile(String id) {
				return null;
			}

			@Override
			public Player retrievePlayer(String id) {
				return null;
			}

			@Override
			public PlayerCharacter retrievePlayerCharacter(String id) {
				return null;
			}
		};
	}
}
