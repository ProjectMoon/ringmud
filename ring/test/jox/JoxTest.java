package ring.test.jox;

import java.io.IOException;
import java.io.InputStream;

import com.wutka.jox.JOXBeanInputStream;

public class JoxTest {
	public static void main(String[] args) {
		InputStream input = new JoxTest().getClass().getClassLoader().getResourceAsStream("ring/test/jox/beans.xml");
		JOXBeanInputStream joxIn = new JOXBeanInputStream(input);
		try {
			PersonBean person = (PersonBean)joxIn.readObject(PersonBean.class);
			PersonBean person2 = (PersonBean)joxIn.readObject(PersonBean.class);
			System.out.println("person: " + person);
			System.out.println("person2: " + person2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
