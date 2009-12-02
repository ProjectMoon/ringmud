package ring.nrapi.aggregate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;
import ring.nrapi.resources.TestResource;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(
	namespace = RingConstants.RING_NAMESPACE,
	propOrder = {
		"testResource"
	}
)
public class TestAggregate extends AbstractResourceAggregate implements ResourceAggregate {
	private TestResource testResource;
	
	@XmlElement
	public TestResource getTestResource() {
		return testResource;
	}
	
	public void setTestResource(TestResource res) {
		testResource = res;
	}
}
