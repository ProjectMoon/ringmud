package ring.aspects;
import ring.events.EventDispatcher;
import ring.events.EventEnum;
import ring.nrapi.business.AbstractBusinessObject;
import ring.persistence.XQuery;
import org.xmldb.api.modules.XMLResource;

/**
 * Aspect for system events. System events are lower-level events such as loading an
 * object, unloading an object, persisting an object to the database, etc.
 * @author projectmoon
 *
 */
public aspect SystemEvents {
	/**
	 * Enum for describing system events.
	 * @author projectmoon
	 *
	 */
	public enum SystemEvent implements EventEnum {
		ON_LOAD("onLoad");
		
		private String eventName;
		private SystemEvent(String name) {
			eventName = name;
		}
		
		public String getEventName() {
			return eventName;
		}
	}
	
	/**
	 * onLoad event for non-root objects in an AbstractBusinessObject hierarchy.
	 * @param obj The abstract business object.
	 */
	after(AbstractBusinessObject obj) returning(): 
		call(void AbstractBusinessObject.createChildRelationship(..)) && args(.., obj) {
			EventDispatcher.dispatch(SystemEvent.ON_LOAD, obj);
	}
	
	/**
	 * onLoad event for root objects in an AbstractBusinessObject hierarchy.
	 * Matches return type of <code>XQuery.convertToObject</code> instead.
	 * @param obj The abstract business object.
	 */
	after() returning(AbstractBusinessObject obj):
		call(AbstractBusinessObject XQuery.convertToObject(XMLResource, Class)) {
			EventDispatcher.dispatch(SystemEvent.ON_LOAD, obj);
	}
	
	
}