package ring.world;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.util.List;

import ring.nrapi.business.BusinessObject;

/**
 * This is a class that represents an object that is in "the world." 
 * @author projectmoon
 * 
 */
public abstract class WorldObject extends BusinessObject {
	/**
	 * Retrieve the metadata for this WorldObject. Metadata aggregates
	 * information that is common to all WorldObjects, but that might not be
	 * stored in the same places in each WorldObject.
	 * @return The metadata.
	 */
	public abstract WorldObjectMetadata getMetadata();
	
	/**
	 * This method, given var-args array of data types represented by Class objects,
	 * returns a list of all WorldObjects contained within this WorldObject if and only
	 * if the contained world object is an instance of the specified data type. This method
	 * never returns null.
	 * <br/><br/>
	 * WorldObjects that do not contain any child WorldObjects should simply return a 0-length
	 * list.
	 * @param dataTypes
	 * @return A filtered list of WorldObjects meeting the above conditions. Never null. 
	 */
	public abstract List<WorldObject> produceSearchList(Class<?> ... dataTypes);
	
	/**
	 * As the main produceSearchList method, but with a List parameter instead of var-args.
	 * @see WorldObject#produceSearchList(Class...)
	 * @param dataTypes
	 * @return
	 */
	public abstract List<WorldObject> produceSearchList(List<Class<?>> dataTypes);
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
