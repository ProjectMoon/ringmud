package ring.server.callbacks;

/**
 * Interface for connect/disconnect callbacks.
 */
public interface Callback {
	public void execute(CallbackEvent event);
}
