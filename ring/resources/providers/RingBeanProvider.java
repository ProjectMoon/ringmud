package ring.resources.providers;

import java.util.Map;

import ring.resources.beans.RingBean;

/**
 * Interface representing a backend bean store. A RingBeanProvider provides
 * read/write capabilities for its specific bean type.
 * @author projectmoon
 *
 * @param <B>
 */
public interface RingBeanProvider<B extends RingBean> {
	/**
	 * Returns a Map of all the beans that this provider is capable of getting.
	 * @return
	 */
	public Map<String, B> loadBeans();
}
