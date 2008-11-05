package ring.commands;

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

//This class represents an in-game command. It stores the text string and the type of the command...


public class Command {
  public Command() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //Constants.
  public static String CMD = "CMD_";//comands
  public static String TXT = "TXT_";//say and tell commands and such.
  public static String INV = "INV_";//for things like drop...
  public static String EQP = "EQP_";//for things like remove...
  public static String SPL = "SPL_";//for things like cast...

  //Instance variables.
  String[] cmd;
  String type;

  public Command(String[] cmd, String type) {
    this.cmd = cmd;
    this.type = type;
  }

  public String[] getCommandString() {
    return cmd;
  }

  public String getActualCommand() {
    return cmd[0];
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setActualCommand(String cmd) {
    this.cmd[0] = cmd;
  }

  public String[] getParameters() {
    if (cmd.length == 1) return new String[] { null }; //this command may not have parameters...
    String[] res = new String[cmd.length - 1];

    for (int c = 1; c < cmd.length; c++) {
      res[c - 1] = cmd[c];
    }

    return res;
  }

  public int getParameterLength() {
    return cmd.length - 1;
  }

  //enforceType method.
  //This method makes sure that the command has the right type before its execution...
  //Just cleans up the CommandHandler class a bit. Lord knows it needs it...
  //More if statements ( :( ) will be added over time...
  public void enforceType() {
    if ((cmd[0].equals("say")) || (cmd[0].equals("tell")) || (cmd[0].equals("who"))
        || (cmd[0].equals("prepare")) || (cmd[0].equals("setdesc"))) type = TXT;
    if ((cmd[0].equals("drop")) || (cmd[0].equals("wear"))) type = INV;
    if ((cmd[0].equals("remove"))) type = EQP;
    if ((cmd[0].equals("cast"))) type = SPL;
  }

  private void jbInit() throws Exception {
  }
}
