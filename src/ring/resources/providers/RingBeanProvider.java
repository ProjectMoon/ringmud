package ring.resources.providers;

import java.util.Map;

import ring.resources.beans.RingBean;

/**
 * Interface representing a backend bean store. A RingBeanProvider provides
 * read/write capabilities for its specific bean type. Classes that implement
 * this interface can load beans in all kinds of different ways: XML files,
 * databases, etc.
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
