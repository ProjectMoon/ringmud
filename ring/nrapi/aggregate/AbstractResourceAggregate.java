package ring.nrapi.aggregate;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ring.nrapi.data.RingConstants;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(
	namespace = RingConstants.RING_NAMESPACE,
	propOrder = {
		"ID"
	}
)
public abstract class AbstractResourceAggregate implements ResourceAggregate {
	private boolean storeAsUpdate;
	private String id;
	
	@Override
	@XmlElement(name = "id")
	public String getID() {
		return id; 
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public void setStoreAsUpdate(boolean val) {
		storeAsUpdate = val;
	}

	@Override
	public boolean storeAsUpdate() {
		return storeAsUpdate;
	}
	
	@Override
	public String toXML() {
		try {
			JAXBContext ctx = JAXBContext.newInstance(this.getClass());
			Marshaller m = ctx.createMarshaller();
			//m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter writer = new StringWriter();
			m.marshal(this, writer);
			String xml = writer.toString();
			
			//TODO find a MUCH better way to do this... so hackish.
			return xml.substring(xml.indexOf("?>") + 2);
		}
		catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
