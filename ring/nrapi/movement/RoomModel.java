package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;

import ring.nrapi.data.RingConstants;


@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder = {
	"title",
	"description",
	"mobileIDCollection",
	"entityIDCollection",
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
	//See bottom of class for EntityIDCollection object definition.
	private EntityIDCollection entityIDs = new EntityIDCollection();
	
	//See bottom of class for MobileIDCollection object definition.
	private MobileIDCollection mobileIDs = new MobileIDCollection();
	
	private String title;
	private String description;
	private int length;
	private int width;
	private int depth;
	
	@XmlElement(name = "entityIDs")
	public EntityIDCollection getEntityIDCollection() {
		return entityIDs;
	}
	
	public void setEntityIDCollection(EntityIDCollection entityIDs) {
		this.entityIDs = entityIDs;	
	}
	
	@XmlTransient
	/**
	 * Convenience method for getting the list of entity IDs.
	 * Same as calling getIDs from the entity ID collection object.
	 * @return the list of entity IDs.
	 */
	public List<String> getEntityIDs() {
		return getEntityIDCollection().getIDs();
	}	
	
	@XmlElement(name = "mobileIDs")
	public MobileIDCollection getMobileIDCollection() {
		return mobileIDs;
	}
	
	public void setMobileIDCollection(MobileIDCollection mobileIDs) {
		this.mobileIDs = mobileIDs;
	}
	
	@XmlTransient
	/**
	 * Convenience method for getting the list of mobile IDs.
	 * Same as calling getIDs from the mobile ID collection object.
	 * @return the list of mobile IDs.
	 */	
	public List<String> getMobileIDs() {
		return getEntityIDCollection().getIDs();
	}
	
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
	
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlType
	public static class EntityIDCollection {
		private List<String> entityIDs = new ArrayList<String>();
		
		@XmlElement(name = "id")
		public List<String> getIDs() {
			return entityIDs;
		}
		
		public void setIDs(List<String> entityIDs) {
			this.entityIDs = entityIDs;
		}

		public void addEntityID(String id) {
			entityIDs.add(id);
		}
	
		public boolean removeEntityID(String id) {
			return entityIDs.remove(id);
		}		
	}
	
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlType
	public static class MobileIDCollection {
		private List<String> mobileIDs = new ArrayList<String>();
		
		@XmlElement(name = "id")
		public List<String> getIDs() {
			return mobileIDs;
		}
		
		public void setIDs(List<String> mobileIDs) {
			this.mobileIDs = mobileIDs;
		}

		public void addMobileID(String id) {
			mobileIDs.add(id);
		}
	
		public boolean removeMobileID(String id) {
			return mobileIDs.remove(id);
		}		
	}	
}
