package ring.events.listeners;

import java.util.EventListener;

/**
 * Listener interface that BusinessObjects can add to
 * in order to respond to events concerning themselves
 * or objects contained within themselves.
 * @author projectmoon
 *
 */
public interface BusinessObjectListener extends EventListener {
	/**
	 * Fired when the parent of this BusinessObject changes.
	 * @param e
	 */
	public void parentChanged(BusinessObjectEvent e);
}
