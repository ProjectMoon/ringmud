package ring.nrapi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;
import ring.nrapi.mobiles.BodyPart;
import ring.effects.Effect;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"cursed",
	"armor",
	"partWornOn"
})
public class Item extends Entity {

	public void setPartWornOn(BodyPart part) {
		// TODO Auto-generated method stub
		
	}

	@XmlElement
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@XmlElement
	public boolean isCursed() {
		// TODO Auto-generated method stub
		return false;
	}

	@XmlElement
	public boolean isArmor() {
		// TODO Auto-generated method stub
		return false;
	}

	@XmlTransient
	public Effect getPassiveEffects() {
		// TODO Auto-generated method stub
		return null;
	}

	@XmlElement(name = "bodyPart")
	public BodyPart getPartWornOn() {
		// TODO Auto-generated method stub
		return null;
	}

}
