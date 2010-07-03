package ring.comms;

/**
 * Exception denoting a communication problem. Unchecked due to the sheer
 * amount of places it can be encountered and to allow bubbling as this
 * exception should be handled at a high level of the class hierarchy.
 * @author projectmoon
 *
 */
public class CommunicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CommunicationException() {
		super();
	}
	
	public CommunicationException(String msg) {
		super(msg);
	}
	
	public CommunicationException(Throwable cause) {
		super(cause);
	}
}
