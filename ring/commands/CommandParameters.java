package ring.commands;

import ring.world.WorldObject;
import java.util.Vector;
import ring.mobiles.Mobile;
import ring.entities.Entity;
import ring.mobiles.Body;
import ring.mobiles.BodyPart;
import ring.movement.Room;
import ring.movement.ZoneCoordinate;

/**
 * <p>Title: RingMUD Codebase</p>
 *
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar
 * to DikuMUD</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: RaiSoft/Thermetics</p>
 *
 * @author Jeff Hair
 * @version 1.0
 */
public final class CommandParameters {
  private Object[] parameters;
  private String[] initParameters;
  private CommandSender sender;
  private String cmdType;

  public CommandParameters(String[] params, CommandSender sender) {
    this.sender = sender;
    this.cmdType = cmdType;
    initParameters = params;
  }

  public void init(String cmdType) {
    this.cmdType = cmdType;
    parameters = getParameters(initParameters, cmdType);
  }

  //length method.
  //Using initParameters guarantees that this will return, well.. usually.
  public int length() {
    return initParameters.length;
  }

  //getParameter method.
  //Returns a parameter.
  public Object getParameter(int index) {
    System.out.println("params in CP: " + parameters);
    if (parameters == null)return null;
    if (index >= parameters.length) return null;
    return parameters[index];
  }
  
  public String getParameterAsText(int index) {
      if (parameters == null) return null;
      else return initParameters[index];
  }

  //lastParameter method.
  //Returns the last parameter in the parameters array.
  public Object lastParameter() {
    return parameters[parameters.length - 1];
  }

  //constructSpellName method.
  //This simply returns up to parameters.length - 1 in a combined string.
  public String constructSpellName() throws ClassCastException {
    String text = "";
    for (int c = 0; c < parameters.length - 1; c++) {
      System.out.println("examining: " + parameters[c]);
      if (! (parameters[c] instanceof String))throw (new ClassCastException(
          "Object must be string!"));
      else text += (String) parameters[c] + " ";
    }

    return text.trim();
  }

  public String paramString() {
    if (parameters == null) return null;
    String res = "";
    for (int c = 0; c < parameters.length; c++) {
      if (parameters[c] == null) res += "[null]";
      else res += parameters[c].toString();
      res += " ";
    }

    res = res.trim();
    return res;
  }

  //getParameters method.
  //This method gets all of the parameters needed for the method to be executed properly.
  //All nulls and such are handled properly as well.
  private Object[] getParameters(String[] params, String cmdType) {
    System.out.println("in getParameters");
    WorldObject o;

    //Handle a command that has no parameters.
    System.out.println("checking nullity");
    if ( (params == null) || (params.length == 0) || (params[0] == null))return null;

    System.out.println("passed nullity. param length: " + params.length);
    for (int c = 0; c < params.length; c++) {
      System.out.println("param: " + params[c]);
    }

    //Do things according to the type of command.
    //is is a "say"-type command?
    if (cmdType.equals(Command.TXT))return params;

    //well it must require some sort of object, so let's declare an Object[].
    Object[] parameters = new Object[params.length];

    //Is it a command that requires the last object to be a WorldObject target? i.e. a cast command
    if (cmdType.equals(Command.SPL)) {
      for (int c = 0; c < params.length - 1; c++) {
        parameters[c] = params[c];
      }

      System.out.println("getting a name");
      String name = params[params.length - 1].toString();
      System.out.println("got it");
      parameters[params.length - 1] = getWorldObjectByName(name);
      System.out.println("got world object");
    }//END OF SPELL COMMANDS

    //Is it a command that looks for parameters in the inventory?
    if (cmdType.equals(Command.INV)) {
      for (int x = 0; x < params.length; x++) {
        //First, let's check to see if the parameter is something in the room. This is the most common
        //Parameter.
        o = getWorldObjectFromInventoryByName(params[x]);
        if (o != null) {
          parameters[x] = o;
        }

        //it must be something else. For now, pass it along.
        else {
          parameters[x] = params[x];
        }
      }
    } //END OF INVENTORY COMMAND PARAMETERS!!!!

    //Is it a command that looks for parameters in the equipment?
    if (cmdType.equals(Command.EQP)) {
      for (int x = 0; x < params.length; x++) {
        //First, let's check to see if the parameter is something in the room. This is the most common
        //Parameter.
        o = getWorldObjectFromEquipmentByName(params[x]);
        if (o != null) {
          parameters[x] = o;
        }

        //it must be something else. For now, pass it along.
        else {
          parameters[x] = params[x];
        }
      }
    } //END OF EQUIPMENT COMMAND PARAMETERS!!!!!!

    //It must be a command that looks for parameters in a room...
    if (cmdType.equals(Command.CMD)) {
      System.out.println("in CMD if statement.");
      for (int x = 0; x < parameters.length; x++) {
        System.out.println("loop " + x);
        //First, let's check to see if the parameter is something in the room. This is the most common
        //Parameter.
        o = getWorldObjectByName(params[x]);
        if (o != null) {
          parameters[x] = o;
        }

        //it must be something else. For now, pass it along.
        else {
          parameters[x] = params[x];
        }
      }
    } //END OF CMD PARAMETERS

    //Return the parameters....
    return parameters;
  }

