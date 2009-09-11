package ring.jox;

import java.lang.reflect.ParameterizedType;

import ring.jox.beans.RingBean;
import ring.mobiles.Mobile;

public class TestType extends RingBean<Mobile> {
	public Class returnedClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class) parameterizedType.getActualTypeArguments()[0];
	}

	public static void main(String[] args) {
		System.out.println(new TestType().returnedClass());
	}
}
