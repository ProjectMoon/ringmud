package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.Sense;
import ring.mobiles.senses.Stimulus;
import ring.mobiles.senses.stimuli.VisualStimulus;

public class VisualSense implements Sense<VisualStimulus> {
	private boolean disabledFromBlindness;
	private boolean disabledFromDarkness;
	private String name;
	
	@Override
	public ProcessedDepiction process(VisualStimulus stimulus) {
		if (!isDisabled()) {
			return new ProcessedDepiction(stimulus.getDepiction());
		}
		else {
			return new ProcessedDepiction(stimulus.getBlindDepiction());
		}
	}

	@Override
	public boolean isDisabled() {
		return (disabledFromBlindness || disabledFromDarkness);
	}
	
	public boolean disabledFromBindness() {
		return disabledFromBlindness;
	}

	public boolean disabledFromDarkness() {
		return disabledFromDarkness;
	}
	
	public void setDisabledFromBlindness(boolean value) {
		disabledFromBlindness = value;
	}
	
	public void setDisabledFromDarkness(boolean value) {
		disabledFromDarkness = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}
