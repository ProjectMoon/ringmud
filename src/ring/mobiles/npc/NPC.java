package ring.mobiles.npc;

import ring.commands.CommandResult;
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

	public CommandResult doCommand(String command) {
		// Was anything even "typed?"
		if (command.length() <= 0)
			return null;

		// Send the command.
		return super.handler.sendCommand(command);
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
}
