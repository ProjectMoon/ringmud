package ring.resources.beans;

import java.util.HashMap;

/**
 * A class that represents the set of all item beans loaded into the MUD. Used by the
 * JOX parser to store item beans during the parse process.
 * @author projectmoon
 *
 */
public class ItemBeanSet {
	public static final long serialVersionUID = 1;
	
	private HashMap<String, ItemBean> itemSet = new HashMap<String, ItemBean>();
	
	public void copyFrom(ItemBeanSet otherSet) {
		itemSet.putAll(otherSet.itemSet);
	}
	
	public void setItem(ItemBean[] items) {
		for (ItemBean bean : items) {
			itemSet.put(bean.getID(), bean);
		}
	}
	
	public ItemBean[] getItem() {
		return itemSet.values().toArray(new ItemBean[0]);
	}
	
	
}