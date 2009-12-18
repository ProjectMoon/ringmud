package ring.nrapi.mobiles;

//Static import for all of the BodyPart constants.
import static ring.nrapi.mobiles.Body.*;

/**
 * The BodyFactory class creates stock bodies for a host of creatures. There
 * are bodies available for all the default races, as well as generic/specific
 * NPC body creation methods.
 * @author projectmoon
 *
 */
public class BodyFactory {

	// createMediumHumanoidBody method.
	// This method will create a body used by basically all of the PC races in
	// the game except
	// for ogres, gnomes, and illithids. It also creates a lot of the NPC
	// races/mobile races.
	public static Body createMediumHumanoidBody() {
		Body body = new Body();

		body.addPart(MEDIUM_BODY);
		body.addPart(LEFT_ARM);
		body.addPart(RIGHT_ARM);
		body.addPart(LEFT_FINGER);
		body.addPart(RIGHT_FINGER);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_HAND);
		body.addPart(RIGHT_HAND);
		body.addPart(LEFT_WRIST);
		body.addPart(RIGHT_WRIST);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createSmallHumanoidBody method.
	// This makes a small humanoid. It is used for Gnomes for the most part.
	public static Body createSmallHumanoidBody() {
		Body body = new Body();

		body.addPart(SMALL_BODY);
		body.addPart(LEFT_ARM);
		body.addPart(RIGHT_ARM);
		body.addPart(LEFT_FINGER);
		body.addPart(RIGHT_FINGER);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_HAND);
		body.addPart(RIGHT_HAND);
		body.addPart(LEFT_WRIST);
		body.addPart(RIGHT_WRIST);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createIllithidBody method.
	// This method creates the body of an illithid. The illithid only has one
	// difference--
	// 4 tentacles. These tentacles give it the ability to brain drain and also
	// wear tentacle
	// weapons.
	public static Body createIllithidBody() {
		Body body = new Body();

		body.addPart(MEDIUM_BODY);
		body.addPart(LEFT_ARM);
		body.addPart(RIGHT_ARM);
		body.addPart(LEFT_FINGER);
		body.addPart(RIGHT_FINGER);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_HAND);
		body.addPart(RIGHT_HAND);
		body.addPart(LEFT_WRIST);
		body.addPart(RIGHT_WRIST);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);
		body.addPart(TENTACLES);
		body.addPart(TENTACLES);
		body.addPart(TENTACLES);
		body.addPart(TENTACLES);

		return body;
	}

	// createLargeHumanoid method.
	// This method creates a large humanoid body. This is used for ogres,
	// giants, and the like.
	public static Body createLargeHumanoidBody() {
		Body body = new Body();

		body.addPart(LARGE_BODY);
		body.addPart(LEFT_ARM);
		body.addPart(RIGHT_ARM);
		body.addPart(LEFT_FINGER);
		body.addPart(RIGHT_FINGER);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_HAND);
		body.addPart(RIGHT_HAND);
		body.addPart(LEFT_WRIST);
		body.addPart(RIGHT_WRIST);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createSmallQuadrapedBody method.
	// This method creates a small quadraped such as a rabbit or fox. This is
	// used for most
	// animal monsters in the game. However, there are some bodies that can
	// create bigger
	// versions of quadrapeds.
	public static Body createSmallQuadrapedBody() {
		Body body = new Body();

		body.addPart(SMALL_BODY);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(WAIST);

		return body;
	}

	// createMediumQuadrapedBody method.
	// This method creates a medium-sized quadraped body.
	public static Body createMediumQuadrapedBody() {
		Body body = new Body();

		body.addPart(MEDIUM_BODY);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createLargeQuadrapedBody method.
	// This method creates a large quadraped body. Useful for mobiles like
	// young,
	// wingless... dragons?
	public static Body createLargeQuadrapedBody() {
		Body body = new Body();

		body.addPart(LARGE_BODY);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createHugeQuadrapedBody method.
	// Creates a huge quadraped body. Useful for something like the Tarrasque.
	public static Body createHugeQuadrapedBody() {
		Body body = new Body();

		body.addPart(HUGE_BODY);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);

		return body;
	}

	// createDragonBody method.
	// This method creates a dragon body. Essentially the above method, but with
	// wings.
	public static Body createDragonBody() {
		Body body = new Body();

		body.addPart(SMALL_BODY);
		body.addPart(HEAD);
		body.addPart(FACE);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(LEFT_LEG);
		body.addPart(RIGHT_LEG);
		body.addPart(LEFT_FOOT);
		body.addPart(RIGHT_FOOT);
		body.addPart(SHOULDER);
		body.addPart(SHOULDER);
		body.addPart(WAIST);
		body.addPart(WINGS);

		return body;
	}

}
