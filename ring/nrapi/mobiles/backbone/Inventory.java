package ring.nrapi.mobiles.backbone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;
import ring.nrapi.entities.Item;

/**
 * A class representing a Mobile's inventory. Really just a fancy List containing Item objects.
 * @author projectmoon
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"capacity", "items"
})
public class Inventory implements Iterable<Item>, Serializable {
	//JAXB type for XML serializing of items.
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlType
	public static class ItemCollection {
		private List<Item> items;
		
		public ItemCollection() { items = new ArrayList<Item>(); }
		public ItemCollection (int capacity) { items = new ArrayList<Item>(capacity); }
		
		@XmlElement(name = "item")
		public List<Item> getItems() { return items; }
		public void setItems(List<Item> items) { this.items = items; }
	}
	
	public static final long serialVersionUID = 1;
	
	private ItemCollection inv;
	private int capacity;
	
	public Inventory() {
		inv = new ItemCollection(30);
		capacity = 30;
	}
	
	public Inventory(int capacity) {
		inv = new ItemCollection(capacity);
		this.capacity = capacity;
	}
	
	@XmlElement
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@XmlElement(name = "items")
	public ItemCollection getItems() {
		return inv;
	}
	
	public void setItems(ItemCollection items) {
		inv = items;
	}
	
	public List<Item> getItemList() {
		return inv.items;
	}
	
	public int size() {
		return inv.items.size();
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
	
		for (Item item : inv.items) {
			if (item.getName().toLowerCase().indexOf(name) != -1) {
				return item;
			}
		}
	
		//Couldn't find anything.
		return null;
	}
	
	public boolean removeItem(Item item) {
		if (!item.isCursed()) {
			return inv.items.remove(item);
		}
		else {
			return false;
		}
	}
	
	//TODO later this will need weight checks or something.
	public boolean addItem(Item item) {
		inv.items.add(item);
		return true;
	}
	
	public void setItem(Item[] items) {
		for (Item item : items) {
			inv.items.add(item);
		}
	}
	
	public void setItem(int i, Item item) {
		inv.items.add(i, item);
	}
	
	public Item getItem(int index) {
		return inv.items.get(index);
	}

	public Iterator<Item> iterator() {
		return new ItemIterator(this);
	}

}
