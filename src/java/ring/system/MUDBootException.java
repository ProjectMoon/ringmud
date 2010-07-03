package ring.system;

/**
 * An exception that tells the user that something went wrong
 * with the most core part of the MUD during start up. The MUD
 * should not continue booting up if this exception is thrown.
 * @author projectmoon
 *
 */
public class MUDBootException extends RuntimeException {
	public static final long serialVersionUID = 1;
	
	public MUDBootException() {
		super();
	}
	
	public MUDBootException(String msg) {
		super(msg);
	}
}
