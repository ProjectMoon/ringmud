package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.RingConstants;
import ring.nrapi.entities.Entity;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"model",
	"zoneID"
})
public class Room extends AbstractBusinessObject {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
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
	public void save() {
		//XMLDataStoreFactory.getDefaultStore().storeAggregate(super.getAggregate());		
	}
	
	public void addEntity(Entity ent) {
		entities.add(ent);
		getModel().addEntityID(ent.getID());
	}
	
	public boolean removeEntity(Entity ent) {
		getModel().removeEntityID(ent.getID());
		return entities.remove(ent);
	}
		
	public String generateBlindExitsString(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public String generateRandomOccupantsList(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
}
