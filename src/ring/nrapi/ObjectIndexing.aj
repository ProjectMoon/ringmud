package ring.nrapi;

import java.util.UUID;

import ring.nrapi.business.BusinessObject;


/**
 * Aspect that adds new objects to the object index.
 * @author projectmoon
 *
 */
public aspect ObjectIndexing {
	//Unused right now
	pointcut constructorExecution(): (execution(public BusinessObject.new(..)));
	
	pointcut createChildObject(BusinessObject obj): execution(void BusinessObject.createChildRelationship(..)) && args(.., obj);
	pointcut createRootObject(BusinessObject obj): execution(void BusinessObject.createChildRelationships()) && target(obj) && if (obj.isRoot());
	
	private void addBusinessObject(BusinessObject bo) {
		if (!(bo instanceof ObjectSearch.IndexEntry)) {
			if (bo.getUuid() == null) {
				bo.setUuid(UUID.randomUUID());
			}
			ObjectIndex index = ObjectIndexSystem.getObjectIndex();
			index.addObject(bo);
		}
	}
	
	after(BusinessObject bo): createChildObject(bo) || createRootObject(bo) {
		addBusinessObject(bo);
	}
	
	//Logging
	before(BusinessObject bo): call(void ObjectIndex.addObject(..)) && args(bo) {
		System.out.println("Adding " + bo + " to the object index.");
		System.out.println("   " + bo + " is a root? " + bo.isRoot());
	}
	
	after(BusinessObject bo): call(void ObjectIndex.addObject(..)) && args(bo) {
		System.out.println("Added " + bo + " to the object index.");
	}
	
	before(BusinessObject bo): execution(void IndexCleanupDaemon.remove(BusinessObject)) && args(bo) {
		System.out.println("Removing " + bo + " from the index.");
	}

	after(BusinessObject bo): execution(void IndexCleanupDaemon.remove(BusinessObject)) && args(bo) {
		System.out.println("Successfully removed " + bo + " from the index.");
	}
}
