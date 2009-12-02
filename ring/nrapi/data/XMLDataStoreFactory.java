package ring.nrapi.data;

public class XMLDataStoreFactory {
	public static XMLDataStore getDefaultStore() {
		return new ExistDBStore();
	}
}
