package ring.installer;

@SuppressWarnings("serial")
public class InstallationException extends Exception {
	public InstallationException(String msg) {
		super(msg);
	}
	
	public InstallationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
