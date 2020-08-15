package ring.world;

import java.util.ArrayList;
import java.util.List;

import ring.world.TickerEvent.TickerType;

/**
 * This class governs time in the MUD world. It fires TickerEvents every 2
 * seconds to all registered TickerListeners. The world ticker is retrieved
 * by calling the static method Ticker.getTicker(). It returns a thread-safe
 * instance of the Ticker class for manipulation.
 * 
 * @author projectmoon
 * 
 */
public class Ticker implements Runnable {
	//Constants.
	public static final int MAX_TICK = 43200;
	private static int tickDuration = 2000;
	
	//Instance variables for a ticker.
	private List<TickerListener> listeners = new ArrayList<TickerListener>();
	private int tickCount;

	//The singleton ticker instance
	private static final Ticker ticker = new Ticker();
	
	/**
	 * Creates a new ticker. Private constructor for singleton instance.
	 */
	private Ticker() {
		this.tickCount = 0;
	}
	
	/**
	 * Gets the Ticker singleton instance.
	 * @return
	 */
	public static Ticker getTicker() {
		return ticker;
	}
	
	/**
	 * Gets the current tick. The current tick is between 0 and 43,200. Since
	 * a tick is 2 seconds, there are 43,200 ticks in one 24 hour period. The
	 * MUD server does not keep track of what "MUD day" it is. But it does keep
	 * track of the time of the MUD day.
	 * @return
	 */
	public synchronized int currentTick() {
		return tickCount;
	}

	/**
	 * Register a ticker listener with this ticker.
	 * @param listener
	 */
	public synchronized void addTickerListener(TickerListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a ticker listener from this ticker.
	 * @param listener
	 * @return true if the listener was removed, false if it was not removed or is not in the list.
	 */
	public synchronized boolean removeTickerListener(TickerListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Fires a PULSE event to all registered listeners on the server.
	 */
	public void fireTickesdfrEvent() {
		synchronized (listeners) {
			for (TickerListener listener : listeners) {
				TickerEvent event = new TickerEvent(this, tickCount, TickerType.PULSE);
				listener.processTick(event);
			}
		}
	}

	/**
	 * Runs the ticker thread.
	 */
	public void run() {
		//TODO change condition to "while server is not shut down"
		while (true) {
			try {
				Thread.sleep(tickDuration);
				tickCount++;
				if (tickCount > MAX_TICK) {
					tickCount = 1;
				}
				
				fireTickesdfrEvent();
			}
			catch (InterruptedException ie) {}
		}
	}

	/**
	 * Generates a list of all registered ticker listeners. Useful
	 * for displaying to admins on the server.
	 * @return
	 */
	public synchronized String tickerList() {
		String text = "";
		int c = 0;

		for (TickerListener listener : listeners) {
			text += (c + 1) + ". " + listener.toString()
				+ " [B]" + listener.getClass().getName() + "[R]\n";
		}
		
		text += "\nCURRENT TICK: " + currentTick();

		return text;
	}
}
