package ring.nrapi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.xmldb.api.base.XMLDBException;

import ring.nrapi.business.BusinessObject;
import ring.persistence.Loadpoint;
import ring.persistence.XQuery;

public class ObjectSearch {
	private static final String XQUERY_START = "for $entry in doc(\"did.xml\")";
	private static final String XQUERY_END = "return <entry uuid=\"{$entry/@uuid}\" />";
	
	@XmlAccessorType(XmlAccessType.PROPERTY)
	@XmlRootElement(name = "entry")
	public static class IndexEntry extends BusinessObject {}
	
	private ObjectIndex index;

	public List<BusinessObject> search(String xpath) {
		String query = XQUERY_START;
		if (!xpath.startsWith("/")) {
			query += "/" + xpath;
		}
		else {
			query += xpath;
		}
		
		query += "\n" + XQUERY_END;
		
		System.out.println(query);
		
		ArrayList<UUID> uuids = new ArrayList<UUID>(0);
		XQuery xq = new XQuery(Loadpoint.GAME, query);
		try {
			List<IndexEntry> entries = xq.execute(IndexEntry.class);
			uuids.ensureCapacity(entries.size());
			
			for (IndexEntry entry : entries) {
				uuids.add(entry.getUuid());
			}
		} 
		catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return index.getAll(uuids);
	}
	
	public ObjectIndex getObjectIndex() {
		return index;
	}
	
	public void setObjectIndex(ObjectIndex index) {
		this.index = index;
	}
}
