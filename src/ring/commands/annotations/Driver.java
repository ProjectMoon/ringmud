package ring.commands.annotations;

import ring.items.Item;
import ring.mobiles.Mobile;
import ring.mobiles.npc.NPC;
import ring.movement.Room;

@Template({
	@Form(bind = { @BindType({String.class}) }),
	@Form(id = "steal", clause = "$mobile", bind = { @BindType({Mobile.class}), @BindType({Mobile.class}) }),
})
public class Driver {
	public static void main(String[] args) {
		Room r1 = new Room();
		r1.getModel().setTitle("A room");
		Mobile mob = new NPC();
		mob.getBaseModel().setName("mobile");
		
		r1.addMobile(mob);
		mob.setLocation(r1);
		
		Item i = new Item();
		i.setName("sword");
		
		mob.getDynamicModel().getInventory().addItem(i);
		
		String command = "look mob";
		/*for (String arg : args) {
			command += arg + " ";
		}*/
		command = command.trim();
		Template t = Driver.class.getAnnotation(Template.class);
		
		CommandParser parser = new CommandParser(t);
		ParsedCommand cmd = parser.parse(generateSender(mob), command);
		System.out.println("cmd: " + cmd);
		for (Object arg : cmd.getArguments()) {
			System.out.println("arg: " + arg);
		}
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
