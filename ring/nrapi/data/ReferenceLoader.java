package ring.nrapi.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

import ring.nrapi.entities.Item;
import ring.nrapi.entities.Entity;
import ring.nrapi.mobiles.Mobile;
import ring.nrapi.mobiles.backbone.Equipment;
import ring.nrapi.mobiles.backbone.Inventory;
import ring.nrapi.mobiles.backbone.Equipment.EquipmentTuple;
import ring.nrapi.movement.Room;

/**
 * Unmarshaller listener for following referenced data in the XML.
 * Referenced data is always loaded from the static collection.
 * @author projectmoon
 *
 */
public class ReferenceLoader extends Unmarshaller.Listener {
	public void afterUnmarshal(Object target, Object parent) {
		if (target instanceof Mobile) {
			handleMobile((Mobile)target);
		}
	 	else if (target instanceof Room) {
	 		handleRoom((Room)target);
	 	}
	 	else if (target instanceof Entity) {
	 		handleEntity((Entity)target);
	 	}
	}

	private void handleEntity(Entity entity) {
		
	}

	private void handleRoom(Room target) {
		DataStore ds = DataStoreFactory.getDefaultStore();
		ds.setLoadpoint(Loadpoint.STATIC);
		List<Entity> newEnts = new ArrayList<Entity>(target.getEntities().size());
		
		for (Entity ent : target.getEntities()) {
			//If the entity is referential, replace the referential version
			//with the actual version from the static collection. Otherwise,
			//just add it to the list of new entities.
			if (ent.isReferential()) {
			 	Entity e = DataStoreFactory.getDefaultStore().retrieveEntity(ent.getID());
			 	if (e != null) {
			 		newEnts.add(e);
			 	}
			 	else {
			 		System.err.println("Couldn't follow reference for " + ent.getID() + "; removing it.");
			 	}
			}
			else {
				newEnts.add(ent);
			}
		}
		
		//Replace the old entity list with our new list of complete
		//entities.
		target.setEntities(newEnts);
	}

	private void handleMobile(Mobile target) {
		DataStore ds = DataStoreFactory.getDefaultStore();
		ds.setLoadpoint(Loadpoint.STATIC);
		List<Item> newInv = new ArrayList<Item>(target.getDynamicModel().getInventory().getItems().size());
		List<EquipmentTuple> newEq = new ArrayList<EquipmentTuple>(
				target.getDynamicModel().getEquipment().getEntries().size());
		
		Inventory inv = target.getDynamicModel().getInventory();
		Equipment eq = target.getDynamicModel().getEquipment();
		
		//Inventory first
		for (Item item : inv) {
			if (item.isReferential()) {
				Item i = ds.retrieveItem(item.getID());
				if (i != null) {
					newInv.add(i);
				}
				else {
					System.err.println("Couldn't follow reference for " + item.getID() + "; removing it.");
				}
			}
			else {
				newInv.add(item);
			}
		}
		
		//Then equipment
		for (EquipmentTuple tuple : eq.getEntries()) {
			Item item = tuple.getItem();
			if (tuple.getItem().isReferential()) {
				Item i = ds.retrieveItem(item.getID());
				if (i != null) {
					EquipmentTuple temp = new EquipmentTuple(tuple.getBodyPart(), i);
					newEq.add(temp);
				}
				else {
					System.err.println("Couldn't follow reference for " + item.getID() + "; removing it.");
				}
			}
			else {
				newEq.add(tuple);
			}
		}
		
		//Replace the inventory and equipment.
		Inventory newInventory = new Inventory();
		newInventory.setItems(newInv);
		Equipment newEquipment = new Equipment();
		newEquipment.setEntries(newEq);
		
		target.getDynamicModel().setInventory(newInventory);
		target.getDynamicModel().setEquipment(newEquipment);
	}
}
