package ring.resources.loaders;

import java.util.List;

import ring.resources.RingResource;

/**
 * Interface representing a resource loader for RingMUD. Classes implementing this
 * interface are what provide the various pieces of building-block content of the MUD
 * in some form. Authors who wish to create custom ResourceLoaders should not implement
 * this interface directly, but rather extend AbstractResourceLoader, which simplifies
 * the task.
 * @author projectmoon
 *
 * @param <T>
 */
public interface ResourceLoader<T extends RingResource<?>> {
	/**
	 * Gets a resource given a bean ID.
	 * @param beanID
	 * @return The resource, if found, null otherwise.
	 * @throws IllegalStateException if the beans have not yet been loaded.
	 */
	public T getResource(String beanID) throws IllegalStateException;
	
	/**
	 * Returns a list of all resources from this resource loader.
	 * @return The list of resources.
	 * @throws IllegalStateException if the beans have not yet been loaded.
	 */
	public List<T> getAllResources() throws IllegalStateException;
	
	/**
	 * Refreshes the specified resource with the latest bean in this
	 * resource loader's cache.
	 * @param resource
	 */
	public void refresh(T resource) throws IllegalStateException;
	
	/**
	 * Returns whether or not the beans have been loaded into this resource
	 * loader.
	 * @return true or false
	 */
	public boolean areBeansLoaded();
}
