package ring.nrapi.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"name",
	"amount"
}
)
public class TestResource {
	private String name;
	private int amount;
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@XmlElement
	public int getAmount() {
		return amount;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getName() {
		return name;
	}
}
