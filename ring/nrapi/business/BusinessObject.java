package ring.nrapi.business;

import ring.nrapi.data.Persistable;

/**
 * Represents a game business object. All BusinessObjects are also
 * Persistable.
 * @author projectmoon
 * @see Persistable
 */
public interface BusinessObject extends Persistable {
	public void save();
}
