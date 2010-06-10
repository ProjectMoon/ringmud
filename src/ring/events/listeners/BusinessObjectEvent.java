package ring.events.listeners;

import java.util.EventObject;
import ring.nrapi.business.BusinessObject;
import ring.persistence.Persistable;

public class BusinessObjectEvent extends EventObject {
	private Persistable source;
	
	public BusinessObjectEvent(Persistable bo) {
		super(bo);
		source = bo;
	}
	
	public Persistable getSourceObject() {
		return source;
	}
	
	@Override
	public Object getSource() {
		return getSourceObject();
	}
}
