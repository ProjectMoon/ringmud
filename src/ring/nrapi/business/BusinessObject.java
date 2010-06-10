package ring.nrapi.business;

import ring.persistence.Persistable;
import ring.events.listeners.BusinessObjectListener;

/**
 * Represents a game business object. All BusinessObjects are also
 * Persistable.
 * @author projectmoon
 * @see Persistable
 */
public interface BusinessObject extends Persistable {
	public void save();
}
