package ring.movement.nm;

/**
 * A class that denoates something has gone wrong with world construction. Usually
 * this is because an auto-linked room could not be made, or some other illegal
 * construction attempt has been made.
 * @author projectmoon
 *
 */
public class WorldConstructionException extends RuntimeException {
	public static final long serialVersionUID = 1;
	
	public WorldConstructionException() {
		super();
	}
	
	public WorldConstructionException(String msg) {
		super(msg);
	}
}
