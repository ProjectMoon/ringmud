package ring.server.callbacks;

public enum CallbackEvent {
	CONNECTED,
	DISCONNECTED,
	GRACEFUL_QUIT,
	UNEXPECTED_QUIT;
	
	public boolean wasDisconnected() {
		if (this.equals(CONNECTED) == false) {
			return true;
		}
		else {
			return false;
		}
	}
}
