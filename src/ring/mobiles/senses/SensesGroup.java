package ring.mobiles.senses;

/**
 * Represents a group of senses that a Mobile has. The senses group has a 
 * consume method that takes any kind of stimulus. It is up to the 
 * implementation of this interface to delegate the proper stimulus to the proper sense.
 * Senses are stored by name, so a Mobile may have several different ways of
 * detecting an individual stimulus.
 * @author projectmoon
 *
 */
public interface SensesGroup {
	/**
	 * Consumes a stimulus in some fashion or another. After being consumed,
	 * the processed depiction of this stimulus is passed on to the depiction
	 * handler for final processing and delivery to the Mobile. Note that consuming
	 * a stimulus does not necessarily mean that stimulus is fully processed (or processed at all).
	 * A deaf person consumes audio stimuli, but does not process it.
	 * @param stimulus
	 */
	public void consume(Stimulus stimulus);
	
	/**
	 * Detect whether or not this senses group can consume the specified stimulus.
	 * This method is not entirely intuitive; some senses groups may delegate unknown
	 * stimulus types to another method to see if there is another way for that stimulus
	 * to be handled. Thus, this method would return true, as the stimulus can be "consumed"
	 * in some fashion.
	 * @param stimulus
	 * @return true if this stimulus can be consumed.
	 */
	public boolean canConsume(Stimulus stimulus);
	
	/**
	 * Add a sense to this senses group.
	 * @param name
	 * @param sense
	 */
	public <S extends Stimulus> void addSense(Class<S> stimClass, Sense<S> sense);
	
	/**
	 * Get a sense by its name.
	 * @param name
	 * @return The sense, or null if none is found.
	 */
	public <S extends Stimulus> Sense<S> getSense(Class<S> stimClass);
	
	/**
	 * Remove a sense.
	 * @param name
	 * @return true if a sense was removed, false otherwise.
	 */
	public boolean removeSense(Class<?> stimClass);
	
	/**
	 * Gets the depiction handler for this senses group.
	 * @return the handler.
	 */
	public DepictionHandler getDepictionHandler();
	
	/**
	 * Sets the depiction handler for this senses group.
	 * @param handler
	 */
	public void setDepictionHandler(DepictionHandler handler);
}
