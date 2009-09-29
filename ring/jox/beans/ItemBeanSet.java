package ring.jox.beans;

import ring.entities.Item;

/**
 * A class that represents the set of all items loaded into the MUD. Used by the
 * JOX parser to store item beans during the parse process.
 * @author projectmoon
 *
 */
public class ItemBeanSet {
	public void setItem(int index, Item i) {
		throw new UnsupportedOperationException();
	}
	
	public Item getItem(int index) {
		throw new UnsupportedOperationException();
	}
	
	public void setItem(Item[] items) {
		throw new UnsupportedOperationException();	
	}
	
	public Item[] getItem() {
		throw new UnsupportedOperationException();
	}
	
	
}
