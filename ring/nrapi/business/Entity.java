package ring.nrapi.business;

import ring.nrapi.entities.EntityAggregate;

public class Entity extends AbstractBusinessObject<EntityAggregate> {
	private EntityAggregate agg;
	
	@Override
	public boolean load(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void save() {
		throw new UnsupportedOperationException();
	}

}
