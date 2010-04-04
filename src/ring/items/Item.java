package ring.items;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.mobiles.Body;
import ring.nrapi.business.AbstractBusinessObject;
import ring.mobiles.BodyPart;
import ring.persistence.RingConstants;
import ring.world.WorldObject;
import ring.world.WorldObjectMetadata;
import ring.effects.Effect;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"description",
	"cursed",
	"partWornOn",
	"idlePrefix",
	"idleSuffix"
})
public class Item extends WorldObject {
	private BodyPart partWornOn;
	private String name;
	private String description;
	private String idlePrefix;
	private String idleSuffix;
	
	@XmlElement(name = "bodyPart")
	public BodyPart getPartWornOn() {
		return partWornOn;
	}
	
	public void setPartWornOn(BodyPart part) {
		partWornOn = part;
	}

	@XmlElement
	public String getName() {
		return name;
	}
	
	@XmlElement
	public boolean isCursed() {
		// TODO Auto-generated method stub
		return false;
	}

	@XmlTransient
	public Effect getPassiveEffects() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setIdlePrefix(String idlePrefix) {
		this.idlePrefix = idlePrefix;
	}

	@XmlElement
	public String getIdlePrefix() {
		return idlePrefix;
	}

	public void setIdleSuffix(String idleSuffix) {
		this.idleSuffix = idleSuffix;
	}

	@XmlElement
	public String getIdleSuffix() {
		return idleSuffix;
	}

	public static void main(String[] args) {
		Item i = new Item();
		i.setID("item1");
		i.setPartWornOn(Body.FACE);
		System.out.println(i.toXMLDocument());
	}

	@Override
	public WorldObjectMetadata getMetadata() {
		WorldObjectMetadata metadata = new WorldObjectMetadata();
		metadata.setName(getName());
		return metadata;
	}
}
