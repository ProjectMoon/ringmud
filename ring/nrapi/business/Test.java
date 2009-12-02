package ring.nrapi.business;

import ring.nrapi.aggregate.TestAggregate;
import ring.nrapi.data.XMLDataStore;
import ring.nrapi.data.XMLDataStoreFactory;
import ring.nrapi.resources.TestResource;

public class Test extends AbstractBusinessObject<TestAggregate> {
	private TestAggregate agg;
	
	@Override
	public boolean load(String id) {
		XMLDataStore store = XMLDataStoreFactory.getDefaultStore();
		TestAggregate agg = store.retrieveTestAggregate(id);
		this.agg = agg;
		return true;
	}
	
	@Override
	public void save() {
		XMLDataStore store = XMLDataStoreFactory.getDefaultStore();
		store.storeAggregate(agg);
	}
	
	public TestResource getTestResource() {
		return agg.getTestResource();
	}

	public void setTestResource(TestResource tr) {
		agg.setTestResource(tr);
	}

}
