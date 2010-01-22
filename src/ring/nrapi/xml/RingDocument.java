package ring.nrapi.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.entities.Entity;
import ring.nrapi.items.Item;
import ring.nrapi.mobiles.Mobile;
import ring.nrapi.movement.Room;
import ring.persistence.RingConstants;

/**
 * Object representation for a RingMUD XML document. Used to
 * validate imports.
 * @author projectmoon
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "ring")
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"rooms",
	"entities",
	"mobiles",
	"items"
})
public class RingDocument {
	private List<Mobile> mobiles = new ArrayList<Mobile>();
	private List<Room> rooms = new ArrayList<Room>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Item> items = new ArrayList<Item>();
	
	@XmlElement(name = "mobile")
	public List<Mobile> getMobiles() {
		return mobiles;
	}
	
	public void setMobiles(List<Mobile> mobiles) {
		this.mobiles = mobiles;
	}
	
	@XmlElement(name = "room")
	public List<Room> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	
	@XmlElement(name = "entity")
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	@XmlElement(name = "item")
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
