package ring.movement;

import ring.nrapi.business.BusinessObject;

/**
 * Class that represents information about an in-game zone.
 * @author projectmoon
 */
public class Zone extends BusinessObject {
	private String name;
	private String description;
	private int minLevel;
	private int maxLevel;
	
	@Override
	public void save() {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinLevel() {
		return minLevel;
	}
	
	public void setMinLevel(int level) {
		minLevel = level;
	}

	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int level) {
		maxLevel = level;
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
		result = prime * result + maxLevel;
		result = prime * result + minLevel;
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
		Zone other = (Zone) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (maxLevel != other.maxLevel)
			return false;
		if (minLevel != other.minLevel)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
