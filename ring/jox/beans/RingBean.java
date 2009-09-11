package ring.jox.beans;

import java.lang.reflect.ParameterizedType;
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
public abstract class RingBean<T> implements Type {
	/**
	 * At the moment, this method is pretty much useless as the parser
	 * still requires a Class object. There's no way to instantiate a T
	 * without a T already in existence somewhere else.
	 * @return
	 */
	public Class getRealClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class) parameterizedType.getActualTypeArguments()[0];
	}
	
	public T getInstance() {
		//Class c =
		return null;
	}

}
