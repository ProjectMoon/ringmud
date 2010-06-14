package ring.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.BusinessObject;
import ring.persistence.RingConstants;

/**
 * This class is used to group Rooms and Portals together for use by the
 * LocationManager and storage in the database. It is not typically declared
 * in game code. The only place it will generally be found in user code is in
 * Python data files.
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
public class Location extends BusinessObject {
	private Room room;
	private List<Portal> exits = new ArrayList<Portal>();

	public Location() {}
	
	@XmlElement
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	@XmlElement(name = "exit")
	public List<Portal> getExits() {
		return exits;
	}
	
	public void setExits(List<Portal> exits) {
		this.exits = exits;
	}
	
	public void addExit(Portal port) {
		exits.add(port);
	}
}
