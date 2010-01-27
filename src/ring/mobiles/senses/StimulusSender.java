package ring.mobiles.senses;

import ring.mobiles.Mobile;
import ring.mobiles.senses.stimuli.AudioStimulus;
import ring.mobiles.senses.stimuli.OlfactoryStimulus;
import ring.mobiles.senses.stimuli.Stimulus;
import ring.mobiles.senses.stimuli.TactileStimulus;
import ring.mobiles.senses.stimuli.TasteStimulus;
import ring.mobiles.senses.stimuli.VisualStimulus;
import ring.movement.Room;

public class StimulusSender {
	public static void sendStimulus(Room room, Stimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
	
	public static void sendStimulus(Room room, VisualStimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
	
	public static void sendStimulus(Room room, AudioStimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
	
	public static void sendStimulus(Room room, OlfactoryStimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
	
	public static void sendStimulus(Room room, TactileStimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
	
	public static void sendStimulus(Room room, TasteStimulus stimulus, Mobile excludedMobile) {
		for (Mobile mob : room.getMobiles()) {
			if (mob != excludedMobile) {
				mob.getDynamicModel().getSensesGroup().consume(stimulus);
			}
		}
	}
}
