package ring.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class DocumentValidator {
	private static final Schema SCHEMA = loadSchema();
	
	private static String lastError = "";
	
	private static Schema loadSchema() {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		factory.setResourceResolver(new ResourceResolver());
		
		InputStream stream1 = DocumentValidator.class.getClassLoader().getResourceAsStream("ring/nrapi/xml/schema1.xsd");
		InputStream stream2 = DocumentValidator.class.getClassLoader().getResourceAsStream("ring/nrapi/xml/schema2.xsd");
		StreamSource input1 = new StreamSource(stream1);
		StreamSource input2 = new StreamSource(stream2);
		
		try {
			return factory.newSchema(new Source[] { input1, input2 });
		}
		catch (SAXException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean validate(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		StreamSource src = new StreamSource(input);
		Validator validator = SCHEMA.newValidator();
		
		try {
			validator.validate(src);
			return true;
		}
		catch (SAXException e) {
			lastError = e.getMessage();
			return false;
		}
	}
	
	public static String getLastError() {
		return lastError;
	}
}
