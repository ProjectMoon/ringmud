package ring.aspects;
import ring.commands.*;

public aspect Test {
	pointcut test():
		call(CommandResult Command.execute(CommandSender, CommandParameters));
		
		before(): test() {
			System.out.println("Calling a command.");
		}
}