  //getWorldObjectFromInventoryByName method.
  //This method returns an Entity in the inventory of the CommandSender via a String name.
  //Will have to be updated later to accomodate things like 1.sword, 2.berry, etc.
  private WorldObject getWorldObjectFromInventoryByName(String name) {
    if (name.equals("a"))return null;
    Mobile mob;
    name = name.toLowerCase();
    try {
      mob = (Mobile) sender;
    }
    catch (NullPointerException e) {
      System.out.println("WARNING: NULL SENDER SOURCE");
      mob = null;
    }

    Vector inventory = mob.getInventory();
    try {
      if (inventory.size() > 0) {
        for (int c = 0; c < inventory.size(); c++) {
          Entity ent = (Entity) inventory.get(c);
          if ( (ent.getName().toLowerCase().indexOf(name) != -1)) {
            return ent;
          }
        }
      }
    }
    //Catch a null pointer... This tells us it isn't anything... just a sort of thing to return.
    catch (NullPointerException e) {
      System.out.println("COULDN\'T FIND AN ITEM");
      return null;
    }

    //No WorldObject found to return; therefore it must be a string that needs to be passed...
    return null;
  }

  //getWorldObjectFromEquipment method.
//This method returns a WorldObject from the Mobile's equipment.
  private WorldObject getWorldObjectFromEquipmentByName(String name) {
    if (name.equals("a"))return null;
    Mobile mob;
    name = name.toLowerCase();
    try {
      mob = (Mobile) sender;
    }
    catch (NullPointerException e) {
      System.out.println("WARNING: NULL SENDER SOURCE");
      mob = null;
    }

    //Need to get the list of BodyParts...
    Body mobBody = mob.getBody();
    Vector bodyParts = mobBody.getBodyParts();

    try {
      if (bodyParts.size() > 0) {
        for (int c = 0; c < bodyParts.size(); c++) {
          BodyPart part = (BodyPart) bodyParts.get(c);
          Entity ent = (Entity) part.getItem();
          if (ent != null) {
            if ( (ent.getName().toLowerCase().indexOf(name) != -1)) {
              return ent;
            }
          }
        }
      }
    }
    //Catch a null pointer... This tells us it isn't anything... just a sort of thing to return.
    catch (NullPointerException e) {
      System.out.println("COULDN\'T FIND AN ITEM FROM EQUIPMENT");
      return null;
    }

    //No WorldObject found to return; therefore it must be a string that needs to be passed...
    return null;
  }

  //getWorldObjectByName method.
  //This method returns a WorldObject in the room of the CommandSender via a String name.
  //Will have to be updated later to accomodate things like "1.monster, 2.sword" etc.
  private WorldObject getWorldObjectByName(String name) {
    if (name.equals("a"))return null;
    WorldObject o;
    name = name.toLowerCase();
    try {
      o = (WorldObject) sender;
    }
    catch (NullPointerException e) {
      System.out.println(e);
      e.printStackTrace();
      //System.out.println("WARNING: NULL SENDER SOURCE");
      o = null;
    }

    ZoneCoordinate location = o.getLocation();
    Room room = location.getRoom();
    //First loop through mobiles to see if the thing we're looking for is a mobile...
    try {
      Vector mobiles = room.getMobiles();
      if (mobiles.size() > 0) {
        for (int c = 0; c < mobiles.size(); c++) {
          Mobile mob = (Mobile) mobiles.get(c);
          if ( (mob.getShortDescription().toLowerCase()).indexOf(name) != -1) {
            return mob;
          }
        }
      }

      //next try the entities...
      Vector entities = room.getEntities();
      if (entities.size() > 0) {
        for (int c = 0; c < entities.size(); c++) {
          Entity ent = (Entity) entities.get(c);
          if ( (ent.getName().toLowerCase().indexOf(name) != -1)) {
            return ent;
          }
        }
      }
    }
    //Catch a null pointer... This tells us it isn't anything... just a sort of thing to return.
    catch (NullPointerException e) {
      System.out.println("COULDN\'T FIND!");
      return null;
    }

    //No WorldObject found to return; therefore it must be a string that needs to be passed...
    return null;
  }

}
