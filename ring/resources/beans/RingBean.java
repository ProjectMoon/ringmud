package ring.resources.beans;

import java.lang.reflect.Type;

/**
 * This empty abstract class is extended by all of the beans in this package
 * to get around the fact that Java does not store types of generic classes
 * at runtime. It does store, though, this information in child classes
 * extending from a genericize abstract superclass. This was originally conceived
 * to attempt to get around Java's type erasure, but it doesn't quite do the job.
 * That is, you still have to have another one around somewhere 
 * @author projectmoon
 *
 * @param <T>
 */
public abstract class RingBean implements Type {
	protected String beanID;
	
	/**
	 * Returns the unique ID of this bean.
	 * @return
	 */
	public String getID() {
		return beanID;
	}
	
	/**
	 * Sets the unique ID of this bean.
	 * @param id
	 */
	public void setID(String id) {
		beanID = id;
	}
}
