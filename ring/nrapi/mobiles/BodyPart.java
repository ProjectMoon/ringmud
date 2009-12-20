package ring.nrapi.mobiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder = {
	"name"
})
/**
 * This class represents an individual body part, such as a face, a finger, a
 * head, etc. There are a host of BodyPart constants in the Body class. An
 * individual BodyPart is immutable and thus objects can share many copies of
 * them. Items are not stored in this class. They are rather stored in a list in
 * the Mobile class.
 * 
 * @author projectmoon
 * 
 */
public class BodyPart {
	public static final long serialVersionUID = 1;
	// This class is used for a body part.

	// Description of the body part.
	private String name;

	public BodyPart() {}
	
	public BodyPart(String name) {
		this.name = name;
	}

	// Copy constructor
	public BodyPart(BodyPart other) {
		this.name = other.name;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
