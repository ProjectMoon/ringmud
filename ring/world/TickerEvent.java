package ring.world;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;

public class TickerEvent extends EventObject{
  private int currentTick;
  private String tickID;

  public TickerEvent(Object source, int currentTick, String tickID) {
    super(source);
    this.currentTick = currentTick;
    this.tickID = tickID;
  }

  public int getCurrentTick() {
    return currentTick;
  }

  public String getTickID() {
    return tickID;
  }
}