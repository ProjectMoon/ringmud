package ring.players;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import ring.mobiles.*;
import ring.world.*;
import ring.util.*;
import ring.entities.*;
import ring.movement.*;
import ring.commands.*;
import ring.effects.*;

import java.util.*;
import java.net.*;
import java.io.*;

public class PlayerCharacter extends Mobile implements Runnable, CommandSender, TickerListener {
  //Player Constants.
  //Player-Server connection.
  // the player to server connection
  private transient Socket connection;
  private transient BufferedInputStream input;
  private transient BufferedOutputStream output;
  private transient Thread thread;

  //Player instance variables.
  private String password;
  private Date lastLogon;
  private transient boolean TTPActive;
  private transient long TTPTotal;
  private transient int TTPCount;
  private transient Date TTPStart;
  private transient Date TTPEnd;
  private transient Date sessionStartTime;
  private transient String lastCommand = null;

  //Other variables.
  private boolean communicationError;
  private boolean quitting;

  public PlayerCharacter() {}
  public PlayerCharacter(Socket socket, BufferedInputStream input, BufferedOutputStream output, String pName) {
    connection = socket;
    this.input = input;
    this.output = output;
    password = "";
    lastLogon = new Date();
    super.setName(pName);
    //World.getWorld().getTicker().addTickerListener(this, "PULSE");
  }

  public void setPassword(String pass) {
    password = pass;
  }

  public boolean checkAlias(String name) {
    return false;
  }

  //setLogonDate method.
  //This method creates a new Date object and sets lastLogon to it. This should be
  //called every time the player logs in.
  public void setLogonDate() {
    lastLogon = new Date();
  }

  //savePlayer method.
  //This method saves the player's current status into the player file. The file is
  //overwritten every time so there is no need to do appending and other complicated
  //things. The file is opened with the readPlayer method.
  public void savePlayer() {

  }

  //readPlayer method.
  //This will load a player from a player file.
  public void readPlayer() {

  }

  //processTick method.
  public synchronized void processTick(TickerEvent e) {
    //Loop through the effects...!
    //System.out.println("[" + this + "] processing effects: size: " + super.effectsList.size());
    super.processTick(e);
    /*
    for (int c = 0;  c < super.effectsList.size(); c++) {
      Effect effect = (Effect)super.effectsList.get(c);
      if (effect.isDead()) { System.out.println("***Removing Effect: " + effect); super.effectsList.removeElement(effect); }
      else effect.decrementTimer();
    }

    //Deal with locking.
    if (this.isLocked()) this.decrementLockTime();
    if (this.getLockTimeRemaining() <= 0) { this.setLocked(false); }
*/
  }

  //run method.
  //The run method starts the up the character and then goes into a loop, waiting on
  //commands.
  public void run() {
    System.out.println("Starting a player thread...");
    //Start up character.
    System.out.println("Creating player in the world: " + getName());
    World.getWorld().getTicker().addTickerListener(this, "PULSE");
    //Set location.
    Room room = ZoneCoordinate.ORIGIN.getRoom();
    room.addMobile(this);
    setLocation(new ZoneCoordinate(0, 0, 0, 0));
    doCommand("look");

    //Wait for commands.
    while(!isDead) {
      Thread.currentThread().yield();
      sendPrompt();
      doCommand(receiveData());
    }
  }

  public boolean isCommunicationError() {
    return communicationError;
  }

  //getPrompt method.
  //This returns the prompt that the player has set.
  public String getPrompt() {
    return "[B][GREEN]HP: " + super.getCurrentHPString() + "/" + super.getMaxHPString() + " MV: " + super.getCurrentMV() + "/" + super.getMaxMV() + " ]> [R][WHITE]";
  }

  //setCommunication error method.
  //This sets communicationError to true.
  public void setCommunicationError() {
    communicationError = true;
  }

  public String parseOutgoingCommands(String text) {
    return "hI";
  }

  //type method.
  //Returns the type that this CommandSender is.
  public Object source() {
    return this;
  }

  //decrementLockTime method.
  //Overriden to display "Your head clears." after time is up.
  public void decrementLockTime() {
    super.decrementLockTime();
    if (super.lockTimeRemaining <= 0) sendData2("\n" + super.lockFinishedMessage + "\n\n" + getPrompt());
  }

  //doCommand method.
  //This is the main way to send commands to the world so it parses, handles them, and
  //returns a result.
  public void doCommand(String command) {
    //Was anything even typed?
    if (command.length() <= 0) return;

    //Is the player locked?
    if (this.isLocked) { sendData2(super.lockMessage + " (" + super.lockTimeRemaining * 2 + " seconds left)\n\n"); return; }

    System.out.println("Sending: " + command);

    //Send the command.
    CommandResult res = super.handler.sendCommand(command);
    String result = res.getText();
    sendData2(result);
  }

