package ring.nrapi.business;

import ring.nrapi.aggregate.ResourceAggregate;

public abstract class AbstractBusinessObject<T extends ResourceAggregate> implements BusinessObject<T> {
	private T agg;
	
	public abstract boolean load(String id);
	public abstract void save();
	
	public boolean loadFromAggregate(T aggregate) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns the aggregate's ID.
	 * @return
	 */
	protected String getID() {
		return agg.getID();
	}
	
	/**
	 * Protected-level method to get the aggregate. Currently all business objects go in one
	 * package, so protected is used. May change to public later.
	 * @return
	 */
	protected T getAggregate() {
		return agg;
	}
	
	/**
	 * Sets the aggregate.
	 * @param agg
	 */
	protected void setAggregate(T agg) {
		this.agg = agg;
	}
}
