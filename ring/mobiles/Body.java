package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.util.*;
import java.io.Serializable;
import ring.entities.*;

public class Body implements Serializable {
  //This class defines a body, equipment slots, and the racial abilities it has. Numerous
  //different body parts can be created and can be all added. This means that a mobile
  //can have two left arms and two right arms, one leg, etc.
  //
  //There are different useful methods in the class such as:
  //generateSlotList()
  //getBodyPartDescription(int partIndex)
  //checkBodyPart(int partIndex)

  //Instance variables.

  //List of Body Parts.
  //When any BodyPart is added to the body, it creates a cloned version of the body part
  //to make sure the static instances do not change. The BodyPart method implements a
  //equals() method to check if the BodyPart is equal to another on the actual Body. This
  //allows for multiple body parts of the same type. Example: 8 fingers on a human. When
  //using a method to return a BodyPart, the first one that is empty is returned if there are
  //multiple copies of the BodyPart. Otherwise, the first occurance is returned.
  public static BodyPart LEFT_ARM = new BodyPart("left arm");
  public static BodyPart RIGHT_ARM = new BodyPart("right arm");
  public static BodyPart LEFT_HAND = new BodyPart("left hand");
  public static BodyPart RIGHT_HAND = new BodyPart("right hand");
  public static BodyPart LEFT_LEG = new BodyPart("left leg");
  public static BodyPart RIGHT_LEG = new BodyPart("right leg");
  public static BodyPart LEFT_WRIST = new BodyPart("left wrist");
  public static BodyPart RIGHT_WRIST = new BodyPart("right wrist");
  public static BodyPart WINGS = new BodyPart("wings");
  public static BodyPart NORMAL_TAIL = new BodyPart("tail");
  public static BodyPart LEGS_TAIL = new BodyPart("tail");
  public static BodyPart TENTACLE = new BodyPart("tentacle");//illithid tentacle.
  public static BodyPart THUMB = new BodyPart("thumb");
  public static BodyPart OPPOSABLE_CLAW = new BodyPart("thumb claw");
  public static BodyPart HEAD = new BodyPart("head");
  public static BodyPart WAIST = new BodyPart("waist");
  public static BodyPart NECK = new BodyPart("neck");
  public static BodyPart LEFT_FOOT = new BodyPart("left foot");
  public static BodyPart RIGHT_FOOT = new BodyPart("right foot");
  public static BodyPart LEFT_FINGER = new BodyPart("left finger");
  public static BodyPart RIGHT_FINGER = new BodyPart("right finger");
  public static BodyPart LEFT_CLAW = new BodyPart("left claw");
  public static BodyPart RIGHT_CLAW = new BodyPart("right claw");
  public static BodyPart SMALL_BODY = new BodyPart("body");//body for stuff like rabbits.
  public static BodyPart MEDIUM_BODY = new BodyPart("body");//normal body.
  public static BodyPart LARGE_BODY = new BodyPart("body");//body for stuff like ogres.
  public static BodyPart HUGE_BODY = new BodyPart("body");//body for stuff like young dragons.
  public static BodyPart COLOSSAL_BODY = new BodyPart("body");//body for stuf like dragons.
  public static BodyPart FACE = new BodyPart("face");
  public static BodyPart SHOULDER = new BodyPart("shoulder");

  //Other variables.
  private Vector bodyParts;

  //Creates a new body object.
  public Body() {

  }

  //addPart method.
  //This adds a body part to the body. It is possible to add multiple body parts to
  //the body. BE AWARE that if multiple copies of the same body part are added,
  //this affects the way certain methods work.
  public void addPart(BodyPart part) {
    if (bodyParts == null) bodyParts = new Vector();

    bodyParts.addElement(part);
  }

  //removePart method.
  //This will remove the FIRST EMPTY body part of the specified index. Any body
  //part of the specified index that has an item is spared removal. Call
  //removePart(int index, boolean removeFirst) with a TRUE parameter if you want to
  //remove the first occurance of the body part.
  public void removePart(BodyPart typeOfPart) {
    boolean found = false;
    int c = 0;
    BodyPart part;

    while (!found) {
      part = (BodyPart)bodyParts.get(c);
      if ((part.equals(typeOfPart)) && (!part.hasItem())) {
        bodyParts.removeElementAt(c);
        found = true;
      }
      c++;
    }
  }

  //removePart method.
  //This part will remove the first occurance of the BodyPart of the specified
  //index it finds.
  public void removePart(BodyPart typeOfPart, boolean removeFirst) {
    if (removeFirst == false) {
      removePart(typeOfPart);
    }

    else {
      boolean found = false;
      int c = 0;
      BodyPart part;

      while (!found) {
        part = (BodyPart)bodyParts.get(c);
        if (part.equals(typeOfPart)) {
          bodyParts.removeElementAt(c);
          found = true;
        }
        c++;
      }
    }//End else statement.
  }

  //getPart method.
  //This method will get the first empty occurance of the type of body part specified
  //by the index.
  public BodyPart getFirstEmptyPart(BodyPart typeOfPart) {
    boolean found = false;
    int c = 0;

    while(!found) {
      BodyPart part = (BodyPart)bodyParts.get(c);
      if (typeOfPart.equals(part) && (!part.hasItem())) {
        found = true;
        return part;
      }
      c++;
    }

    System.out.println("Found nothing!");
    return null;
  }

  //getPart method.
  //This version of the above method will return the first occurance of the body part,
  //regardless of whether it has an item in it or not.
  public BodyPart getFirstPart(BodyPart typeOfPart, boolean returnFirst) {
    if (returnFirst == false) {
      return getFirstEmptyPart(typeOfPart);
    }

    else {
      BodyPart part;
      boolean found = false;
      int c = 0;

      while(!found) {
        part = (BodyPart)bodyParts.get(c);
        if (typeOfPart.equalsIgnoreItem(part)) {
          found = true;
          return part;
        }
        c++;
      }
    }//End else statement.

    return null;//Didn't find anything!
  }

