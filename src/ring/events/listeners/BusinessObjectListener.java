package ring.events.listeners;

public interface BusinessObjectListener {
	//Fired when the parent of this business object is changed.
	public void parentChanged(BusinessObjectEvent e);
}
