package ring.nrapi.business;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.data.RingConstants;
import ring.nrapi.xml.JAXBAnnotationReader;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"ID"
})
public abstract class AbstractBusinessObject implements BusinessObject {
	private String id;
	private boolean storeAsUpdate;
	private boolean referential;
	
	public abstract void save();

	@XmlAttribute(name = "ref")
	public boolean isReferential() {
		return referential;
	}
	
	public void setReferential(boolean val) {
		referential = val;
	}
	
	/**
	 * Returns the objects unique's ID.
	 * @return the id
	 */
	@XmlAttribute(name = "id", required = true)
	public String getID() {
		return id;
	}
	
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
		if (isReferential()) {
			JAXBAnnotationReader reader = new JAXBAnnotationReader(this.getClass());
			String element = reader.rootElementName();
			String xml = "<" + element + " reference=\"true\">";
			xml += "<id>" + getID() + "</id>";
			xml += "</" + element + ">";
			
			return xml;
		}
		else {
			return marshalledXML();
		}
	}
	
	private String marshalledXML() {
		try {
			JAXBContext ctx = JAXBContext.newInstance(this.getClass());
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
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
