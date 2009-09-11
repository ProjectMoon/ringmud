package ring.jox.beans;

import java.io.Serializable;

/**
 * Defines a portal for the JOX parser. Basically a stripped-down Portal.
 * 
 * @author projectmoon
 * 
 */
public class PortalBean extends RingBean<PortalBean> implements Serializable {
	public static final long serialVersionUID = 1;

	private String direction;
	private String destinationID;
	private int searchDC;
	private String interactiveName;

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDestination(String destinationID) {
		this.destinationID = destinationID;
	}

	public String getDestination() {
		return destinationID;
	}

	public void setInteractiveName(String interactiveName) {
		this.interactiveName = interactiveName;
	}

	public String getInteractiveName() {
		return interactiveName;
	}

	public void setSearchDC(int searchDC) {
		this.searchDC = searchDC;
	}

	public int getSearchDC() {
		return searchDC;
	}

}
