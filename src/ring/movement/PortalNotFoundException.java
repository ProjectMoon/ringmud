package ring.nrapi.movement;

public class PortalNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PortalNotFoundException() {
		super();
	}
	
	public PortalNotFoundException(String msg) {
		super(msg);
	}
}
