package ring.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.movement.Zone;
import ring.nrapi.business.AbstractBusinessObject;

/**
 * Class for running XQuery against the XML database and
 * converting the results into an arbitrary object.
 * @author projectmoon
 *
 */
public class XQuery {
	private String query;
	private Loadpoint loadpoint = Loadpoint.STATIC;
	
	public XQuery() {}
	
	public XQuery(String query) {
		this.query = query;
	}
	
	public XQuery(Loadpoint loadpoint, String query) {
		this.loadpoint = loadpoint;
		this.query = query;
	}
	
	public void setLoadpoint(Loadpoint point) {
		loadpoint = point;
	}
	
	public Loadpoint getLoadpoint() {
		return loadpoint;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public <T extends AbstractBusinessObject> List<T> query(Class<T> cl) throws XMLDBException, JAXBException {
		ExistDB db = new ExistDB();
		
		Collection col = getCollection(db);
		List<XMLResource> xmlDocs = db.query(col, getQuery());
		List<T> results = new ArrayList<T>(xmlDocs.size());
		
		for (XMLResource res : xmlDocs) {
			T conv = convertToObject(res, cl);
			results.add(conv);
		}
		
		return results;
	}
	
	private Collection getCollection(ExistDB db) throws XMLDBException {
		if (loadpoint == Loadpoint.GAME) {
			return db.getCollection(ExistDBStore.GAME_COLLECTION);
		}
		else if (loadpoint == Loadpoint.STATIC){
			return db.getCollection(ExistDBStore.STATIC_COLLECTION);
		}
		else if (loadpoint == Loadpoint.PLAYERS) {
			return db.getCollection(ExistDBStore.PLAYERS_COLLECTION);
		}
		else {
			throw new UnsupportedOperationException("Can't load from default loadpoint. Yet.");
		}
	}
	
	private <T extends AbstractBusinessObject> T convertToObject(XMLResource res, Class<T> cl) throws JAXBException, XMLDBException {
		JAXBContext ctx = JAXBContext.newInstance(cl);
		Unmarshaller um = ctx.createUnmarshaller();
		um.setListener(new ReferenceLoader());
		
		T conv = (T)um.unmarshal(res.getContentAsDOM());
		conv.setStoreAsUpdate(true);
		conv.setDocumentID(res.getDocumentId());
		conv.createChildRelationships();
		
		return conv;
	}
}
