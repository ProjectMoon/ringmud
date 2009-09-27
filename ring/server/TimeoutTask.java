package ring.server;

import java.util.TimerTask;

public class TimeoutTask extends TimerTask {
	public static final long MILLISECOND = 1;
	public static final long SECOND = 1000;
	public static final long MINUTE = 10000;
	public static final long HOUR = 100000;
	
	public static final long TIMEOUT_LIMIT = 5;
	protected long timeoutCount = 0;
	
	@Override
	public void run() {
		timeoutCount++;
	}
	
	public boolean isTimedOut() {
		return (timeoutCount > TIMEOUT_LIMIT);
	}

}
