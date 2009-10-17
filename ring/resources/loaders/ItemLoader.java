package ring.resources.loaders;

import java.util.ArrayList;
import java.util.List;

import ring.entities.Armor;
import ring.entities.Item;
import ring.entities.Weapon;
import ring.resources.beans.ItemBean;
import ring.resources.providers.ItemProvider;

public class ItemLoader extends AbstractResourceLoader<ItemBean, Item, ItemProvider> implements ResourceLoader<Item> {

	@Override
	public Item getResource(String beanID) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			ItemBean bean = super.getBean(beanID);
			Item i = createItem(bean.getItemType());
			
			i.populateFromBean(bean);
			return i;
		}
		else {
			throw new IllegalStateException(this + " getResource: beans are not yet loaded!");
		}
	}
	
	private Item createItem(String type) throws IllegalArgumentException {
		if (type.equalsIgnoreCase("armor")) {
			return new Armor();
		}
		else if (type.equalsIgnoreCase("weapon")) {
			return new Weapon();
		}
		else if (type.equalsIgnoreCase("other")) {
			throw new UnsupportedOperationException("cannot create \"other\" typed items yet.");
		}
		else {
			throw new IllegalArgumentException("Item type" + type + " is not a valid item type!");
		}
	}

	@Override
	public void refresh(Item resource) throws IllegalStateException {
		if (super.areBeansLoaded()) {
			resource.populateFromBean(super.getBean(resource.getBeanID()));
		}
		else {
			throw new IllegalStateException(this + " refresh: beans are not yet loaded!");
		}
	}
	
	@Override
	public List<Item> getAllResources() throws IllegalStateException {
		if (super.areBeansLoaded()) {
			ArrayList<Item> list = new ArrayList<Item>(super.getBeanAmount());
			
			for (ItemBean bean : super.getBeans()) {
				Item i = createItem(bean.getItemType());
				i.populateFromBean(bean);
				list.add(i);
			}
			
			return list;
		}
		else {
			throw new IllegalStateException(this + ": getAllResources: beans are not yet loaded!");
		}
	}

}
