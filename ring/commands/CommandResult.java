package ring.commands;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

public class CommandResult {
  //Instance variables.
  String text;//the text to be returned to the player.
  String failText;//text to be returned on fail.
  boolean successful;//did the command "fail" or "succeed" to be executed? example: "can't wear this item."

  public CommandResult() {
    text = "";
    failText = "[R][GREEN]You can't do that.[R][WHITE]";
    successful = false;
  }

  public void clearText() {
    text = "";
  }

  public void setFailText(String text) {
    failText = text;
  }
  public void addText(String txt) {
    text += txt;
  }

  public void setText(String txt) {
    text = txt;
  }

  public void setSuccessful(boolean b) {
    successful = b;
  }

  public String getText() {
    if (successful) return text + "\n\n";
    else return failText + "\n\n"; //have a double line break after every command input
  }

}
