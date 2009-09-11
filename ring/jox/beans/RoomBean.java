package ring.jox.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A bean representing a Room to be constructed. Basically a stripped down version
 * of the Room class.
 * @author projectmoon
 *
 */
public class RoomBean extends RingBean<RoomBean> implements Serializable {
	public static final long serialVersionUID = 1;
	
	public RoomBean() {
		portals = new ArrayList<PortalBean>();
	}

	private String roomID;
	private String title;
	private String description;
	private int width;
	private int height;
	private int length;	
	private List<PortalBean> portals;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}
	
	public void setExit(PortalBean[] exits) {
		for (PortalBean port : exits) {
			portals.add(port);
		}
	}
	
	public PortalBean[] getExit() {
		return portals.toArray(new PortalBean[0]);
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}
	
	public void setID(String roomID) {
		this.roomID = roomID;
	}
	
	public String getID() {
		return roomID;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}

}
