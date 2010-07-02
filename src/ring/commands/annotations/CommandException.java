package ring.commands.annotations;

public class CommandException extends RuntimeException {
	public CommandException(String msg) {
		super(msg);	
	}
	
	public CommandException(String msg, Throwable cause) {
		super(msg, cause);
	}
}	
