package ring.resources.loaders;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ring.resources.RingResource;
import ring.resources.beans.RingBean;
import ring.resources.providers.RingBeanProvider;

/**
 * Abstract resource loader than handles certain menial tasks of resource loading.
 * Provides methods to extending classes for bean management. Also allows extending
 * classes to enforce very specific generic typing via three generic parameters.
 * Users implementing their own resource loaders should extend this class, and not
 * implement ResourceLoader directly.
 * @author projectmoon
 *
 * @param <B> The resource bean type.
 * @param <R> The resource type.
 * @param <P> The bean provider type.
 */
public abstract class AbstractResourceLoader<B extends RingBean, R extends RingResource<B>, P extends RingBeanProvider<B>> implements ResourceLoader<R> {
	//Empty HashMap by default to suppress any null pointer exceptions.
	//If the beans are not loaded, illegal state exceptions will be thrown instead.
	protected Map<String, B> beans = new HashMap<String, B>();
	private P provider;
	private boolean beansLoaded = false;

	public abstract R getResource(String beanID) throws IllegalStateException;
	public abstract List<R> getAllResources() throws IllegalStateException;
	public abstract void refresh(R resource) throws IllegalStateException;	
	
	public final boolean areBeansLoaded() {
		return beansLoaded;
	}
	
	/**
	 * Sets the backend bean provider for this resource loader.
	 * @param provider
	 */
	protected final void setProvider(P provider) {
		this.provider = provider;
	}
	
	/**
	 * Gets the backend bean provider for this resource loader.
	 * @return
	 */
	public final P getProvider() {
		return provider;
	}
	
	/**
	 * Gets a bean from the bean store.
	 * @param beanID
	 * @return the bean if found, null otherwise.
	 */
	protected final B getBean(String beanID) {
		return beans.get(beanID);
	}
	
	/**
	 * Returns the number of beans in the cache.
	 * @return the number of beans in the cache.
	 */
	protected final int getBeanAmount() {
		return beans.size();
	}
	
	/**
	 * Gets all beans in the cache in the form of a Collection.
	 * @return Collection
	 */
	protected final Collection<B> getBeans() {
		return beans.values();
	}
	
	/**
	 * Calls the backend bean provider's loadBeans method.
	 */
	protected final void loadBeans() {
		synchronized (beans) {
			beans = provider.loadBeans();
			beansLoaded = true;
		}
	}
}
