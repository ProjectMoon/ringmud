package ring.nrapi.movement;

/**
 * An exception that denotes there is something strange about a given move operation.
 * Usually this amounts to a Movable attempting to move from a Location that does not
 * contain the Portal it wishes to move into.
 * @author projectmoon
 *
 */
public class MovementAssertionException extends RuntimeException {
	public static final long serialVersionUID = 1;
	public MovementAssertionException() {
		super();
	}
	
	public MovementAssertionException(String msg) {
		super(msg);
	}
}
