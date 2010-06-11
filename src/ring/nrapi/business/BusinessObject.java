package ring.nrapi.business;

import java.util.List;

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
	public void addBusinessObjectListener(BusinessObjectListener listener);
	public boolean removeBusinessObjectListener(BusinessObjectListener listener);
	public List<BusinessObjectListener> getBusinessObjectListeners();
}
