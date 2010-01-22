package ring.persistence;

public class DataStoreFactory {
	public static DataStore getDefaultStore() {
		return new ExistDBStore();
	}
}
