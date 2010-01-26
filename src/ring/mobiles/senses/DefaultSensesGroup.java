package ring.mobiles.senses;

import java.util.HashMap;
import java.util.Map;

import ring.mobiles.senses.sensors.VisualSense;
import ring.mobiles.senses.stimuli.VisualStimulus;

/**
 * A default implementation of the SensesGroup interface. This senses group implements
 * the basic senses of humans and other humanoids. This senses group supports one sense
 * per stimulus. Extra senses per stimuli are rejected with runtime exceptions. 
 * @author projectmoon
 *
 */
public class DefaultSensesGroup implements SensesGroup {
	public static final String EYES = "eyes";
	public static final String EARS = "ears";
	
	private DepictionHandler handler;
	
	private Map<Class<? extends Stimulus>, Sense<? extends Stimulus>> senses = new HashMap<Class<? extends Stimulus>, Sense<? extends Stimulus>>();
	
	@Override
	public DepictionHandler getDepictionHandler() {
		return handler;
	}

	@Override
	public void setDepictionHandler(DepictionHandler handler) {
		this.handler = handler;		
	}
	
	@Override
	public <S extends Stimulus> void addSense(Class<S> stimClass, Sense<S> sense) {
		senses.put(stimClass, sense);		
	}

	@Override
	public boolean canConsume(Stimulus stimulus) {
		Sense<?> sense = senses.get(stimulus.getClass());
		return (sense != null);
	}


	@Override
	public void consume(Stimulus stimulus) {
		if (canConsume(stimulus)) {
			Sense sense = senses.get(stimulus.getClass());
			ProcessedDepiction depiction = sense.process(stimulus);
			handler.handle(depiction);
		}
	}
	
	@Override
	public <S extends Stimulus> Sense<S> getSense(Class<S> stimClass) {
		return (Sense<S>)senses.get(stimClass);
	}

	@Override
	public boolean removeSense(Class<?> stimClass) {
		return (senses.remove(stimClass) != null);
	}
	
	public static void main(String[] args) {
		DefaultSensesGroup group = new DefaultSensesGroup();
		DepictionHandler handler = new DepictionHandler() {
			@Override
			public void handle(ProcessedDepiction depiction) {
				System.out.println(depiction.getDepiction());
			}
		};
		
		group.setDepictionHandler(handler);
		
		group.addSense(VisualStimulus.class, new VisualSense());
		VisualStimulus stimulus = new VisualStimulus();
		stimulus.setDepiction("Hi there this is a visual stimulus");
		stimulus.setBlindDepiction("This is the blind depiction");
		
		VisualSense sense = (VisualSense)group.getSense(VisualStimulus.class);
		sense.setDisabledFromBlindness(true);
		group.consume(stimulus);
	}


}
