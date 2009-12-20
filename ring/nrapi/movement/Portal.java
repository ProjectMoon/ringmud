package ring.nrapi.movement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;
import ring.util.TextParser;

/**
 * This class represents an entrance to some Destination. A Portal in this
 * case is a generic direction to go towards or object to enter in order to
 * change the current location of the Mobile. A Portal could be a direction
 * like north or south, it could be an entrance like a door, or it could be
 * truly a portal, that leads to grand places like another plane of existence!
 * 
 * @author jeff
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder = {
	"destination",
	"displayName",
	"searchDC"
})
public class Portal {
	//The place to go!
	private Room destination;
	
	//i.e. "north", "heavy wooden door"
	private String displayName;
	
	//the name that mobiles must use to go into this
	//portal. i.e. "door" or "north"
	//It is the same as display name but without formatting.
	//Not settable by user.
	private String interactiveName;
	
	//Search DC required to find this Portal.
	private int searchDC;
	
	/**
	 * No-args constructor that sets nothing. Used by JAXB.
	 */
	public Portal() {}
	
	/**
	 * Explicitly set all parameters of this Portal.
	 * @param dest
	 * @param displayName
	 * @param interactiveName
	 */
	public Portal(Room dest, String displayName, String interactiveName) {
		destination = dest;
		this.displayName = displayName;
		this.interactiveName = interactiveName;
	}
	
	/**
	 * Set desination and display name, but allow the MUD to automatically
	 * determine the interactive name. It attempts to strip all ANSI format
	 * tags from the display name and use that as the interactive name.
	 * @param dest
	 * @param displayName
	 */
	public Portal(Room dest, String displayName) {
		destination = dest;
		this.displayName = displayName;
		this.interactiveName = TextParser.stripFormatting(displayName);
	}

	@XmlTransient
	public boolean isHidden() {
		return (searchDC > 0);
	}
	
	@XmlAttribute
	public int getSearchDC() {
		return searchDC;
	}	
	
	public void setSearchDC(int dc) {
		searchDC = dc;
	}
	
	@XmlAttribute(name = "direction")
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String name) {
		displayName = name;
		setInteractiveName(TextParser.stripFormatting(name));
	}
	
	@XmlTransient
	public String getInteractiveName() {
		return interactiveName;
	}
	
	public void setInteractiveName(String name) {
		interactiveName = name;
	}
	
	@XmlAttribute
	public Room getDestination() {
		return destination;
	}
	
	public void setDestination(Room loc) {
		destination = loc;
	}
	
	public String toString() {
		return "Portal[displayname=" + displayName + ", interactivename=" + interactiveName
			+ ", Destination=" + destination;
	}
	
	/**
	 * Returns true if the destinations of the two Portal objects point to the same Location object.
	 * False otherwise.
	 */
	public boolean equals(Object other) {
		if (other == null || (!(other instanceof Portal))) {
			return false;
		}
		else {
			Portal p = (Portal)other;
			return (this.destination == p.destination);
		}
	}
}
