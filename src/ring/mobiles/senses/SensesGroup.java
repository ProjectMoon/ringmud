package ring.mobiles.senses;

import java.util.HashMap;
import java.util.Map;

import ring.mobiles.senses.sensors.Sense;
import ring.mobiles.senses.sensors.VisualSense;
import ring.mobiles.senses.stimuli.Stimulus;
import ring.mobiles.senses.stimuli.VisualStimulus;

/**
 * A group of senses. A senses group can have up to 5 different senses:
 * auditory, visual, tactile, olfactory (smell), or taste. A senses group
 * consumes stimuli sent to it. The group is capable of determining what
 * sense to use to process the stimulus. If the group does not have a sense
 * to consume a stimulus, it silently ignores the stimulus as if it were
 * not perceived at all.
 * <br/><br/>
 * Note that there is a difference between "consuming" and "processing" a
 * stimulus. A senses group can consume any stimulus that it has a sense for,
 * even if that sense is disabled. A disabled sense may not be able to PROCESS
 * a stimulus properly, however.
 * @author projectmoon
 *
 */
public class SensesGroup {
	private VisualSense visualSense;
	//private AuditorySense auditorySense;
	//private TactileSense tactileSense;
	//private OlfactorySense olfactorySense;
	//private TasteSense tasteSense;
	
	private DepictionHandler handler;
	
	public DepictionHandler getDepictionHandler() {
		return handler;
	}

	public void setDepictionHandler(DepictionHandler handler) {
		this.handler = handler;		
	}
	
	public boolean canConsume(Stimulus stimulus) {
		return true; //TODO overload.
	}

	public void consume(VisualStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = visualSense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	/*
	public void consume(AudioStimulus stimulus) {

	}
	
	public void consume(TactileStimulus stimulus) {

	}
	
	public void consume(OlfactoryStimulus stimulus) {

	}
	
	public void consume(Tastestimulus stimulus) {

	}
	*/
	
	public VisualSense getVisualSense() {
		return visualSense;
	}
	
	public void setVisualSense(VisualSense sense) {
		visualSense = sense;
	}	
	
	public static void main(String[] args) {
		SensesGroup group = new SensesGroup();
		DepictionHandler handler = new DepictionHandler() {
			@Override
			public void handle(ProcessedDepiction depiction) {
				System.out.println(depiction.getDepiction());
			}
		};
		
		group.setDepictionHandler(handler);
		
		group.setVisualSense(new VisualSense());
		group.getVisualSense().setDisabledFromBlindness(true);
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction("Hi there this is a visual stimulus");
		stimulus.setBlindDepiction("This is the blind depiction");
		group.consume(stimulus);
	}


}
