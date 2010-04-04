package ring.persistence;

import javax.xml.bind.Unmarshaller;

import ring.nrapi.business.AbstractBusinessObject;

/**
 * Unmarshalling listener for setting up parent relationships. 
 * The parent takes care of creating child relationships later, 
 * after it has been loaded into memory.
 * @author projectmoon
 *
 */
public class ParentRelationshipCreator extends Unmarshaller.Listener {
	public void afterUnmarshal(Object target, Object parent) {
		if (target instanceof AbstractBusinessObject) {
			if (parent instanceof AbstractBusinessObject) {
				AbstractBusinessObject po = (AbstractBusinessObject)parent;
				po.addChild((AbstractBusinessObject)target);
			}
		}
	}
}
