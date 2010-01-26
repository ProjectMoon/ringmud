package ring.mobiles.senses;

/**
 * Interface representing a "sense." A creature that has a sense can
 * consume stimuli related to the sense. As a practical example, a
 * creature that has (working) eyes can see, and thus has a visual sense
 * that can consume visual stimuli.
 * @author projectmoon
 *
 */
public interface Sense<S extends Stimulus> {
	/**
	 * Returns true if this sense is disabled in some way or another.
	 * A sense that is disabled should consume its related sense stimulus
	 * with impaired or no effectiveness. For example, a Mobile that cannot
	 * see would not receive any visual stimuli.
	 * @return true or false
	 */
	public boolean isDisabled();
	
	/**
	 * Processes a received stimulus. If the sense is working properly (i.e. isDisabled() == false),
	 * the stimulus should be processed with full effectiveness. If the sense is disabled,
	 * the stimulus should be processed with limited effectiveness or not processed
	 * at all.
	 * @param stimulus
	 * @return The processed depiction of the stimulus. If the sense is working properly, this generally
	 * corresponds exactly to the stimulus' getDepiction() method.
	 */
	public ProcessedDepiction process(S stimulus);
	
	public String getName();
	public void setName(String name);
}
