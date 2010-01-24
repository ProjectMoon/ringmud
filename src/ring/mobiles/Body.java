package ring.nrapi.mobiles;

import java.util.*;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.persistence.RingConstants;

/**
 * This class defines a body and equipment slots. Numerous different body parts
 * can be created and all can be added. This gives the MUD a very flexible body
 * system that supports all kinds of creatures, dismemberment, polymorph, mutations,
 * etc.
 * @author projectmoon
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"bodyParts"
})
public class Body implements Serializable {
	public static final long serialVersionUID = 1;

	public static BodyPart FACE = new BodyPart("face");
	public static final BodyPart SHOULDER = new BodyPart("shoulder");	
	public static final BodyPart LEFT_ARM = new BodyPart("left arm");
	public static final BodyPart RIGHT_ARM = new BodyPart("right arm");
	public static final BodyPart LEFT_HAND = new BodyPart("left hand");
	public static final BodyPart RIGHT_HAND = new BodyPart("right hand");
	public static final BodyPart LEFT_LEG = new BodyPart("left leg");
	public static final BodyPart RIGHT_LEG = new BodyPart("right leg");
	public static final BodyPart LEFT_WRIST = new BodyPart("left wrist");
	public static final BodyPart RIGHT_WRIST = new BodyPart("right wrist");
	public static final BodyPart WINGS = new BodyPart("wings");
	public static final BodyPart NORMAL_TAIL = new BodyPart("tail");
	public static final BodyPart LEGS_TAIL = new BodyPart("tail");
	public static final BodyPart TENTACLES = new BodyPart("tentacles");// illithid tentacle.
	public static final BodyPart THUMB = new BodyPart("thumb");
	public static final BodyPart OPPOSABLE_CLAW = new BodyPart("thumb claw");
	public static final BodyPart HEAD = new BodyPart("head");
	public static final BodyPart WAIST = new BodyPart("waist");
	public static final BodyPart NECK = new BodyPart("neck");
	public static final BodyPart LEFT_FOOT = new BodyPart("left foot");
	public static final BodyPart RIGHT_FOOT = new BodyPart("right foot");
	public static final BodyPart LEFT_FINGER = new BodyPart("left finger");
	public static final BodyPart RIGHT_FINGER = new BodyPart("right finger");
	public static final BodyPart LEFT_CLAW = new BodyPart("left claw");
	public static final BodyPart RIGHT_CLAW = new BodyPart("right claw");
	
	// body for stuff like rabbits.
	public static final BodyPart SMALL_BODY = new BodyPart("body");
	
	// normal body that most players have.
	public static final BodyPart MEDIUM_BODY = new BodyPart("body");
	
	// body for stuff like ogres.
	public static final BodyPart LARGE_BODY = new BodyPart("body");
	
	// body for stuff like young dragons.
	public static final BodyPart HUGE_BODY = new BodyPart("body");
	
	// body for stuff like regular dragons.
	public static final BodyPart COLOSSAL_BODY = new BodyPart("body");

	//The list of body parts.
	private List<BodyPart> bodyParts = new ArrayList<BodyPart>();

	// Creates a new body object.
	public Body() {
		bodyParts = new ArrayList<BodyPart>(20);
	}
	
	@XmlElement(name = "part")
	public List<BodyPart> getBodyParts() {
		return bodyParts;
	}
	
	public void setBodyParts(List<BodyPart> parts) {
		bodyParts = parts;
	}

	/**
	 * Adds a body part.
	 * @param part
	 */
	public void addPart(BodyPart part) {
		bodyParts.add(part);
	}

	/**
	 * Removes the first encountered body part on this Body.
	 * @param typeOfPart
	 * @return true or false
	 */
	public boolean removePart(BodyPart typeOfPart) {
		return bodyParts.remove(typeOfPart);
	}
}
