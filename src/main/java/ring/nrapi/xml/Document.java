package ring.nrapi.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.exist.dom.XMLUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.mobiles.Mobile;
import ring.mobiles.npc.NPC;
import ring.movement.Room;
import ring.nrapi.business.BusinessObject;
import ring.persistence.ParentRelationshipCreator;

/**
 * Object-oriented mapping of an XML document loaded into memory that stores an
 * AbstractBusinessObject hierarchy. The document class stores metadata about
 * the AbstractBusinessObject hierarchy, as well as the events attached to each
 * object. The class is primarily intended to be used from python code.
 * @author projectmoon
 *
 */
public class Document {
	public enum Source { DOCUMENT, FRAGMENT };
	
	private Source source;
	private String name;
	private BusinessObject root;
	public static void main(String[] args) throws XPathExpressionException, JAXBException {
		Mobile npc = new NPC();
		npc.getBaseModel().setName("mob");
		
		Room r = new Room();
		r.getModel().setTitle("a room");
		r.addMobile(npc);
		npc.setLocation(r);
			
		StringReader reader = new StringReader(r.toXML());
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "//mobile";
		InputSource inputSource = new InputSource(reader);
		NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);

		for (int c = 0; c < nodes.getLength(); c++) {
			Element e = (Element)nodes.item(c);
			//System.out.println(e.getTagName());
			System.out.println(nodeToString(e));
			//System.out.println(bo.toXML().equals(npc.toXML()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BusinessObject> T convertToObject(Element el, Class<T> cl) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(cl);
		Unmarshaller um = ctx.createUnmarshaller();
		um.setListener(new ParentRelationshipCreator());
		T conv = (T)um.unmarshal(el);
		
		conv.setStoreAsUpdate(true);
		//conv.setDocumentID(res.getDocumentId());
		conv.createChildRelationships();
		
		return conv;
	}
	
	public List<BusinessObject> search(String query) {
		String[] split = query.split("/");
		
		return null;
	}
	
	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
		System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
		}
}

/*
The following describes how the event-attaching system will be created. It uses the XML database as an efficient index for searching, and stores all
ABOs in a WeakHashMap to create a list of search results. Then, a specialized Python class will provide jQuery-like capabilities for operating on the
set of all returned ABOs. After that, it is simply a matter of adding new event-handling methods to ABOs with the standard Java pattern. Jython will
take care of converting them to event properties. Everybody wins!

The database can be used to store a document that describes all of the currently-existing ABOs, their hierarchies, and the documents they come from.
By attaching a UUID field to every ABO, we can pretty much forward the xquery search directly to the database and then find objects by UUID stored
in a WeakHashMap. So, we have:

1. WeakHashMap<UUID, AbstractBusinessObject>: This will need to store roots as well as children. It may need to store ABOs as WeakReferences.
	* The WeakReferences of the ABOs register themselves with the ReferenceQueue below.
2. A ReferenceQueue + thread that is responsible for removing obsolete ABO entries from the database index document.
	* This thread must synchronize on the hashmap for the whole delete process.
3. Modification of existing code that adds a UUID field to AbstractBusinessObject, mapped as @XmlAttribute:
	* Canonical ID will still be needed for filtering purposes.
3. Modification to existing code that will update the database index document when new ABOs are created. Can probably do through aspect:
	* When a new ABO of any type is created, it is assigned a UUID.
	* The UUID->ABO relation is added to the map specified in #1. Will probably need to wrap the ABO in a WeakReference.
	* After ROOT ABO creation is done, its entire document is appended as a child of the root node in the DB index document.
	* The document must also be tested against all LiveEvent objects.
4. A class that exposes a search method for the database index document:
	* The search method takes an individual XPath expression.
	* The method itself limits the search to the game collection and index document.
	* The search method then takes the list of Nodes returned and compiles a list of UUIDs from the Nodes' uuid attributes.
	* The search method queries the WeakHashMap and returns a list of ABOs whose UUIDs match those in the list.
5. The search class also exposes "live" XPath mappings for event attachment:
	* This allows event handlers to be defined ahead of time, and added as objects matching the XPath expression are created.
	* The XPath expression and its associated event handlers are wrapped up in a LiveEvent object and added to a list.
	* Whenever a new ABO document is about to be added to the database index document, the fragmnet is first tested against all LiveEvent objects
	  using the same algorithm as defined in the search method.
5. The List class returned that contains the ABOs will actually likely be a Python class with specialized code for adding events to all objects in the list at once.
	* First, we should concentrate on the first 4 steps of course...
	* When a new ABO is added to the result list class, Python will rip out its event properties (type test vs CompoundCallable?) and add them to the list class.
	* The ABO is then added to an event property->ABO  map.
	* The event properties are added to the list class as methods that query the above map and then appends the event handler to the objects.

Can this system handle non-reflexive events?
No, not really. This system isn't really for adding events, it's for finding game objects. So in theory it could be expanded beyond event adding.
However, since the system is only for finding objects, it's not aware of any way to attach another object's event handler to a different object.
Another system, likely using Observer pattern, will need to be created for that.

Work plan is as follows:
X. Add code to createRingDatabase to create the DiD in the game collection.
X. Add UUID field to ABO and re-evaluate purpose/use of documentID, documentName, ID, and canonicalID.
	* documentID = root document name. DELETED; superceded by Persistable.root.documentName
	* documentName = name of document Persistable comes from
	* ID = user-defined ID in xml file.
	* canonicalID = documentName + : + ID
	* Removed BusinessObject interface; renamed ABO to BusinessObject
X. Add isRoot() property to ABO if its' not there already.
X. equals() and hashCode() methods for all BOs (ugh)
X. Create IndexCleanupDaemon that holds the reference queue for removing entries from the DiD.
X. Create ObjectIndex class that contains the WeakHashMap, and methods for adding and removing ABOs from it.
	* The addABO method will add the object to the WeakHashMap, and if isRoot() == true, add it to the DiD as well.
	* removeABO will of course do the reverse.
X. Create ObjectSearch class that contains the searching and live search methods.
	* This might need to be a python class so we can use functors (though we could use runnables or something...)
	* Live events could instead be generalized to a live search where a callback is executed when a new ABO matches the live criteria.
5. Create ResultList Python class that contains the specialized code for jQuery-like operations.
6. Maybe also create a Python class that hooks into EventManager to make it easier to use.
7. Entirely delete old event system (the entire ring.events package)
8. Move ring.events.listeners to ring.events.
9. The new classes will be part of ring.events.

Tests to run:
1. Figure out proper delete query
2. Try a mockup of the hashmap and reference queue with wait/notify:
	* Main thread for launching code and mutlithreaded access to hashmap
	* ReferenceQueue thread
*/
