package ring.test.jox;

import java.io.IOException;
import java.io.InputStream;

import com.wutka.jox.JOXBeanInputStream;

public class JoxTest {
	public static void main(String[] args) {
		InputStream input = new JoxTest().getClass().getClassLoader().getResourceAsStream("ring/test/jox/beans.xml");
		JOXBeanInputStream joxIn = new JOXBeanInputStream(input);
		try {
			Item person = (Item)joxIn.readObject(Item.class);
			System.out.println("person: " + person.getLongDescription());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
