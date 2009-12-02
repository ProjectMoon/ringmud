package ring.nrapi.business;

import ring.nrapi.aggregate.ResourceAggregate;

public interface BusinessObject<T extends ResourceAggregate> {
	public boolean load(String id);
	public boolean loadFromAggregate(T aggregate);
	public void save();
}
