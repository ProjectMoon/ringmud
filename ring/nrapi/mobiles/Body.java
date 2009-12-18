package ring.nrapi.mobiles;

import java.util.*;
import java.io.Serializable;
import ring.nrapi.entities.*;

/**
 * This class defines a body and equipment slots. Numerous different body parts
 * can be created and all can be added. This gives the MUD a very flexible body
 * system that supports all kinds of creatures, dismemberment, polymorph, mutations,
 * etc.
 * @author projectmoon
 *
 */
public class Body implements Serializable {
	public static final long serialVersionUID = 1;

	// List of Body Parts.
	// When any BodyPart is added to the body, it creates a cloned version of
	// the body part
	// to make sure the static instances do not change. The BodyPart method
	// implements a
	// equals() method to check if the BodyPart is equal to another on the
	// actual Body. This
	// allows for multiple body parts of the same type. Example: 8 fingers on a
	// human. When
	// using a method to return a BodyPart, the first one that is empty is
	// returned if there are
	// multiple copies of the BodyPart. Otherwise, the first occurance is
	// returned.
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

	// Other variables.
	private List<BodyPart> bodyParts;

	// Creates a new body object.
	public Body() {
		bodyParts = new ArrayList<BodyPart>(20);
	}

	// addPart method.
	// This adds a body part to the body. It is possible to add multiple body
	// parts to
	// the body. BE AWARE that if multiple copies of the same body part are
	// added,
	// this affects the way certain methods work.
	public void addPart(BodyPart part) {
		bodyParts.add(part);
	}

	// removePart method.
	// This will remove the FIRST EMPTY body part of the specified index. Any
	// body
	// part of the specified index that has an item is spared removal. Call
	// removePart(int index, boolean removeFirst) with a TRUE parameter if you
	// want to
	// remove the first occurance of the body part.
	public void removePart(BodyPart typeOfPart) {
		boolean found = false;
		int c = 0;
		BodyPart part;

		while (!found) {
			part = (BodyPart) bodyParts.get(c);
			if (part.equals(typeOfPart)) {
				bodyParts.remove(c);
				found = true;
			}
			c++;
		}
	}

	// removePart method.
	// This part will remove the first occurance of the BodyPart of the
	// specified
	// index it finds.
	public void removePart(BodyPart typeOfPart, boolean removeFirst) {
		if (removeFirst == false) {
			removePart(typeOfPart);
		}

		else {
			boolean found = false;
			int c = 0;
			BodyPart part;

			while (!found) {
				part = (BodyPart) bodyParts.get(c);
				if (part.equals(typeOfPart)) {
					bodyParts.remove(c);
					found = true;
				}
				c++;
			}
		}// End else statement.
	}

	// getPart method.
	// This version of the above method will return the first occurance of the
	// body part,
	// regardless of whether it has an item in it or not.
	public BodyPart getFirstPart(BodyPart typeOfPart) {
		BodyPart part;
		boolean found = false;
		int c = 0;

		while (!found) {
			part = (BodyPart) bodyParts.get(c);
			if (typeOfPart.equals(part)) {
				found = true;
				return part;
			}
			c++;
		}

		return null;// Didn't find anything!
	}

	// getEligibleBodyParts method.
	// This method returns an array of BodyParts that the specified item can be
	// used on.
	public BodyPart[] getEligibleBodyParts(Item item) {
		Vector<BodyPart> returnList = new Vector<BodyPart>();
		BodyPart part = item.getPartWornOn();

		// loop through all of the BodyParts on the body.
		for (int c = 0; c < bodyParts.size(); c++) {
			BodyPart otherPart = (BodyPart) bodyParts.get(c);
			if (part.equals(otherPart))
				returnList.addElement(otherPart);
		}

		// Was anything found?
		if (returnList.size() == 0)
			return null;

		// Yep!
		// Change the vector to an array.
		BodyPart[] returnParts = new BodyPart[returnList.size()];
		for (int c = 0; c < returnList.size(); c++)
			returnParts[c] = (BodyPart) returnList.get(c);

		return returnParts;
	}

	public BodyPart[] getBodyParts() {
		return bodyParts.toArray(new BodyPart[0]);
	}
	
	public void setBodyParts(BodyPart[] parts) {
		bodyParts = new ArrayList<BodyPart>(parts.length);
		
		for (BodyPart part : parts) {
			bodyParts.add(part);
		}
	}
	
	
}
