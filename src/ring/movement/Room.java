package ring.movement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.movement.PortalNotFoundException;
import ring.movement.LocationManager;
import ring.movement.Portal;
import ring.nrapi.business.BusinessObject;
import ring.commands.WorldObjectSearch;
import ring.entities.Entity;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.persistence.RingConstants;
import ring.world.WorldObject;

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
public class Room extends BusinessObject {
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
	
	@XmlElementWrapper(name = "entities")
	@XmlElement(name = "entity")
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
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
			return exitString.substring(0, exitString.length() - 2) + "[R]";
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
		if (mobiles == null || mobiles.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		
		for (Mobile mob : getMobiles()) {
			if (mob != excludedMobile) {
				sb.append(mob.getShortDescription()).append("[R][WHITE] is here.\n");
			}
		}

		return sb.toString();
	}
	
	public String getMobileList(Mobile excludedMobile) {
		return getMobileList(excludedMobile, 0);
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
	public boolean canEnter(Mobile mov) {
		return mov.canMove();
	}
	
	/**
	 * Not specified by this class, because it does not extend WorldObject.
	 * However, it is still useful to have.
	 * @see ring.world.WorldObject#produceSearchList(Class...)
	 * @param dataTypes
	 * @return A list of world objects, filtered by data types.
	 */
	public List<WorldObject> produceSearchList(Class<?> ... dataTypes) {
		List<WorldObject> objs = new ArrayList<WorldObject>();
		objs.addAll(this.getMobiles());
		objs.addAll(this.getEntities());
		
		return WorldObjectSearch.filterByDataType(objs, dataTypes);
	}
	
	/**
	 * @see ring.world.WorldObject#produceSearchList(List)
	 * @param dataTypes
	 * @return A list of filtered world objects.
	 */
	public List<WorldObject> produceSearchList(List<Class<?>> dataTypes) {
		return produceSearchList(dataTypes.toArray(new Class<?>[0]));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((entities == null) ? 0 : entities.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((mobiles == null) ? 0 : mobiles.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((zone == null) ? 0 : zone.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (entities == null) {
			if (other.entities != null)
				return false;
		} else if (!entities.equals(other.entities))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (mobiles == null) {
			if (other.mobiles != null)
				return false;
		} else if (!mobiles.equals(other.mobiles))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (zone == null) {
			if (other.zone != null)
				return false;
		} else if (!zone.equals(other.zone))
			return false;
		return true;
	}
}
