package ring.deployer;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Finds IDs in XML documents.
 * @author projectmoon
 *
 */
public class IDFinder implements ContentHandler {
	private List<String> ids = new ArrayList<String>();
	
	public List<String> getIDs() {
		return ids;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		String id = atts.getValue("id");
		if (id != null) {
			ids.add(id);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void endDocument() throws SAXException {}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {}

	@Override
	public void setDocumentLocator(Locator locator) {}

	@Override
	public void skippedEntity(String name) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

}
