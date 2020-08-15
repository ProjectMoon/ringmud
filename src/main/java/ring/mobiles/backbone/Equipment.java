package ring.mobiles.backbone;

import ring.items.Item;
import ring.mobiles.BodyPart;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the equipment a mobile is currently wearing. Because
 * BodyParts are immutable, this class is necessary so that mobiles can actually
 * wear things.
 * 
 * @author projectmoon
 * 
 */
public class Equipment implements Iterable<Item>, Serializable {
	public static final long serialVersionUID = 1;
	
	/**
	 * Inner class for transforming this object into something JAXB
	 * can understand.
	 * @author projectmoon
	 *
	 */
	public static class EquipmentTuple {
		private BodyPart part;
		private Item item;
		
		public EquipmentTuple() {}
		public EquipmentTuple(BodyPart part, Item item) {
			this.part = part;
			this.item = item;
		}

		public BodyPart getBodyPart() { return part; }
		public void setBodyPart(BodyPart part) { this.part = part; }

		public Item getItem() { return item; }
		public void setItem(Item item) { this.item = item; }
	}
	
	protected HashMap<BodyPart, Item> equipment = new HashMap<BodyPart, Item>();

	public Equipment() {}

	public List<EquipmentTuple> getEntries() {
		List<EquipmentTuple> entries = new ArrayList<EquipmentTuple>(equipment.keySet().size());
		for (BodyPart part : equipment.keySet()) {
			EquipmentTuple tuple = new EquipmentTuple(part, equipment.get(part));
			entries.add(tuple);
		}
		
		return entries;
	}
	
	public void setEntries(List<EquipmentTuple> entries) {
		for (EquipmentTuple tuple : entries) {
			equipment.put(tuple.getBodyPart(), tuple.getItem());
		}
	}

	public Item getItem(BodyPart part) {
		return equipment.get(part);
	}

	public void putItem(BodyPart part, Item item) {
		item.setPartWornOn(part);
		equipment.put(part, item);
	}

	public Item removeItem(BodyPart part) {
		Item item = equipment.remove(part);
		if (item != null) {
			//item.setPartWornOn(null);
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
		return new ItemIterator(this);
	}
	
	public Collection<Item> getItems() {
		return equipment.values();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((equipment == null) ? 0 : equipment.hashCode());
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
		Equipment other = (Equipment) obj;
		if (equipment == null) {
			if (other.equipment != null)
				return false;
		} else if (!equipment.equals(other.equipment))
			return false;
		return true;
	}

}
