package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;

public class Alignment implements Serializable {
    public static final long serialVersionUID = 1;
  //This class is used for the alignment of mobiles. It condenses the boolean array into an
  //easy-to-use class. The class provides useful methods related to alignment.

  //Instance variables.
  //alignment and type ints. These ints store the type of alignment.
  private int type;
  private int alignment;

  //Constants.
  //These constants are used for accessing alignments.
  public static final int LAWFUL = 0;
  public static final int NEUTRAL = 1;
  public static final int CHAOTIC = 2;
  public static final int GOOD = 0;
  public static final int EVIL = 2;
 
  //This constructor constructs a default alignment of true neutral.
  public Alignment() {
    //Set type.
    type = NEUTRAL;
    //Set alignment.
    type = NEUTRAL;
  }

  //This constructor constructs an alignment of the specified type.
  public Alignment(int type, int alignment) {
    if ((type < LAWFUL) && (type > CHAOTIC)) {
      type = NEUTRAL;
    }

    if ((alignment < GOOD) && (alignment > EVIL)) {
      alignment = NEUTRAL;
    }

    this.type = type;
    this.alignment = alignment;
  }

  //###################
  //METHODS

  //Get and Set methods.
  //These methods get alignment and set alignment.

  //getAlignmentString method.
  //This method returns the alignment as a string viewable to the player. This is generally used in
  //the score command.
  public String getAlignmentString() {
    String type = convertType(getType());
    String alignment = convertAlignment(getAlignment());
    String res = type + alignment;

    if (res.equals("[R][CYAN]Neutral[WHITE] [R][CYAN]Neutral[WHITE]")) {
      res = "[R][CYAN]True Neutral[WHITE]";
    }

    return res;
  }

  //getType method.
  //This method returns the type of alignment (lawful, neutral, chaotic) as an int.
  public int getType() {
    return type;
  }

  //getAlignment method.
  //This method returns the alignment (good, neutral, evil) as an int.
  public int getAlignment() {
    return alignment;
  }

  //convertType method.
  //This private method converts an int-based type to a String.
  private String convertType(int type) {
    if (type == LAWFUL) return "[B][WHITE]Lawful[R] ";
    if (type == NEUTRAL) return "[R][CYAN]Neutral[WHITE] ";
    if (type == CHAOTIC) return "[B][GREEN]Chaotic[R][WHITE] ";

    return "TYPE ERROR";
  }

  //convertAlignment method.
  //This private method converts an int-based alignment to a String.
  private String convertAlignment(int alignment) {
    if (alignment == GOOD) return "[B][WHITE]Good[R]";
    if (alignment == NEUTRAL) return "[R][CYAN]Neutral[WHITE]";
    if (alignment == EVIL) return "[B][RED]Evil[R][WHITE]";

    return "ALIGNMENT ERROR";
  }

}
