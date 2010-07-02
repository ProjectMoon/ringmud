package ring.commands.annotations;

public class CommandParsingException extends Exception {
	public CommandParsingException(String msg) {
		super(msg);
	}
	
	public CommandParsingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
