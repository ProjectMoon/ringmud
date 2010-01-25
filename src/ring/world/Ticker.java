package ring.world;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Vector;

public class Ticker extends Thread {
  //This class is what governs time in the world. It ticks every 2 seconds and fires events to
  //all of the registered listeners. There is generally only the world ticker.

  //This internal class is used to hold data for the listeners when firing.
  private class ListenerData {
    TickerListener listener = null;
    String tickID = null;

    public ListenerData(TickerListener listener, String tickID) {
      this.listener = listener;
      this.tickID = tickID;
    }
  }

  //Instance variables.
  private Vector listeners;
  private int tickCount;

  //Constants.
  public static final int MAX_TICK = 86400;
  private static int tickDuration = 2000;

  public Ticker() {
    super();
    this.setName("World Ticker");
    this.tickCount = 0;
  }

  //addTickerListener method.
  //This will add a listener to the ticker.
  //Mobiles should add their listener when they are created.
  public void addTickerListener(TickerListener listener, String tickID) {
     if (listeners == null) listeners = new Vector();
     listeners.addElement(new ListenerData(listener, tickID));
  }

  //removeTickerListener method.
  //This method will remove a listener from the ticker.
  //Mobiles should remove themselves when they die.
  public void removeTickerListener(TickerListener listener, String tickID) {
    ListenerData data = null;
    if (listeners == null) return;

    for (int x = 0; x < listeners.size(); x++) {
      data = (ListenerData) listeners.elementAt(x);
      if ( data.listener == listener && data.tickID.equals(tickID)) {
        listeners.removeElementAt(x);
        return;
      }
    }
  }

  //fireTickerEvent method.
  //This is the main method that will fire a ticker event to all the registered listeners.
  //When they receive the event, they will act appropriately.
  public void fireTickerEvent() {
    ListenerData targetData;
    TickerListener target;

    if (listeners == null) return;

    Vector targets = (java.util.Vector) listeners.clone();

    //fire the event to all of the registered listeners on the server.
    for (int x = 0; x < targets.size(); x++) {
      targetData = (ListenerData) targets.elementAt(x);
      target = targetData.listener;
      TickerEvent event = new TickerEvent(this, tickCount, targetData.tickID);
      try {
        target.processTick(event);
      } catch (Exception e) {}
    }
  }

  //run method.
  //This method generates ticks and sends them to all registered listeners. From there,
  //the listeners will do what they are supposed to do.
  public void run() {
    while (true) {
      try {
        this.sleep(tickDuration);
        tickCount++;
        if (tickCount > MAX_TICK) tickCount = 1;
        fireTickerEvent();
      } catch (InterruptedException ie) {
      }
    }
  }

  //tickerList method.
  //This method returns a string list of all currently active ticker listeners and what they're
  //attached to.
  public String tickerList() {
    String text = "";

    for (int c = 0; c < listeners.size(); c++) {
      ListenerData data = (ListenerData)listeners.get(c);
      text += (c + 1) + ". Registered to: " + data.listener.toString() + " [B][" + data.listener.getClass().getName() + "][R]; id: [" + data.tickID + "]\n";
    }

    text += "\nCURRENT TICK: " + tickCount;

    return text;
  }
}
