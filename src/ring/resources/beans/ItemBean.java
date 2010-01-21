package ring.resources.beans;

/**
 * Class representing properties of various items in the game.
 * Different subclasses of Item will make use of different properties
 * in this class. Therefore, not all properties will be used by a given
 * type of item.
 * @author projectmoon
 *
 */
public class ItemBean extends RingBean {
	private String type;
	private int ac;
	private String name;

	/**
	 * Returns the type of this item.
	 * @return A string as "armor", "weapon", or "other".
	 */
	public String getItemType() {
		return type;
	}
	
	/**
	 * Sets the type of this item. This allows the resource loader
	 * to determine what type of Item to create. It also determines
	 * which properties of the bean will be used to populate the Item.
	 * @param type
	 */
	public void setItemType(String type) {
		this.type = type;
	}
	

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public int getACBonus() {
		return ac;
	}
	
	public void setACBonus(int ac) {
		this.ac = ac;
	}
}
