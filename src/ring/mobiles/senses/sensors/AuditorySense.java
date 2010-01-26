package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.stimuli.AudioStimulus;

public class AuditorySense implements Sense<AudioStimulus> {
	private String name;
	private boolean deaf;
	
	@Override
	public ProcessedDepiction process(AudioStimulus stimulus) {
		if (!isDisabled()) {
			return new ProcessedDepiction(stimulus.getDepiction());
		}
		else {
			return new ProcessedDepiction(stimulus.getDeafDepiction());
		}
	}

	@Override
	public boolean isDisabled() {
		return deaf;
	}
	
	public void setDeafness(boolean value) {
		deaf = value;
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
