package ring.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;
import ring.movement.Room;

public class Zone {
  private int index;
  private Vector entranceList;
  private String name;

  public Zone() {
    entranceList = new Vector();
    name = "Test Zone";
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public Vector getEntrances() {
    return entranceList;
  }

  public void addEntrance(Room room) {
    entranceList.addElement(room);
  }

}