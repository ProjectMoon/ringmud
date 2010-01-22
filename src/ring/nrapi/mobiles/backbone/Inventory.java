package ring.nrapi.mobiles.backbone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.items.Item;
import ring.persistence.RingConstants;

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
	
	@XmlElement
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
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

}
