package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.stimuli.TasteStimulus;

public class TasteSense implements Sense<TasteStimulus> {
	private String name;
		
	@Override
	public ProcessedDepiction process(TasteStimulus stimulus) {
		if (!isDisabled()) {
			return new ProcessedDepiction(stimulus.getDepiction());
		}
		else {
			return new ProcessedDepiction("Your " + getName() + " can't taste anything!");
		}
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
