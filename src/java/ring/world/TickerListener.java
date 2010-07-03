package ring.world;

import java.util.EventListener;

/**
 * Interface that allows objects to listen to ticker events.
 * 
 * @author projectmoon
 * 
 */
public interface TickerListener extends EventListener {
	public void processTick(TickerEvent event);
}
