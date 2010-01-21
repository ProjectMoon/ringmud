package ring.resources;

import ring.resources.beans.RingBean;

/**
 * An interface representing some kind of external game resource. All must implement
 * this interface's populateFromBean method, where they receive a RingBean of a specific
 * type and take information from it. 
 * @author projectmoon
 *
 */
public interface RingResource<B extends RingBean> {
	/**
	 * Populate this Resource with the bean type it has declared.
	 * @param bean
	 */
	public void populateFromBean(B bean);
	
	/**
	 * Gets the bean ID of this resource. To set this property,
	 * The populateFromBean method should transfer the bean's ID
	 * into the object.
	 * @return The bean ID.
	 */
	public String getBeanID();
}
