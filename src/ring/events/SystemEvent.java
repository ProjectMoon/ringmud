package ring.events;

public enum SystemEvent {
	ON_LOAD("onLoad");
	
	private String eventName;
	private SystemEvent(String name) {
		eventName = name;
	}
	
	public String getEventName() {
		return eventName;
	}
}
