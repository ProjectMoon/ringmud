package ring.mobiles.senses;

import java.util.HashMap;
import java.util.Map;

import ring.mobiles.senses.sensors.*;
import ring.mobiles.senses.stimuli.*;

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
	private AuditorySense auditorySense;
	private TactileSense tactileSense;
	private OlfactorySense olfactorySense;
	private TasteSense tasteSense;
	
	private DepictionHandler handler;
	
	public static SensesGroup createDefaultSensesGroup() {
		SensesGroup group = new SensesGroup();
		
		//Eyes
		VisualSense vs = new VisualSense();
		vs.setName("eyes");
		group.setVisualSense(vs);
		
		//Ears
		AuditorySense as = new AuditorySense();
		as.setName("ears");
		group.setAuditorySense(as);
		
		//Touch
		TactileSense ts = new TactileSense();
		ts.setName("sense of touch");
		group.setTactileSense(ts);
		
		//Nose
		OlfactorySense os = new OlfactorySense();
		os.setName("nose");
		group.setOlfactorySense(os);
		
		//Tongue
		TasteSense taste = new TasteSense();
		taste.setName("tongue");
		group.setTasteSense(taste);
		
		return group;
	}
	
	public static SensesGroup createDefaultSensesGroup(DepictionHandler handler) {
		SensesGroup group = createDefaultSensesGroup();
		group.setDepictionHandler(handler);
		return group;
	}	
	
	public DepictionHandler getDepictionHandler() {
		return handler;
	}

	public void setDepictionHandler(DepictionHandler handler) {
		this.handler = handler;		
	}
	
	public boolean canConsume(Stimulus stimulus) {
		//TODO implement something better later, as
		//this will accept custom stimuli.
		return true;
	}

	public void consume(VisualStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = visualSense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	public void consume(AudioStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = auditorySense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	public void consume(TactileStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = tactileSense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	public void consume(OlfactoryStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = olfactorySense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	public void consume(TasteStimulus stimulus) {
		if (canConsume(stimulus)) {
			ProcessedDepiction depiction = tasteSense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	//This is for unknown stimuli types.
	public void consume(Stimulus stimulus) {
		throw new UnsupportedOperationException("SensesGroup cannot yet consume unknown stimuli.");
	}
		
	public VisualSense getVisualSense() {
		return visualSense;
	}
	
	public void setVisualSense(VisualSense sense) {
		visualSense = sense;
	}
	
	public AuditorySense getAuditorySense() {
		return auditorySense;
	}
	
	public void setAuditorySense(AuditorySense sense) {
		auditorySense = sense;
	}
	
	public OlfactorySense getOlfactorySense() {
		return olfactorySense;
	}
	
	public void setOlfactorySense(OlfactorySense sense) {
		olfactorySense = sense;
	}
	
	public TactileSense getTactileSense() {
		return tactileSense;
	}
	
	public void setTactileSense(TactileSense sense) {
		tactileSense = sense;
	}
	
	public TasteSense getTasteSense() {
		return tasteSense;
	}
	
	public void setTasteSense(TasteSense sense) {
		tasteSense = sense;
	}
	
	//Test out senses group!	
	public static void main(String[] args) {
		DepictionHandler handler = new DepictionHandler() {
			@Override
			public void handle(ProcessedDepiction depiction) {
				System.out.println(depiction.getDepiction());
			}
		};
		
		SensesGroup group = SensesGroup.createDefaultSensesGroup(handler);
		
		group.getVisualSense().setDisabledFromBlindness(true);
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction("Hi there this is a visual stimulus");
		stimulus.setBlindDepiction("This is the blind depiction");
		group.consume(stimulus);
		
		group.getVisualSense().setDisabledFromBlindness(false);
		group.consume(stimulus);
		
		//Test unknown stimulus
		Stimulus telepathy = new Stimulus() {
			@Override public String getDepiction() { return "WooOOoooOoo.. I'm in your MIND!"; }
			@Override public void setDepiction(String s) {}
		};
		
		//Should throw an exception
		group.consume(telepathy);
	}


}
