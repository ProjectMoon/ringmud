package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.stimuli.TactileStimulus;

public class TactileSense implements Sense<TactileStimulus> {
	private String name;
		
	@Override
	public ProcessedDepiction process(TactileStimulus stimulus) {
		return new ProcessedDepiction(stimulus.getDepiction());
	}

	@Override
	public boolean isDisabled() {
		return false;
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
