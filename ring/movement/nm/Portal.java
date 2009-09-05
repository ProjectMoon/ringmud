package ring.movement.nm;

import ring.util.TextParser;

/**
 * This class represents an entrance to some Destination. A Portal in this
 * case is a generic direction to go towards or object to enter in order to
 * change the current location of the Mobile. A Portal could be a direction
 * like north or south, it could be an entrance like a door, or it could be
 * truly a portal, that leads to grand places like another plane of existence!
 * @author jeff
 *
 */
public class Portal {
	//The place to go!
	private Destination destination;
	
	//i.e. "north", "heavy wooden door"
	private String displayName;
	
	//the name that mobiles must use to go into this
	//portal. i.e. "door" or "north"
	//Generally the same as display name but without formatting.
	private String interactiveName;
	
	//Search DC required to find this Portal.
	private int searchDC;
	
	/**
	 * Explicitly set all parameters of this Portal.
	 * @param dest
	 * @param displayName
	 * @param interactiveName
	 */
	public Portal(Destination dest, String displayName, String interactiveName) {
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
	public Portal(Destination dest, String displayName) {
		destination = dest;
		this.displayName = displayName;
		this.interactiveName = TextParser.stripFormatting(displayName);
	}
	
	public void setSearchDC(int dc) {
		searchDC = dc;
	}
	
	public int getSearchDC() {
		return searchDC;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getInteractiveName() {
		return interactiveName;
	}
	
	public Destination getDestination() {
		return destination;
	}
	
	public String toString() {
		return "Portal[displayname=" + displayName + ", interactivename=" + interactiveName
			+ ", Destination=" + destination;
	}
}
