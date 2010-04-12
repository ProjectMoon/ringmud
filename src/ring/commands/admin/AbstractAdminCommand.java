package ring.commands.admin;

import ring.commands.CommandSender;
import ring.players.PlayerCharacter;

public abstract class AbstractAdminCommand {
	public boolean isAccessAllowed(CommandSender sender) {
		if (sender instanceof PlayerCharacter == false) {
			return false;
		}
		else {
			PlayerCharacter player = (PlayerCharacter)sender;
			return (player.getBaseModel().getType().isGod());
		}
	}
}
