package ring.jox;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ring.resources.beans.RingBean;

import com.wutka.jox.JOXBeanInputStream;

/**
 * The class that is responsible for parsing game data files. Genericized for
 * your convenience!
 * 
 * @author projectmoon
 * 
 */
public class BeanParser<T extends RingBean> {
	public BeanParser() {
		//Reflection trickery to instantiate T without having to supply a T.
		//Class c =
	}
	
	@SuppressWarnings("unchecked")
	public T parse(String xmlFile, Class c) {
		try {
			InputStream input = new FileInputStream(xmlFile);
			JOXBeanInputStream joxIn = new JOXBeanInputStream(input);
			T ret = (T)joxIn.readObject(c);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T parse(InputStream xmlStream, Class c) {
		try {
			JOXBeanInputStream joxIn = new JOXBeanInputStream(xmlStream);

			T ret = (T)joxIn.readObject(c);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
