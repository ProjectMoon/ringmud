package ring.nrapi.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.nrapi.movement.PortalNotFoundException;
import ring.nrapi.movement.LocationManager;
import ring.nrapi.movement.Movable;
import ring.nrapi.movement.Portal;
import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.entities.Entity;
import ring.nrapi.items.Item;
import ring.nrapi.mobiles.Mobile;
import ring.persistence.RingConstants;
import ring.world.World;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"model",
	"zoneID",
	"items",
	"mobiles",
	"entities"
})
public class Room extends AbstractBusinessObject {
	private static final String NO_EXITS_STRING = "[B][RED]NO EXITS![R][WHITE]";
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Mobile> mobiles = new ArrayList<Mobile>();
	private List<Item> items = new ArrayList<Item>();
	
	private RoomModel model = new RoomModel();
	private Zone zone;
	
	@XmlElement
	public RoomModel getModel() {
		return model;
	}
	
	public void setModel(RoomModel model) {
		this.model = model;
	}
	
	@XmlElement
	public String getZoneID() {
		return "testID";
		//return zone.getID();
	}
	
	public void setZoneID(String id) {
		//TODO implement setZoneID
		//if zone already exists, compare the two, and change if necessary
		//else load new zone corresponding to ID and attempt to set zone to that.		
	}
	
