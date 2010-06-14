package ring.items;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.mobiles.Body;
import ring.nrapi.business.BusinessObject;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((idlePrefix == null) ? 0 : idlePrefix.hashCode());
		result = prime * result
				+ ((idleSuffix == null) ? 0 : idleSuffix.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((partWornOn == null) ? 0 : partWornOn.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idlePrefix == null) {
			if (other.idlePrefix != null)
				return false;
		} else if (!idlePrefix.equals(other.idlePrefix))
			return false;
		if (idleSuffix == null) {
			if (other.idleSuffix != null)
				return false;
		} else if (!idleSuffix.equals(other.idleSuffix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partWornOn == null) {
			if (other.partWornOn != null)
				return false;
		} else if (!partWornOn.equals(other.partWornOn))
			return false;
		return true;
	}
}
