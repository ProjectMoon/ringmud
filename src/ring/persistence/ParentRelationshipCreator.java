package ring.persistence;

import javax.xml.bind.Unmarshaller;

import ring.nrapi.business.AbstractBusinessObject;
import ring.events.listeners.*;

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
				final AbstractBusinessObject to = (AbstractBusinessObject)target;
				
				to.addBusinessObjectListener(new BusinessObjectListener() {
					@Override
					public void parentChanged(BusinessObjectEvent e) {
						System.out.println("parent for " + to + " changed to: " + e.getSource());
					}
				});
				po.addChild(to);
			}
		}
	}
}
