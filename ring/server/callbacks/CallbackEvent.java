package ring.server.callbacks;

public enum CallbackEvent {
	CONNECTED,
	DISCONNECTED,
	GRACEFUL_QUIT,
	UNEXPECTED_QUIT
}
