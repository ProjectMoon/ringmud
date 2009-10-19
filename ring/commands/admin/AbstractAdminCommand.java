package ring.commands.admin;

import ring.commands.Command;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.players.PlayerCharacter;

public abstract class AbstractAdminCommand implements Command {
	public boolean isAccessAllowed(CommandSender sender) {
		if (sender instanceof PlayerCharacter == false) {
			return false;
		}
		else {
			PlayerCharacter player = (PlayerCharacter)sender;
			return (player.getType() >= Mobile.GOD);
		}
	}
}
