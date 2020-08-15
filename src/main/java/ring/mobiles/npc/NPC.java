package ring.mobiles.npc;

import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.world.TickerEvent;
import ring.world.TickerListener;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ai == null) ? 0 : ai.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NPC other = (NPC) obj;
		if (ai == null) {
			if (other.ai != null)
				return false;
		} else if (!ai.equals(other.ai))
			return false;
		return true;
	}
}