	@XmlTransient
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
		setZoneID(zone.getID());
	}
	
	@Override
	public void createChildRelationships() {
		for (Entity ent : getEntities()) {
			ent.setParent(this);
		}
		
		for (Item item : getItems()) {
			item.setParent(this);
		}
		
		for (Mobile mob : getMobiles()) {
			mob.setParent(this);
		}
		
	}
	
	@XmlElementWrapper(name = "entities")
	@XmlElement(name = "entity")
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
		createChildRelationships();
	}	
	
	@XmlElementWrapper(name = "mobiles")
	@XmlElement(name = "mobile")
	public List<Mobile> getMobiles() {
		return mobiles;
	}
	
	public void setMobiles(List<Mobile> mobiles) {
		this.mobiles = mobiles;
	}
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	public List<Item> getItems() {
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addMobile(Mobile mob) {
		mobiles.add(mob);
		mob.setParent(this);
	}
	
	public boolean removeMobile(Mobile mob) {
		if (mobiles.contains(mob)) {
			mob.setParent(null);
			return mobiles.remove(mob);
		}
		else {
			return false;
		}
	}
	
	public void addEntity(Entity ent) {
		entities.add(ent);
		ent.setParent(this);
	}
	
	public boolean removeEntity(Entity ent) {
		if (entities.contains(ent)) {
			ent.setParent(null);
			return entities.remove(ent);
		}
		else {
			return false;
		}
	}
	
	public void addItem(Item item) {
		item.setParent(this);
		items.add(item);
	}
	
	public boolean removeItem(Item item) {
		if (items.contains(item)) {
			item.setParent(null);
			return items.remove(item);
		}
		else {
			return false;
		}
	}
	
	/**
	 * Convenience method for getting a list of exits in a
	 * Room. Only returns exits that are plainly visible.
	 * That is, it returns exits that have a search DC of 0.
	 * @return The list of exits.
	 */
	public String getExitsString() {
		return getExitsString(0);	
	}

	/**
	 * Convenience method for getting a list of exits in a Room,
	 * including hidden ones that are visible given the search check
	 * parameter.
	 * @param searchCheck
	 * @return The list of exits found based on the search check result.
	 */
	public String getExitsString(int searchCheck) {
		List<Portal> exits = LocationManager.getPortals(this);
		
		if (exits == null) {
			return NO_EXITS_STRING;
		}
		
		String exitString = "[CYAN]";
		
		//Add all exits to the string that are
		//that the search check allows us to see.
		for (Portal p : exits) {
			if (p.getSearchDC() > 0) {
				if (searchCheck >= p.getSearchDC()) {
					exitString += p.getDisplayName() + ", ";
				}
			}
			else {
				exitString += p.getDisplayName() + ", ";
			}
		}
		
		//Remove the last ", " and return
		//Or, if we have nothing... return an empty string. Bit of an ugly check.
		if (exitString.equals("[CYAN]")) {
			return "";
		}
		else {
			return exitString.substring(0, exitString.length() - 2);
		}
	}	
		
	public String generateBlindExitsString(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public String generateRandomOccupantsList(int searchCheck) {
		throw new UnsupportedOperationException();
	}
	
	public String getEntityList() {
		String res = "";

		if (entities == null || entities.isEmpty())
			return res;

		StringBuilder sb = new StringBuilder();
		
		for (Entity ent : getEntities()) {
			sb.append("[R][WHITE]")
			.append(ent.getName())
			.append(" ")
			.append(ent.getDescriptor())
			.append("[R][WHITE].\n");
		}

		return res;
	}
	
	public String getMobileList(Mobile excludedMobile, int spotCheck) {
		String res = "";
		if (mobiles == null || mobiles.isEmpty())
			return res;

		StringBuilder sb = new StringBuilder();
		
		for (Mobile mob : getMobiles()) {
			//TODO take care of hiding Mobiles.
			sb.append(mob.getBaseModel().getShortDescription())
			.append("[R][WHITE] is here.\n");
		}

		return res;
	}
	
	public String getMobileList(Mobile excludedMobile) {
		String res = "";
		if (mobiles == null || mobiles.isEmpty())
			return res;

		StringBuilder sb = new StringBuilder();
		
		for (Mobile mob : getMobiles()) {
			//TODO take care of hiding Mobiles.
			sb.append(mob.getBaseModel().getShortDescription())
			.append("[R][WHITE] is here.\n");
		}

		return res;
	}
	
	/**
	 * Convenience method for querying the Location Manager for finding
	 * the desired Portal. This method is equivalent to calling the following method:
	 * LocationManager.getDestination(this, direction).
	 * @param direction The string identifier of the desired Portal.
	 * @return The Portal object, if one exists. null otherwise.
	 */
	public Portal getPortal(String direction) {
		try {
			return LocationManager.getPortal(this, direction);
		}
		catch (PortalNotFoundException e) {
			//TODO log this.
			e.printStackTrace();
			System.out.println("uh oh");
			return null;
		}
	}
	
	/**
	 * Whether or not a Movable can enter this Room. Default implementation
	 * allows all Movables to enter.
	 * @param mov
	 * @return
	 */
	public boolean canEnter(Movable mov) {
		return true;
	}
	
	/**
	 * Callback event when a Movable enters this Room.
	 * @param m
	 * @param from
	 */
	public void movableEnters(Movable m, Portal from) {
		RoomModel model = getModel();
		System.out.println(m + " has arrived from " + from.getDisplayName());
		System.out.println("new room: " + model.getTitle());
		
		Mobile mob = (Mobile)m;
	
		// default arrive and leave text to broadcast to others
		String arriveText = mob.getBaseModel().getName() + " [R][WHITE]arrives from "
				+ from.getDisplayName() + ".";


		//Reset certain skill checks.
		//TODO pending skills rewrite

		//TODO pending integration of rewritten classes.
		//World.roomArriveLeaveToLocation(mob, arriveText, "[R][WHITE]You hear the sounds of someone arriving.");

		// subtract the right amount of move points.
		mob.getDynamicModel().changeCurrentMV(-1);

		// display the new information of the room
		if (mob.getBaseModel().isBlind())
			mob.sendData("You stumble into a new area, but you cannot see anything!");
		else {
			mob.sendData(model.getTitle() + "\n"
				+ model.getDescription() + "\n"
				//+ this.getExitsString(mob.getSearchCheck()) + "\n"
				//+ this.getMobileList(mob, mob.getDynamicModel().getSearchCheck())
				//TODO ^ pending skills rewrite
				+ this.getEntityList());
		}
	}

	/**
	 * Callback event when a Movable leaves this room.
	 * @param m
	 * @param to
	 */
	public void movableLeaves(Movable m, Portal to) {
		System.out.println(m + " leaves to " + to.getDisplayName());
		
		Mobile mob = (Mobile)m;
		
		// default arrive and leave text to broadcast to others
		String arriveText = mob.getBaseModel().getName() + " [R][WHITE]leaves to "
				+ to.getDisplayName() + ".";

		// reset the character's search, listen, and spot checks.
		//TODO pending skills rewrite

		//World.roomArriveLeaveToLocation(mob, arriveText, "[R][WHITE]You hear the sounds of someone leaving.");
		//TODO pending nrapi integration
	}
}
