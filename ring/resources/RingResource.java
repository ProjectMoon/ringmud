package ring.resources;

import ring.jox.beans.RingBean;

/**
 * An interface representing some kind of external game resource. All must implement
 * this interface's populateFromBean method, where they receive a RingBean of a specific
 * type and take information from it. 
 * @author projectmoon
 *
 */
public interface RingResource<B extends RingBean<?>> {
	/**
	 * Populate this Resource with the bean type it has declared.
	 * @param bean
	 */
	public void populateFromBean(B bean);
}
