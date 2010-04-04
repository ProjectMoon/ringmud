package ring.world;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.nrapi.business.AbstractBusinessObject;

/**
 * This is a class that represents an object that is in "the world." 
 * @author projectmoon
 * 
 */
public abstract class WorldObject extends AbstractBusinessObject {
	public abstract WorldObjectMetadata getMetadata();
}
