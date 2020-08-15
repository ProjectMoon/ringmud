package ring.nrapi;

import ring.nrapi.business.BusinessObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains methods for starting and stopping the Object Index System.
 * @author projectmoon
 *
 */
public class ObjectIndexSystem {
	public static void start() {
		System.err.println("[FIXME] Delete this method! (probably, because we will get rid of ObjectIndexSystem)");
	}

	public static ObjectSearch newSearch() {
		ObjectSearch search = new ObjectSearch() {
			@Override
			public List<BusinessObject> search(String xpath) {
				return new ArrayList<>();
			}
		};
		return search;
	}
}
