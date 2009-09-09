package ring.mobiles.backbone;

import java.util.HashMap;
import java.util.Iterator;

import ring.entities.Item;
import ring.mobiles.BodyPart;

/**
 * This class represents the equipment a mobile is currently wearing. Because
 * BodyParts are immutable, this class is necessary so that mobiles can actually
 * wear things.
 * 
 * @author projectmoon
 * 
 */
public class Equipment implements Iterable<Item> {
	protected HashMap<BodyPart, Item> equipment;

	public Equipment() {
		equipment = new HashMap<BodyPart, Item>();
	}

	public Item getItem(BodyPart part) {
		return equipment.get(part);
	}

	public void setItem(BodyPart part, Item item) {
		item.setPartWornOn(part);
		equipment.put(part, item);
	}

	public Item removeItem(BodyPart part) {
		Item item = equipment.remove(part);
		if (item != null) {
			item.setPartWornOn(null);
			return item;
		} else {
			return null;
		}
	}

	public Item getItemByName(String name) {
		if (name.length() < 2)
			return null;
		
		name = name.toLowerCase();
	

		for (Item item : equipment.values()) {
			if (item.getName().toLowerCase().indexOf(name) != -1)
				return item;
		}
		
		//Nothing to return, so return null
		return null;
	}

	public boolean hasItem(BodyPart part) {
		return equipment.containsKey(part);
	}

	public int size() {
		return equipment.size();
	}

	public Iterator<Item> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
