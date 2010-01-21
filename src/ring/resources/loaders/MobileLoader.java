package ring.resources.loaders;

import java.util.ArrayList;
import java.util.List;

import ring.mobiles.Mobile;
import ring.resources.beans.MobileBean;
import ring.resources.providers.MobileProvider;

public class MobileLoader extends AbstractResourceLoader<MobileBean, Mobile, MobileProvider> implements ResourceLoader<Mobile> {

	@Override
	public Mobile getResource(String beanID) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			Mobile m = new Mobile();
			MobileBean bean = super.getBean(beanID);
			m.populateFromBean(bean);
			return m;
		}
		else {
			throw new IllegalStateException(this + " getResource: beans are not yet loaded!");
		}
	}

	@Override
	public void refresh(Mobile resource) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			resource.populateFromBean(super.getBean(resource.getBeanID()));
		}
		else {
			throw new IllegalStateException(this + " refresh: beans are not yet loaded!");
		}
	}
	
	@Override
	public List<Mobile> getAllResources() throws IllegalStateException {
		ArrayList<Mobile> list = new ArrayList<Mobile>(super.getBeanAmount());
		
		for (MobileBean bean : super.getBeans()) {
			Mobile m = new Mobile();
			m.populateFromBean(bean);
			list.add(m);
		}
		
		return list;
	}

}
