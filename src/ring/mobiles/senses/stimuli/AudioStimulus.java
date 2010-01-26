package ring.mobiles.senses.stimuli;

/**
 * Class representing an audiovisual stimulus. Objects of this class
 * are received by StimuliListeners and processed accordingly. In the
 * case of players, this data is sent to them and displayed in different
 * ways depending on their status (blind, deaf, etc).
 * @author projectmoon
 *
 */
public class AudioStimulus implements Stimulus {
	private String depiction;
	private String deafDepiction;
	
	@Override
	public String getDepiction() {
		return depiction;
	}

	@Override
	public void setDepiction(String depiction) {
		this.depiction = depiction;
	}	
	
	public String getDeafDepiction() {
		return deafDepiction;
	}
	
	public void setDeafDepiction(String depiction) {
		deafDepiction = depiction;
	}

}
