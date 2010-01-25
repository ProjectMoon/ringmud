package ring.movement;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.xmldb.api.base.XMLDBException;

import ring.items.Item;
import ring.persistence.XQuery;
import ring.system.MUDConfig;

/**
 * Builds the world.
 * @author projectmoon
 *
 */
public class WorldBuilder {
	public static void buildWorld() {
		XQuery xq = new XQuery();
		String query = "for $loc in //item return $loc";
		xq.setQuery(query);
		
		try {
			List<Item> locs = xq.query(Item.class);
			System.out.println("Printing stuff: " + locs.size());
			for (Item loc : locs) {
				System.out.println(loc);
			}
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MUDConfig.loadProperties();
		WorldBuilder.buildWorld();
	}
}
