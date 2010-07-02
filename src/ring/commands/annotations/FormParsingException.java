package ring.commands.annotations;

public class FormParsingException extends Exception {
	public FormParsingException(String msg) {
		super(msg);
	}
	
	public FormParsingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
