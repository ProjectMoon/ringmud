package ring.test.jox;

import java.io.IOException;
import java.io.InputStream;

import ring.jox.BeanParser;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;
import ring.resources.beans.RoomBeanSet;

import com.wutka.jox.JOXBeanInputStream;

public class JoxTest {
	public static void main(String[] args) {
		
		InputStream input = new JoxTest().getClass().getClassLoader().getResourceAsStream("ring/test/jox/beans.xml");
		BeanParser<RoomBeanSet> parser = new BeanParser<RoomBeanSet>();
		RoomBeanSet set = parser.parse(input, RoomBeanSet.class);
		//set.construct();
		
		System.out.println(LocationManager.getOrigin());
	}
}
