package ring.movement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.persistence.RingConstants;

/**
 * This class is used to group Rooms and Portals together for use by the
 * LocationManager and storage in the database.
 * @author projectmoon
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"room",
	"exits"
})
public class Location extends AbstractBusinessObject {
	private Room room;
	private List<Portal> exits;
	
	@Override
	public void createChildRelationships() {
		
	}
	
	@XmlElement
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	@XmlElement
	public List<Portal> getExits() {
		return exits;
	}
	
	public void setExits(List<Portal> exits) {
		this.exits = exits;
	}
}
