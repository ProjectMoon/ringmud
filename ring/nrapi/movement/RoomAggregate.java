package ring.nrapi.movement;

import ring.nrapi.aggregate.AbstractResourceAggregate;
import ring.nrapi.aggregate.ResourceAggregate;

public class RoomAggregate extends AbstractResourceAggregate implements ResourceAggregate {
	private RoomModel model;
	private Zone zone;
	
	public RoomModel getModel() {
		return model;
	}
	
	public void setModel(RoomModel model) {
		this.model = model;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
}
