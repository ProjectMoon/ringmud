package ring.world;

/**
 * Interface that allows objects to listen to ticker events.
 * @author projectmoon
 *
 */
public interface TickerListener {
	public void processTick(TickerEvent event);
}