  //setThread method.
  //This sets the player's thread.
  public void setThread(Thread thread) {
    this.thread = thread;
  }

  //getShortDescription method.
  //This method returns a description of the player that others see when they send
  //a look command.
  public String getShortDescription() {
    return "[WHITE]" + this.getName() + " (" + this.getRaceString() + ")";
  }

  public String toString() {
    return super.getName();
  }



































  public String receiveData() {
    StringBuffer incommingData;
    String incommingData2;
    int inputData;
    int idleCount = 0;
    int timeOutCount = 0;
    int timeOutLimit = World.TIMEOUT_LIMIT * 24;
    byte[] bytes;

    incommingData = new StringBuffer();

    try {
      while (!isCommunicationError() && incommingData.length() == 0 && !isDead && !quitting) {

        // read data from the player until a null character is received
        if (input.available() != 0) {
          inputData = input.read();
          while (inputData != '\n' && inputData != -1 && !isCommunicationError()) {
            if (inputData != '\r') incommingData.append((char) inputData);
            inputData = input.read();
          }

          // if the user is just hitting enter then don't exit and go through
          // command processing just redisplay the prompt - updated if needed
          if (incommingData.length() == 0 && inputData == '\n') {
            sendData2(getPrompt());
          }

          // we should only reach the end of a stream when the socket
          // is closed, therefore error on it.
          if (inputData == -1) throw new IOException();
          //log.message(" Received " + incommingData.length() + " bytes from " + getMobileName());
        } else {
          try {
            Thread.sleep(50);
            if (idleCount++ > 50) {
              sendData("");
              idleCount = 0;
              if (getType() != FORGER && timeOutCount++ > timeOutLimit) {
                sendData("[RED]Connection Idle for " + World.TIMEOUT_LIMIT + " minutes, Logged out by Server.\n[YELLOW]Bye Bye[WHITE]");
                System.out.println("Idle Connection terminated for: " + getName());
                setCommunicationError();
              }
            }
          } catch (InterruptedException ie) {
          }
        }
      }
    } catch (IOException ioe) {
      System.out.println("Comms error receiving data (player) for: " + getName());
      System.out.println(ioe);
      setCommunicationError();
    }

    if (TTPActive) TTPStart = new Date();
    incommingData2 = parseIncomingData(incommingData.toString());

    /*
                 bytes = incommingData2.getBytes();
                 for (int x=0;x<incommingData2.length();x++) {
                 System.out.println("RECEIVE: " + incommingData2.charAt(x) + "\t" + bytes[x] + "\t" + (int) incommingData2.charAt(x));
                 }
     */

    return incommingData2;
  }

  public void sendPrompt() {
	  sendData2(getPrompt());
  }
  public void sendData(String data) {
    if (data.length() != 0) data = "\n" + data + "\n\n" + getPrompt();    
    sendData2(data);
  }

  public void sendData2(String data) {
    if (data.length() != 0) {
     data = TextParser.parseOutgoingData(data);
    }

    try {
      output.write(data.getBytes(), 0, data.getBytes().length);
      output.write(0);
      output.flush();
    } catch (IOException ioe) {
      System.out.println("Comms error sending data (player) for: " + getName());
      System.out.println(ioe);
      setCommunicationError();
    }
  }

  public String parseIncomingData(String data) {

                char dataByte;

                StringBuffer output = new StringBuffer();

                // loop through each data character checking for TELNET protocol commands
                for (int x = 0; x < data.length(); x++) {
                        dataByte = data.charAt(x);
                        // if hex 255 incoming IAC
                        if (dataByte == '\377') {
                                dataByte = data.charAt(++x);

                                // if command is WILL echo DON'T
                                if (dataByte == '\373') {
                                        dataByte = data.charAt(++x);
                                        sendData("\377\376" + dataByte);
                                        continue;
                                }

                                // if command is DO echo WON'T
                                if (dataByte == '\375') {
                                        dataByte = data.charAt(++x);
                                        sendData("\377\374" + dataByte);
                                        continue;
                                }
                        } else {
                                output.append(dataByte);
                        }
                }

                // request to repeat last command typed = !!
                if (output.toString().equals("!!") && lastCommand != null) {
                        return lastCommand;
                } else {
                        lastCommand = output.toString();
                }

                return output.toString();
        }




}
