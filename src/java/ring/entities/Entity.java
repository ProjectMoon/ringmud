package ring.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.persistence.RingConstants;
import ring.world.WorldObject;
import ring.world.WorldObjectMetadata;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"descriptor"
})
/**
 * Entities are invisible objects that sit in Rooms that provide services or interaction
 * to the Room. For example, an respawn entity might respawn mobiles after a period of time
 * after the Mobile dies. Entities can also provide things such as ambient messages (ex: for
 * weather).
 * @author projectmoon
 *
 */
public class Entity extends WorldObject {
	private String name;
	private String descriptor;
	
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	@XmlElement
	public String getDescriptor() {
		return descriptor;
	}

	@Override
	public WorldObjectMetadata getMetadata() {
		WorldObjectMetadata metadata = new WorldObjectMetadata();
		metadata.setName(getName());
		return metadata;
	}
	
	@Override
	public List<WorldObject> produceSearchList(Class<?>... dataTypes) {
		return new ArrayList<WorldObject>(0);
	}
	
	@Override
	public List<WorldObject> produceSearchList(List<Class<?>> dataTypes) {
		return new ArrayList<WorldObject>(0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((descriptor == null) ? 0 : descriptor.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (descriptor == null) {
			if (other.descriptor != null)
				return false;
		} else if (!descriptor.equals(other.descriptor))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
