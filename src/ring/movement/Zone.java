package ring.movement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.persistence.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder = {
	"name",
	"description",
	"minLevel",
	"maxLevel"
})
/**
 * Class that represents information about an in-game zone.
 * @author projectmoon
 */
public class Zone extends AbstractBusinessObject {
	private String name;
	private String description;
	private int minLevel;
	private int maxLevel;
	
	@Override
	public void save() {
		throw new UnsupportedOperationException();
	}
	
	@XmlElement
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement
	public int getMinLevel() {
		return minLevel;
	}
	
	public void setMinLevel(int level) {
		minLevel = level;
	}
	
	@XmlElement
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int level) {
		maxLevel = level;
	}
	
	
}
