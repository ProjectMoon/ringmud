package ring.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Encapsulates the context of an event. An event's context
 * refers to the XML documents to which it the event has scope,
 * and to which IDs in each document the event is bound to.
 * @author projectmoon
 *
 */
public class EventContext {
	private Map<String, Set<String>> documentsAndIDs = new HashMap<String, Set<String>>();
	
	public Set<String> getDocuments() {
		return documentsAndIDs.keySet();
	}
	
	public void addDocument(String doc) {
		documentsAndIDs.put(doc, new HashSet<String>());
	}
	
	public void addID(String doc, String id) {
		Set<String> ids = documentsAndIDs.get(doc);
		if (ids == null) {
			ids = new HashSet<String>();
			documentsAndIDs.put(doc, ids);
		}
		
		ids.add(id);
	}
	
	public Set<String> getIDs(String document) {
		return documentsAndIDs.get(document);
	}
	
	public boolean removeDocument(String document) {
		return (documentsAndIDs.remove(document) != null);
	}
	
	/**
	 * Removes an ID from a specific document scope.
	 * @param document
	 * @param id
	 * @return
	 */
	public boolean removeID(String document, String id) {
		Set<String> ids = documentsAndIDs.get(document);
		if (ids.contains(id)) {
			ids.remove(id);
			documentsAndIDs.put(document, ids);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Removes the specified ID from all documents.
	 * @param id
	 * @return
	 */
	public int unbindID(String id) {
		int removeCount = 0;
		for (String document : documentsAndIDs.keySet()) {
			if (removeID(document, id)) {
				removeCount++;
			}
		}
		
		return removeCount;
	}
	
	public String toString() {
		String res = "";
		for (String document : this.getDocuments()) {
			for (String id : this.getIDs(document)) {
				res += document + ":" + id + "\n";
			}
		}
		
		return res;
	}
}
