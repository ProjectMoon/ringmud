package ring.nrapi;

import ring.nrapi.business.BusinessObject;

import java.util.List;

public interface ObjectSearch {
	List<BusinessObject> search(String xpath);
}
