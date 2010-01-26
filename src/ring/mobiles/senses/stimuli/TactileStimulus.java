package ring.mobiles.senses.stimuli;

/**
 * Class representing a tactile stimulus.
 * @author projectmoon
 *
 */
public class TactileStimulus implements Stimulus {
	private String depiction;
	
	@Override
	public String getDepiction() {
		return depiction;
	}

	@Override
	public void setDepiction(String depiction) {
		this.depiction = depiction;
	}	
}
