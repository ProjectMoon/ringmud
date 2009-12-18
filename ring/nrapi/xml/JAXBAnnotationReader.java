package ring.nrapi.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Given a Class object, it reports useful information about its
 * JAXB annotations.
 * @author projectmoon
 *
 */
@XmlRootElement
public class JAXBAnnotationReader {
	private Class<?> clazz;
	
	public JAXBAnnotationReader(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public XmlRootElement rootElement() {
		if (clazz.isAnnotationPresent(XmlRootElement.class)) {
			return clazz.getAnnotation(XmlRootElement.class);
		}
		else {
			return null;
		}
	}
	
	public String rootElementName() {
		XmlRootElement root = rootElement();
		String res = root.name();
		
		//Override ##default with className
		if (res.equals("##default")) {
			res = getDefaultRootName();
			String firstChar = res.substring(0, 1).toLowerCase();
			res = firstChar + res.substring(1);
		}
		
		return res;
	}
	
	private String getDefaultRootName() {
		String res = clazz.getSimpleName();
		int c = 1;
		
		//Advance to the first uppercase character after the first letter.
		while (c < res.length() && Character.isUpperCase(res.charAt(c)) == false)
			c++;
		
		//If only the first letter is capital, lowercase the whole thing
		//otherwise, lowercase the first word.
		if (c == res.length()) {
			return res.toLowerCase();
		}
		else {
			return res.substring(0, c) + res.substring(c);
		}
	}
}
