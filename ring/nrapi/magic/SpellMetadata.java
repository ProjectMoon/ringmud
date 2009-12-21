package ring.nrapi.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.RingConstants;
import ring.nrapi.xml.XMLParameterException;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"parameters",
})
/**
 * This is a very generic class that stores metadata about spells.
 * Metadata is stored as Strings, but the parameters can be converted
 * to any of the primitive types.
 * 
 * MagicSystem implementations typically have their own implementation
 * of spell metadata that they convert this object into in order to have
 * some semblance of type safety.
 * @author projectmoon
 *
 */
public class SpellMetadata {
	
	@XmlType(propOrder = { "value", "key" })
	public static class MetadataTuple {
		private String param;
		@XmlAttribute public String getKey() { return param; }
		public void setKey(String param) { this.param = param; } 
		
		private String value;
		@XmlAttribute public String getValue() { return value; }
		public void setValue(String value) { this.value = value; }
	}
	
	private Map<String, String> parameters = new HashMap<String, String>();
	private String implementationClass;
	
	public void setParameters(List<MetadataTuple> metadata) {
		for (MetadataTuple tuple : metadata) {
			parameters.put(tuple.getKey(), tuple.getValue());
		}
	}
	
	@XmlElement(name = "entry")
	public List<MetadataTuple> getParameters() {
		ArrayList<MetadataTuple> list = new ArrayList<MetadataTuple>(parameters.size());
		for (String param : parameters.keySet()) {
			MetadataTuple tuple = new MetadataTuple();
			tuple.setKey(param);
			tuple.setValue(parameters.get(param));
			list.add(tuple);
		}
		
		return list;
	}
	
	public String getImplementation() {
		return implementationClass;
	}
	
	public void setImplementation(String implementation) {
		implementationClass = implementation;
	}
	
	public String getAsString(String param) {
		return parameters.get(param);
	}
	
	public int getAsInt(String param) throws XMLParameterException {
		try {
			return Integer.parseInt(parameters.get(param));
		}
		catch (Exception e) {
			throw new XMLParameterException("Couldn't convert to int.", e);
		}
	}
	
	public boolean getAsBoolean(String param) throws XMLParameterException {
		try {
			return Boolean.parseBoolean(parameters.get(param));
		}
		catch (Exception e) {
			throw new XMLParameterException("Couldn't convert to boolean.", e);
		}
	}
	
	public double getAsDouble(String param) throws XMLParameterException {
		try {
			return Double.parseDouble(parameters.get(param));
		}
		catch (Exception e) {
			throw new XMLParameterException("Couldn't convert to double.", e);
		}
	}
	
	public void addParameter(String param, String value) {
		parameters.put(param, value);
	}
	
	public Object removeParameter(String param) {
		return parameters.remove(param);
	}	
}
