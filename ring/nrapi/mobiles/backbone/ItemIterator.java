package ring.nrapi.mobiles.backbone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ring.nrapi.entities.Item;

public class ItemIterator implements Iterator<Item> {
	private int pos;
	private List<Item> items;
	
	
	public ItemIterator(Inventory inv) {
		pos = 0;
		initInventory(inv);
	}
	
	public ItemIterator(Equipment equipment) {
		pos = 0;
		initEquipment(equipment);
	}
	
	private void initEquipment(Equipment eq) {
		items = new ArrayList<Item>(eq.size());
		
		for (Item item : eq.equipment.values()) {
			items.add(item);
		}
	}
	
	private void initInventory(Inventory inv) {
		items = new ArrayList<Item>(inv.size());
		
		for (Item item : inv.getItemList()) {
			items.add(item);
		}
	}
	
	public boolean hasNext() {
		return (pos < items.size());
	}

	public Item next() {
		return items.get(pos++);
	}

	public void remove() {
		throw new UnsupportedOperationException("this iterator does not support removal");
	}

}
