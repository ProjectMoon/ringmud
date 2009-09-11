package ring.test.jox;

import java.io.IOException;
import java.io.InputStream;

import ring.jox.BeanParser;
import ring.jox.beans.RoomSet;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;

import com.wutka.jox.JOXBeanInputStream;

public class JoxTest {
	public static void main(String[] args) {
		InputStream input = new JoxTest().getClass().getClassLoader().getResourceAsStream("ring/test/jox/beans.xml");
		BeanParser<RoomSet> parser = new BeanParser<RoomSet>();
		RoomSet set = parser.parse(input, RoomSet.class);
		set.construct();
		System.out.println(LocationManager.getOrigin());
	}
}
