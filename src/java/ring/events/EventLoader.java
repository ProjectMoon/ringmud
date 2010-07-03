package ring.events;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.xmldb.api.base.XMLDBException;

import ring.deployer.DeployedMUDFactory;
import ring.nrapi.business.BusinessObject;
import ring.persistence.ExistDB;
import ring.persistence.RingConstants;
import ring.persistence.XQuery;
import ring.system.MUDConfig;

/**
 * Loads events at MUD startup.
 * @author projectmoon
 *
 */
public class EventLoader {
	/**
	 * Quick business object class that wraps around the XML fragments
	 * returned by the eventloader.xq file. We need to do this so we can
	 * make use of eXist's <code>util:document-name</code> function in order
	 * to get the document names and the codebehind attribute. Otherwise, we
	 * would have to operate off eXist's IDs (which can be wrong for our purposes),
	 * or return more XML than we need.
	 * @author projectmoon
	 *
	 */
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlRootElement(name = "doc")
	@XmlType(
	namespace = RingConstants.RING_NAMESPACE,
	propOrder= {
		"documentName",
		"codebehind"
	})
	public static class CodebehindEntry extends BusinessObject {
		private String docName;
		private String codebehind;
		
		public void setDocumentName(String docName) {
			this.docName = docName;
		}
		
		@XmlAttribute(name = "name")
		public String getDocumentName() {
			return docName;
		}
		public void setCodebehind(String codebehind) {
			this.codebehind = codebehind;
		}
		
		@XmlElement
		public String getCodebehind() {
			return codebehind;
		}
	}
	
	/**
	 * Load and bind events.
	 * @throws IOException
	 * @throws XMLDBException
	 * @throws JAXBException
	 */
	public void loadEvents() throws IOException, XMLDBException, JAXBException {
		EventDispatcher.initialize();
		InputStream xqStream = this.getClass().getClassLoader().getResourceAsStream("ring/events/eventloader.xq");
		XQuery xq = new XQuery(xqStream);
			
		List<CodebehindEntry> results = xq.execute(CodebehindEntry.class);
		
		for (CodebehindEntry entry : results) {
			load(entry);
		}
	}
	
	private void load(CodebehindEntry entry) {
		try {
			String pythonFile = DeployedMUDFactory.currentMUD().getLocation() + System.getProperty("file.separator") + entry.getCodebehind();
			InputStream pyStream = new FileInputStream(pythonFile);
			EventDispatcher.initializeEvents(entry.getDocumentName(), pyStream);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
