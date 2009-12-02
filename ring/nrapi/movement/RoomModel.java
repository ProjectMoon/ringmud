package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Room's model: its basic description, and internal game data.
 * @author projectmoon
 *
 */
public class RoomModel {
	private ArrayList<String> entityIDs;
	private String title;
	private String description;
	private int length;
	private int width;
	private int depth;
	
	public List<String> getEntityIDs() {
		return entityIDs;
	}
	
	public void addEntityID(String id) {
		entityIDs.add(id);
	}
	
	public boolean removeEntityID(String id) {
		return entityIDs.remove(id);
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	
}
