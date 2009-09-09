package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import ring.world.WorldObject;

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
public class BodyPart extends WorldObject {
	public static final long serialVersionUID = 1;
	// This class is used for a body part.

	// Instance variables.
	// All the constant BodyParts are maintained in class Body.

	// Description of the body part.
	private String description;

	public BodyPart() {}
	
	public BodyPart(String description) {
		super.longDescription = description;
	}

	// Copy constructor
	public BodyPart(BodyPart other) {
		this.longDescription = other.description;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean isEntity() {
		return false;
	}

	@Override
	public boolean isNPC() {
		return false;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}
	
	@Override
	public boolean isItem() {
		return false;
	}

	public int getAC() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCurrentHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getEntityType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxHP() {
		// TODO Auto-generated method stub
		return 0;
	}

}
