package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import ring.entities.*;
import ring.damage.*;

import java.util.*;
import java.io.Serializable;

public class BodyPart implements Serializable {
  //This class is used for a body part.

  //Instance variables.
  //All the constant BodyParts are maintained in class Body.
  //The item this body part has on it.
  private Item equipment;

  //Description of the body part.
  private String description;

  public BodyPart(String description) {
    this.description = description;
  }

  //Copy constructor
  private BodyPart(BodyPart other){
    this.description = other.description;
    this.equipment = other.equipment;
  }

  //getItem method.
  //Returns the current item on the body part.
  public Item getItem() {
    return equipment;
  }

  //removeItem method.
  //Removes the current item on the body part and returns it.
  public Item removeItem() {
    Item returnedItem = getItem();
    equipment = null;
    return returnedItem;
  }


  //addItem method.
  //Adds an item to this body part. If there is an item on it, the method will not allow
  //adding of a new item.
  public void addItem(Item item) {
    if (equipment != null) {
      System.out.println("Item already exists there, remove it first!");
      return;
    }

    else {
      equipment = item;
    }
  }

  //hasItem method.
  //Returns true if this body part has an item. Else returns false.
  public boolean hasItem() {
    if (equipment == null) return false;

    else return true;
  }

  //createUniquePart method.
  //This method creates a unique BodyPart from the part passed to it. Good so you don't have
  //every mob with the same BodyPart references :)
  public BodyPart createUniquePart() {
    return new BodyPart(this);
  }

	//a static version for older code pieces
  public static BodyPart createUniquePart(BodyPart other) {
    return new BodyPart(other);
  }

  //getName method.
  //Gets the name of the body part.
  public String getName() {
    return description;
  }

  public boolean equalsIgnoreItem(BodyPart otherPart) {
    if (this.getName().equalsIgnoreCase(otherPart.getName())) return true;
    return false;
  }

}
