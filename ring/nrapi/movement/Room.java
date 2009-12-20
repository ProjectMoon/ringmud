package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.RingConstants;
import ring.nrapi.entities.Entity;
import ring.nrapi.mobiles.Mobile;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"model",
	"entities",
	"zoneID"
})
public class Room extends AbstractBusinessObject {
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Mobile> mobiles = new ArrayList<Mobile>();
	
	private RoomModel model = new RoomModel();
	private Zone zone;
	
	@XmlElement
	public RoomModel getModel() {
		return model;
	}
	
	public void setModel(RoomModel model) {
		this.model = model;
	}
	
	@XmlElement
	public String getZoneID() {
		return "testID";
		//return zone.getID();
	}
	
	public void setZoneID(String id) {
		//TODO implement setZoneID
		//if zone already exists, compare the two, and change if necessary
		//else load new zone corresponding to ID and attempt to set zone to that.		
	}
	
	@XmlTransient
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
		setZoneID(zone.getID());
	}
	
	@Override
	public void createChildRelationships() {
		for (Entity ent : entities) {
			ent.setParent(this);
		}
		
		//TODO implement cPR for Mobiles in Room.
	}
	
	@XmlElementWrapper(name = "entities")
	@XmlElement(name = "entity")
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
		createChildRelationships();
	}	
	
	@XmlElementWrapper(name = "mobiles")
	@XmlElement(name = "mobile")
	public List<Mobile> getMobiles() {
		return mobiles;
	}
	
	public void setMobiles(List<Mobile> mobiles) {
		this.mobiles = mobiles;
	}
	
	public void addMobile(Mobile mob) {
		mobiles.add(mob);
		mob.setParent(this);
	}
	
	public boolean removeMobile(Mobile mob) {
		if (mobiles.contains(mob)) {
			mob.setParent(null);
			return mobiles.remove(mob);
		}
		else {
			return false;
		}
	}
	
	public void addEntity(Entity ent) {
		entities.add(ent);
		ent.setParent(this);
	}
	
	public boolean removeEntity(Entity ent) {
		if (entities.contains(ent)) {
			ent.setParent(null);
			return entities.remove(ent);
		}
		else {
			return false;
		}
	}
		
	public String generateBlindExitsString(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public String generateRandomOccupantsList(int searchCheck) {
		throw new UnsupportedOperationException();
	}
}
