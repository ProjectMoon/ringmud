package ring.items;

import ring.effects.Effect;
import ring.mobiles.BodyPart;
import ring.world.WorldObject;
import ring.world.WorldObjectMetadata;

import java.util.ArrayList;
import java.util.List;

public class Item extends WorldObject {
	private BodyPart partWornOn;
	private String name;
	private String description;
	private String idlePrefix;
	private String idleSuffix;

	public BodyPart getPartWornOn() {
		return partWornOn;
	}
	
	public void setPartWornOn(BodyPart part) {
		partWornOn = part;
	}

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public boolean isCursed() {
		// TODO Auto-generated method stub
		return false;
	}

	public Effect getPassiveEffects() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getDescription() {
		return description;
	}

	public void setIdlePrefix(String idlePrefix) {
		this.idlePrefix = idlePrefix;
	}

	
	public String getIdlePrefix() {
		return idlePrefix;
	}

	public void setIdleSuffix(String idleSuffix) {
		this.idleSuffix = idleSuffix;
	}

	
	public String getIdleSuffix() {
		return idleSuffix;
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
	
	public String toString() {
		return getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((idlePrefix == null) ? 0 : idlePrefix.hashCode());
		result = prime * result
				+ ((idleSuffix == null) ? 0 : idleSuffix.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((partWornOn == null) ? 0 : partWornOn.hashCode());
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
		Item other = (Item) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idlePrefix == null) {
			if (other.idlePrefix != null)
				return false;
		} else if (!idlePrefix.equals(other.idlePrefix))
			return false;
		if (idleSuffix == null) {
			if (other.idleSuffix != null)
				return false;
		} else if (!idleSuffix.equals(other.idleSuffix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partWornOn == null) {
			if (other.partWornOn != null)
				return false;
		} else if (!partWornOn.equals(other.partWornOn))
			return false;
		return true;
	}
}
