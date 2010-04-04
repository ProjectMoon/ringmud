package ring.nrapi.business;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.persistence.DataStoreFactory;
import ring.persistence.Persistable;
import ring.persistence.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"ID",
	"documentName"
})
public abstract class AbstractBusinessObject implements BusinessObject {
	private Persistable parent;
	private List<Persistable> children = new ArrayList<Persistable>();
	
	private String docID;
	
	private String id;
	private String docName;
	
	private boolean storeAsUpdate;
	
	public void save() {
		DataStoreFactory.getDefaultStore().storePersistable(this);
	}

	/**
	 * Responsible for propagating information down the hierarchy
	 * of objects found in the document or fragment that this
	 * AbstractBusinessObject was loaded from. It propagates information
	 * and calls each child's <code>createChildRelationships()</code> method. This
	 * results in a recursive propagation of information down through the object
	 * hierarchy.
	 * <br/><br/>
	 * This method only concerns objects that are created at object load. It does
	 * not propagate information to children added later during server operation.
	 */
	public final void createChildRelationships() {
		for (Persistable child : children) {
			createChildRelationship(this, child);
		}
	}
	
	private void createChildRelationship(Persistable parent, Persistable child) {
		System.out.println("Creating relationship " + parent + " ==> " + child);
		child.setParent(parent);
		
		//If the document name is null, then it wasn't set anywhere else
		//so the child is probably from the same document...
		if (child.getDocumentName() == null) {
			child.setDocumentName(parent.getDocumentName());
		}
		
		for (Persistable grandchild : child.getChildren()) {
			createChildRelationship(child, grandchild);
		}
	}
	
	/**
	 * Adds a child to the list of children to propagate information
	 * to when <code>createChildRelationships()</code> is called. This
	 * list is only read when this abstract business object is first loaded.
	 * Afterwards, it is not considered relevant. In other words, any new
	 * child business objects added to this object (i.e. a mobile added to a
	 * room) do not have parent information propagated to them. It is 
	 * possible that this might change depending on how world state saving
	 * is implemented.
	 * @param child The child to add.
	 */
	@Override
	public void addChild(Persistable child) {
		children.add(child);
	}
	
	@Override
	public List<Persistable> getChildren() {
		return children;
	}
	
	@Override
	public Persistable getRoot() {
		if (getParent() == null) {
			return this;
		}
		else {
			Persistable root = getParent();
			while (root.getParent() != null) {
				root = root.getParent();
			}
			
			return root;
		}
	}
	
	@Override
	@XmlTransient
	public Persistable getParent() {
		return parent;
	}
	
	public void setDocumentID(String id) {
		docID = id;
	}
	
	@Override
	@XmlTransient
	public String getDocumentID() {
		return docID;
	}
	
	@Override
	public void setParent(Persistable obj) {
		parent = obj;
	}
	
	
	@XmlTransient
	public String getCanonicalID() {
		return getDocumentName() + ":" + getID();
	}
	
	@XmlAttribute(name = "docname")
	public String getDocumentName() {
		return docName;
	}
	
	public void setDocumentName(String docName) {
		this.docName = docName;
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
	
	/**
	 * Returns this object represented as a full XML document containing
	 * the standard processing instruction and a &lt;ring&gt; element as the
	 * root element of the document.
	 */
	public String toXMLDocument() {
		String xml = marshalledXMLDocument();
		String body = xml.substring(xml.indexOf("?>") + 2);
		body = "<ring>" + body + "</ring>";
		String xmlHeader = xml.substring(0, xml.indexOf("?>") + 2);
		return xmlHeader + "\n" + body;
	}
	
	/**
	 * Returns this object represented as an XML fragment. The XML does not contain
	 * the standard XML processing instruction(s), nor does it contain a root element.
	 */
	@Override
	public String toXML() {
		return marshalledXMLFragment();
	}
	
	private String marshalledXMLFragment() {
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
	
	private String marshalledXMLDocument() {
		try {
			JAXBContext ctx = JAXBContext.newInstance(this.getClass());
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter writer = new StringWriter();
			m.marshal(this, writer);
			String xml = writer.toString();
			
			return xml;
		}
		catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
}
