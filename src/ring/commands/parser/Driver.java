package ring.commands.parser;

import ring.commands.annotations.BindType;
import ring.commands.annotations.Form;
import ring.commands.annotations.Template;
import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.npc.NPC;
import ring.movement.Room;

@Template({
	@Form(id = "steal1", clause = ":item from $mobile", bind = { @BindType({Item.class}), @BindType({Mobile.class}) }),
	@Form(id = "steal2", clause = "from $mobile the :item", bind = { @BindType({Mobile.class}), @BindType({Item.class}) }),
	@Form(id = "steal2", clause="the $mobile its #stuff and :item", bind = { @BindType({ Mobile.class }), @BindType(), @BindType({ Item.class }) }),
	@Form(id = "stealhelp", clause = "")
})
public class Driver implements Command {
	@Override
	public void execute(CommandSender sender, ParsedCommand params) {
		System.out.println("Steal command form: " + params.getFormID());
		System.out.println("Args are:");
		for (Object arg : params.getArguments()) {
			System.out.println("    " + arg);
		}
	}

	@Override
	public String getCommandName() {
		return "steal";
	}

	@Override
	public void rollback() {
		
	}
	
	public static void main(String[] args) {
		//Generate test world.
		Room r1 = new Room();
		r1.getModel().setTitle("A room");
		
		Mobile mob = new NPC();
		mob.getBaseModel().setName("mobile");
		
		Item i = new Item();
		i.setName("shiny sword");
		
		mob.getDynamicModel().getInventory().addItem(i);
		r1.addMobile(mob);
		mob.setLocation(r1);
		
		//Set up command and parser.
		try {
			Driver stealCommand = new Driver();
			CommandParser parser = new CommandParser(stealCommand);
			String command = "steal";
			ParsedCommand cmd = parser.parse(generateSender(mob), command);
			stealCommand.execute(generateSender(mob), cmd);
		}
		catch (CommandParsingException e) {
			e.printStackTrace();
		}
		/*
		String command = "steal sword from mob";
		ParsedCommand cmd = parser.parse(generateSender(mob), command);
		stealCommand.execute(generateSender(mob), cmd);
		*/

	}
	
	public static CommandSender generateSender(final Mobile mob) {
		CommandSender sender = new CommandSender() {
			@Override
			public void doCommand(String cmd) {
					
			}

			@Override
			public CommandSenderContext getContext() {
				CommandSenderContext ctx = new CommandSenderContext();
				ctx.setLocation(mob.getLocation());
				return ctx;
			}
		};
		
		return sender;
	}
}
