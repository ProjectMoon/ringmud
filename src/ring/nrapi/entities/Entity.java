package ring.nrapi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"descriptor"
})
/**
 * Entities are inanimate, immobile objects that sit in Rooms. They are,
 * in essence, very limited Mobiles. An Entity cannot be destroyed, but it
 * can be interacted with.
 * @author projectmoon
 *
 */
public class Entity extends AbstractBusinessObject {
	private String name;
	private String descriptor;
	
	@Override
	public void createChildRelationships() {
		//Does nothing
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	@XmlElement
	public String getDescriptor() {
		return descriptor;
	}
}
