package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;

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
}
