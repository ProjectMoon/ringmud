package ring.mobiles.npc;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.world.TickerEvent;
import ring.world.TickerListener;

@XmlRootElement(name = "npc")
public class NPC extends Mobile implements CommandSender, TickerListener {
	public static final long serialVersionUID = 1;
	
	private MobAI ai;

	public NPC() {
		super();
	}

	public synchronized void processTick(TickerEvent e) {
		super.processTick(e);
		ai.act();
	}

	public void doCommand(String command) {
		// Was anything even "typed?"
		if (command.length() > 0) {
			// Send the command.
			super.handler.sendCommand(command);
		}
	}

	public String toString() {
		return super.getBaseModel().getName();
	}
	
	@XmlTransient
	public MobAI getAI() {
		return ai;
	}
	
	public void setAI(MobAI ai) {
		this.ai = ai;
	}
	
	/**
	 * Overriden to not return race.
	 */
	@Override
	@XmlTransient
	public String getShortDescription() {
		String res = getBaseModel().getName();
		String lastName = getBaseModel().getLastName();
		String title = getBaseModel().getTitle();
		
		//Append these if they exist
		if (lastName != null && lastName.length() > 0) res += " " + lastName;
		if (title != null && title.length() > 0) res += " " + title;
		
		//Finallly add class name.
		//TODO add class name.
		
		return res; 
	}
}
