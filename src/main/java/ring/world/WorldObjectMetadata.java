package ring.world;

/**
 * Presents a unified object where metadata about world objects can
 * be collected. Every world object implements the <code>getMetadata()</code>
 * method for retrieving objects of this class. This metadata contains useful
 * information about the world object. It is mostly used in searching and
 * the like.
 * @author projectmoon
 *
 */
public class WorldObjectMetadata {
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
