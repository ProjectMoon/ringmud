package ring.nrapi.data;

public class DataStoreFactory {
	public static DataStore getDefaultStore() {
		return new ExistDBStore();
	}
}
