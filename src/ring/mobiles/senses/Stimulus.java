package ring.mobiles.senses;

/**
 * Interface representing a sense stimulus. This could be vision, hearing,
 * touching, etc.
 * @author projectmoon
 *
 */
public interface Stimulus {
	/**
	 * Gets the text depiction of this stimulus.
	 * @return the depiction.
	 */
	public String getDepiction();
	
	/**
	 * Sets the textual depiction of this stimulus.
	 * @param depiction
	 */
	public void setDepiction(String depiction);
}
