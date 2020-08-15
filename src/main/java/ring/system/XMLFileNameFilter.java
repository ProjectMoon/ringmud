package ring.system;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Simple class that filters out non- .xml files.
 * @author projectmoon
 *
 */
public class XMLFileNameFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return name.endsWith(".xml");
	}

}
