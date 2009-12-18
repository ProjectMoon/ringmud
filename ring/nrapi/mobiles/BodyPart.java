package ring.nrapi.mobiles;

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
	private String description;

	public BodyPart() {}
	
	public BodyPart(String description) {
		this.name = description;
		this.description = description;
	}

	// Copy constructor
	public BodyPart(BodyPart other) {
		this.name = other.name;
		this.description = other.description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
