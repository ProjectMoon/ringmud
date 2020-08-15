package ring.commands.parser;

import ring.movement.Room;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

public interface CommandSender {
	public static class CommandSenderContext {
		private Room location;
		
		public Room getLocation() {
			return location;
		}
		
		public void setLocation(Room location) {
			this.location = location;
		}
	};
	
	public void doCommand(String cmd);
	public CommandSenderContext getContext();
}
