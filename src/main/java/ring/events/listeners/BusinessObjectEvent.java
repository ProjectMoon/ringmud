package ring.events.listeners;

import java.util.EventObject;

import ring.nrapi.business.BusinessObject;

/**
 * A BusinessObjectEvent is a high-level event that all business objects
 * are interested in. This event class is very simple, and only contains
 * the business object that fired the event.
 * @author projectmoon
 *
 */
public class BusinessObjectEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new business object event with the specified
	 * business object as the source.
	 * @param bo
	 */
	public BusinessObjectEvent(BusinessObject bo) {
		super(bo);
	}
}
