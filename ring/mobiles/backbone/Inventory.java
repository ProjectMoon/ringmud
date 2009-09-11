package ring.mobiles.backbone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ring.entities.Item;

/**
 * A class representing a Mobile's inventory. Really just a fancy List containing Item objects.
 * @author projectmoon
 *
 */
public class Inventory implements Iterable<Item>, Serializable {
	public static final long serialVersionUID = 1;
	
	protected List<Item> inv;
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
		return inv.remove(item);
	}
	
	//TODO later this will need weight checks or something.
	public boolean addItem(Item item) {
		inv.add(item);
		return true;
	}
	
	public void setItem(Item[] items) {
		for (Item item : items) {
			inv.add(item);
		}
	}
	
	public void setItem(int i, Item item) {
		inv.add(i, item);
	}
	
	public Item getItem(int index) {
		return inv.get(index);
	}

	public Iterator<Item> iterator() {
		return new ItemIterator(this);
	}

}
