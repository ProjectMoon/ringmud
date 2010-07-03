package ring.movement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.persistence.RingConstants;


@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder = {
	"title",
	"description",
	"depth",
	"width",
	"length"
	
})
/**
 * Class that represents a Room's model: its basic description, and internal game data.
 * This class serialise Mobile and Entity IDs to XML for persistent storage. Current
 * mobiles present in a room are not stored here; only the unchanging game data is.
 * @author projectmoon
 *
 */
public class RoomModel {	
	private String title;
	private String description;
	private int length;
	private int width;
	private int depth;
	
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@XmlElement
	public int getDepth() {
		return depth;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@XmlElement
	public int getWidth() {
		return width;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@XmlElement
	public int getLength() {
		return length;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + depth;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + length;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoomModel other = (RoomModel) obj;
		if (depth != other.depth)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (length != other.length)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