  //getEligibleBodyParts method.
  //This method returns an array of BodyParts that the specified item can be used on.
  public BodyPart[] getEligibleBodyParts(Item item) {
    Vector returnList = new Vector();
    BodyPart part = item.partWornOn();

    //loop through all of the BodyParts on the body.
    for (int c = 0; c < bodyParts.size(); c++) {
      BodyPart otherPart = (BodyPart)bodyParts.get(c);
      if (part.equalsIgnoreItem(otherPart) && (!(otherPart.hasItem()))) returnList.addElement(otherPart);
    }

    //Was anything found?
    if (returnList.size() == 0) return null;

    //Yep!
    //Change the vector to an array.
    BodyPart[] returnParts = new BodyPart[returnList.size()];
    for (int c = 0; c < returnList.size(); c++) returnParts[c] = (BodyPart)returnList.get(c);


    return returnParts;
  }

  public Vector getBodyParts() {
    return bodyParts;
  }

  //########################################
  //END UTILITY METHODS
  //########################################
  //BEGIN STOCK BODY METHODS

  //These methods create stock bodies. There are bodies for all of the races, as well as
  //generic/specific NPC body creation methods. These methods should be called by the race
  //class to generate a Body object for the race.

  //createMediumHumanoidBody method.
  //This method will create a body used by basically all of the PC races in the game except
  //for ogres, gnomes, and illithids. It also creates a lot of the NPC races/mobile races.
  public static Body createMediumHumanoidBody() {
    Body body = new Body();

    body.addPart(MEDIUM_BODY);
    body.addPart(LEFT_ARM);
    body.addPart(RIGHT_ARM);
    body.addPart(LEFT_FINGER);
    body.addPart(RIGHT_FINGER);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_HAND);
    body.addPart(RIGHT_HAND);
    body.addPart(LEFT_WRIST);
    body.addPart(RIGHT_WRIST);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }

  //createSmallHumanoidBody method.
  //This makes a small humanoid. It is used for Gnomes for the most part.
  public static Body createSmallHumanoidBody() {
    Body body = new Body();

    body.addPart(SMALL_BODY);
    body.addPart(LEFT_ARM);
    body.addPart(RIGHT_ARM);
    body.addPart(LEFT_FINGER);
    body.addPart(RIGHT_FINGER);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_HAND);
    body.addPart(RIGHT_HAND);
    body.addPart(LEFT_WRIST);
    body.addPart(RIGHT_WRIST);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }


  //createIllithidBody method.
  //This method creates the body of an illithid. The illithid only has one difference--
  //4 tentacles. These tentacles give it the ability to brain drain and also wear tentacle
  //weapons.
  public static Body createIllithidBody() {
    Body body = new Body();

    body.addPart(MEDIUM_BODY);
    body.addPart(LEFT_ARM);
    body.addPart(RIGHT_ARM);
    body.addPart(LEFT_FINGER);
    body.addPart(RIGHT_FINGER);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_HAND);
    body.addPart(RIGHT_HAND);
    body.addPart(LEFT_WRIST);
    body.addPart(RIGHT_WRIST);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);
    body.addPart(TENTACLE);
    body.addPart(TENTACLE);
    body.addPart(TENTACLE);
    body.addPart(TENTACLE);

    return body;
  }


  //createLargeHumanoid method.
  //This method creates a large humanoid body. This is used for ogres, giants, and the like.
  public static Body createLargeHumanoidBody() {
    Body body = new Body();

    body.addPart(LARGE_BODY);
    body.addPart(LEFT_ARM);
    body.addPart(RIGHT_ARM);
    body.addPart(LEFT_FINGER);
    body.addPart(RIGHT_FINGER);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_HAND);
    body.addPart(RIGHT_HAND);
    body.addPart(LEFT_WRIST);
    body.addPart(RIGHT_WRIST);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }


  //createSmallQuadrapedBody method.
  //This method creates a small quadraped such as a rabbit or fox. This is used for most
  //animal monsters in the game. However, there are some bodies that can create bigger
  //versions of quadrapeds.
  public static Body createSmallQuadrapedBody() {
    Body body = new Body();

    body.addPart(SMALL_BODY);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(WAIST);

    return body;
  }

  //createMediumQuadrapedBody method.
  //This method creates a medium-sized quadraped body.
  public static Body createMediumQuadrapedBody() {
    Body body = new Body();

    body.addPart(MEDIUM_BODY);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }

  //createLargeQuadrapedBody method.
  //This method creates a large quadraped body. Useful for mobiles like young,
  //wingless... dragons?
  public static Body createLargeQuadrapedBody() {
    Body body = new Body();

    body.addPart(LARGE_BODY);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }

  //createHugeQuadrapedBody method.
  //Creates a huge quadraped body. Useful for something like the Tarrasque.
  public static Body createHugeQuadrapedBody() {
    Body body = new Body();

    body.addPart(HUGE_BODY);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);

    return body;
  }

  //createDragonBody method.
  //This method creates a dragon body. Essentially the above method, but with wings.
  public static Body createDragonBody() {
    Body body = new Body();

    body.addPart(SMALL_BODY);
    body.addPart(HEAD);
    body.addPart(FACE);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(LEFT_LEG);
    body.addPart(RIGHT_LEG);
    body.addPart(LEFT_FOOT);
    body.addPart(RIGHT_FOOT);
    body.addPart(SHOULDER);
    body.addPart(SHOULDER);
    body.addPart(WAIST);
    body.addPart(WINGS);

    return body;
  }



}
