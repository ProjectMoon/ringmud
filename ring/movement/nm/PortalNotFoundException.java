package ring.movement.nm;

public class PortalNotFoundException extends RuntimeException {
	public PortalNotFoundException() {
		super();
	}
	
	public PortalNotFoundException(String msg) {
		super(msg);
	}
}
