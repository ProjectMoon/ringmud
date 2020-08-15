package ring.mobiles.backbone;

import ring.items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class representing a Mobile's inventory. Really just a fancy List containing Item objects.
 * @author projectmoon
 *
 */
public class Inventory implements Iterable<Item>, Serializable {
	
	public static final long serialVersionUID = 1;
	
	private List<Item> inv;
	private int capacity;
	
	public Inventory() {
		inv = new ArrayList<Item>(30);
		capacity = 30;
	}
	
	public Inventory(int capacity) {
		inv = new ArrayList<Item>(capacity);
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public List<Item> getItems() {
		return inv;
	}
	
	public void setItems(List<Item> items) {
		inv = items;
	}
	
	public int size() {
		return inv.size();
	}
	
	/**
	 * A useful method that returns an via a partial or full name.
	 * Good for finding items that players type in to the command line.
	 * @param name
	 * @return
	 */
	public Item getItemByName(String name) {
		//1 character (or 0?) isn't much to go off of...
		if (name.length() < 2) 
			return null;
		
		name = name.toLowerCase();
	
		for (Item item : inv) {
			if (item.getName().toLowerCase().indexOf(name) != -1) {
				return item;
			}
		}
	
		//Couldn't find anything.
		return null;
	}
	
	public boolean removeItem(Item item) {
		if (!item.isCursed()) {
			return inv.remove(item);
		}
		else {
			return false;
		}
	}
	
	public boolean addItem(Item item) {
		inv.add(item);
		return true;
	}

	public Iterator<Item> iterator() {
		return new ItemIterator(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((inv == null) ? 0 : inv.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (capacity != other.capacity)
			return false;
		if (inv == null) {
			if (other.inv != null)
				return false;
		} else if (!inv.equals(other.inv))
			return false;
		return true;
	}

}
