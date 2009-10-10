package ring.resources.loaders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ring.resources.RingResource;
import ring.resources.beans.RingBean;
import ring.resources.providers.RingBeanProvider;

public abstract class AbstractResourceLoader<B extends RingBean, R extends RingResource<B>, P extends RingBeanProvider<B>> implements ResourceLoader<R> {
	protected Map<String, B> beans;
	private P provider;
	private boolean beansLoaded = false;

	public abstract R getResource(String beanID) throws IllegalStateException;
	public abstract List<R> getAllResources() throws IllegalStateException;
	public abstract void refresh(R resource) throws IllegalStateException;	
	
	public final boolean areBeansLoaded() {
		return beansLoaded;
	}
	
	protected final void setProvider(P provider) {
		this.provider = provider;
	}
	
	protected final P getProvider() {
		return provider;
	}
	
	protected final B getBean(String beanID) {
		return beans.get(beanID);
	}
	
	protected final int getBeanAmount() {
		return beans.size();
	}
	
	protected final Collection<B> getBeans() {
		return beans.values();
	}
	
	protected final void loadBeans() {
		beans = provider.loadBeans();
		beansLoaded = true;
	}
}
