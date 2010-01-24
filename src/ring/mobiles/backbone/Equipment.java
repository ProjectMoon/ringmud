package ring.mobiles.backbone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.items.Item;
import ring.mobiles.BodyPart;
import ring.persistence.RingConstants;

/**
 * This class represents the equipment a mobile is currently wearing. Because
 * BodyParts are immutable, this class is necessary so that mobiles can actually
 * wear things.
 * 
 * @author projectmoon
 * 
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"entries"
})
public class Equipment implements Iterable<Item>, Serializable {
	public static final long serialVersionUID = 1;
	
	/**
	 * Inner class for transforming this object into something JAXB
	 * can understand.
	 * @author projectmoon
	 *
	 */
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlType
	public static class EquipmentTuple {
		private BodyPart part;
		private Item item;
		
		public EquipmentTuple() {}
		public EquipmentTuple(BodyPart part, Item item) {
			this.part = part;
			this.item = item;
		}
		
		@XmlElement
		public BodyPart getBodyPart() { return part; }
		public void setBodyPart(BodyPart part) { this.part = part; }
		
		@XmlElement
		public Item getItem() { return item; }
		public void setItem(Item item) { this.item = item; }
	}
	
	protected HashMap<BodyPart, Item> equipment = new HashMap<BodyPart, Item>();

	public Equipment() {}
	
	@XmlElement(name = "entry")
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

}
