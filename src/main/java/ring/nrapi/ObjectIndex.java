package ring.nrapi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.UUID;

import org.xmldb.api.base.XMLDBException;

import ring.nrapi.business.BusinessObject;
import ring.persistence.Loadpoint;
import ring.persistence.XQuery;

/**
 * The in-memory index of game objects. The ObjectIndex implements a {@link java.util.WeakHashMap}
 * to relate UUIDs to BusinessObjects. This in-memory index is backed by and synchronized with 
 * the XML datastore. The XML datastore is used for creating a list of UUIDs as a result of an
 * XPath search. 
 * @author projectmoon
 *
 */
public class ObjectIndex {
	private WeakHashMap<UUID, WeakReference<BusinessObject>> index = new WeakHashMap<UUID, WeakReference<BusinessObject>>();
	private ReferenceQueue<BusinessObject> refQueue;
	
	public synchronized void addObject(BusinessObject bo) {
		WeakReference<BusinessObject> ref = new WeakReference<BusinessObject>(bo, refQueue);
		index.put(bo.getUuid(), ref);
		
		if (bo.isRoot()) {
			insertObject(bo);
		}
	}
	
	public synchronized BusinessObject get(UUID key) {
		WeakReference<BusinessObject> ref = index.get(key);
		if (ref != null) {
			return ref.get();
		}
		else {
			return null;
		}
	}
	
	public synchronized BusinessObject removeKey(UUID key) {
		return index.remove(key).get();
	}
	
	protected synchronized void removeAll() {
		index.clear();
	}
	
	protected Collection<BusinessObject> getAll() {
		Set<BusinessObject> bos = new HashSet<BusinessObject>();
		for (WeakReference<BusinessObject> ref : index.values()) {
			bos.add(ref.get());
		}
		
		return bos;
	}
	
	public List<BusinessObject> getAll(List<UUID> keys) {
		List<BusinessObject> bos = new ArrayList<BusinessObject>(keys.size());
		
		for (UUID key : keys) {
			BusinessObject bo = get(key);
			bos.add(bo);
		}
		
		return bos;
	}
	
	private void insertObject(BusinessObject bo) {
		try {
			InputStream query = this.getClass().getResourceAsStream("/ring/nrapi/index_insert.xq");
			XQuery xq = new XQuery(query);
			xq.setLoadpoint(Loadpoint.GAME);
			String queryString = xq.getQuery();
			queryString = queryString.replace("$bo", bo.toXML());
			xq.setQuery(queryString);
			xq.executeUpdate();
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void setReferenceQueue(ReferenceQueue<BusinessObject> refQueue) {
		this.refQueue = refQueue;
	}
}
