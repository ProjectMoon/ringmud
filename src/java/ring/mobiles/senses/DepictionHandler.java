package ring.mobiles.senses;

/**
 * Class that handles the actual forwarding of depiction data to where ever
 * it needs to go. This is attached to a SensesGroup. That Senses group forwards
 * all consumed stimuli data on to the depiction handler for further processing.
 * @author projectmoon
 *
 */
public interface DepictionHandler {
	/**
	 * Handles the processed depiction.
	 * @param depiction
	 */
	public void handle(ProcessedDepiction depiction);
}
