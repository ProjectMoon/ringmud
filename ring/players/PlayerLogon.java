package ring.players;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import ring.world.*;
import ring.mobiles.*;
import ring.players.*;
import ring.util.*;
import ring.spells.*;
import ring.effects.*;
import ring.effects.library.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class PlayerLogon extends Thread {

  //This socket is the player-server connection.
  private Socket socket;
  //These are the input and output of above connection.
  private BufferedInputStream input;
  private BufferedOutputStream output;

  //Stuff.
  private boolean communicationError;
  private PlayerCharacter player;
  private boolean invalidLogin;
  private boolean waiting = true;
  private static String welcomeText = "\n\n[CYAN]                                   RingMUD Alpha[CYAN]\n                              A [WHITE]Forgotten Realms[CYAN] MUD.\n\n[WHITE]                                     Forgers:\n                                [CYAN] Ao (Code & Areas)\n                                 Rillifane (Code)\n                                 Istishia (Code)\n                                 Erevan (Code)\n                                 Fenmarel (Areas & RP)\n                                 Kelemvor (Admin)[WHITE]\n";
  private static String newsText = "\n[YELLOW]There is currently no news.[WHITE]\n";
  private World world;

  public PlayerLogon(Socket connection, PlayerCharacter player) {
    super();
    this.player = player;
    setConnection(connection);
  }

  private void setConnection(Socket connection) {
    socket = connection;

    try {
      this.input = new BufferedInputStream(connection.getInputStream());
      this.output = new BufferedOutputStream(connection.getOutputStream());
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  public void sendData(String data) throws IOException {

    if (data.length() != 0) {
      data = data + "[GREEN]\n>";
    }

    sendData2(data);
}

  //sendData2 method.
  //This handles the actual sending of the data. sendData checks to see if there is
  //any data to actually send.
  public void sendData2(String data) throws IOException {
    if (data.length() != 0) data = TextParser.parseOutgoingData(data);

    try {
      output.write(data.getBytes(), 0, data.getBytes().length);
      output.write(0);
      output.flush();
    } catch (IOException ioe) {
      System.out.println("Comms error sending data (player) for: " + socket.getInetAddress().toString());
      System.out.println(ioe);
      throw ioe;
    }
  }

  private boolean isCommunicationError() {
    return communicationError;
  }

  public Socket getConnection() {
    return socket;
  }

  //run method.
  //This method is where we wait for the player to log on. The player is first prompted
  //if they want to create a new character. At this point they may type yes or no.
  //If they choose yes, the method createNewCharacter() is called. In this method, all
  //of the information is gathered for a new character. The information is then written
  //to the player file.
  //
  //If they choose no, they go to the login screen.
  public void run() {
    PlayerCharacter enteringPlayer = null;

   // while (enteringPlayer == null)
      try {
        //wait for log on.
        waiting = true;
        sendData(welcomeText + "\n[WHITE]Create new character? (Y/N)");
        while(waiting) {
          player = attemptNewCharacter();
          Thread playerThread = new Thread(World.getWorld().getPlayerThreadGroup(), player,
                                           "Player [" + player.getName() + "] ");
          playerThread.setDaemon(true);
          player.setThread(playerThread);
          playerThread.start();
        }
      } catch(IOException e) {System.out.println("Exception...");}
  }

  public PlayerCharacter attemptNewCharacter() {
    String ans = "";
    try {
      ans = receiveData();
    }
    catch (Exception e) {}

    ans = ans.toUpperCase();

    if (ans.equals("Y")) {
      try {
        sendData("[RED]Entering new character creation mode...[WHITE]\nPlease enter a name for this character:");
        PlayerCharacter c = createNewCharacter();
        System.out.println("Finished new character... trying to start thread...");
        waiting = false;
        return c;

      }
      catch(Exception e) {}
    }

    else if (ans.equals("N")) {
      try {
        getPlayerName();
      }
      catch(Exception e) {}
    }

    else {
      try {
        sendData("[WHITE]Please choose Y or N.");
        attemptNewCharacter();
      }
      catch (Exception e) {}
    }

    return null;
  }

  public PlayerCharacter createNewCharacter() {
    try {
    String name = receiveData();
    PlayerCharacter character = createNewCharacter(name);
    return character;
    }
    catch(Exception e) {}
    return null;
  }










































  //#############################################
  //#############################################
  //BEGIN BIG CLUNKY METHODS
  //These methods are big, hard to read, but very necessary.

  //getPlayerName method.
  //Used for logging in.
  public String getPlayerName() throws IOException {
    String playerName = null;
    boolean playerActive;
    boolean invalidName;
    boolean found;
    String allowableCharacters = new String("abcdefghijklmnopqrstuvwxyz");

    do {
      invalidName = true;
      // ask for player name and log user on
      while (invalidName) {
        sendData("[RED]Skipping new character creation...[WHITE]\nPlease enter your character's name:");
        playerName = receiveData();
        invalidName = false;
        if (playerName.length() > 15 || playerName.length() < 1) {
          invalidName = true;
          sendData2("[RED]Sorry, character names must be between 1 and 15 characters long.[WHITE]\n");
        } else {
          for (int x = 0; x < playerName.length(); x++) {
            found = false;
            for (int y = 0; y < allowableCharacters.length(); y++) {
              if (playerName.substring(x, x + 1).equalsIgnoreCase(allowableCharacters.substring(y, y + 1))) {
                found = true;
                break;
              }
            }
            if (!found) {
              sendData2("[RED]Sorry, only the uppercase and lower case letters A-Z are allowed in a character name.[WHITE]\n");
              invalidName = true;
              break;
            }
          }
        }
      }

      System.out.println("Player [" + getConnection().getInetAddress() + "] logged on as " + playerName);

      // check player not already active
      playerActive = false;
      Vector currentPlayers = world.getPlayers();

      for (int x = 0; x < currentPlayers.size(); x++) {
        if (((PlayerCharacter) currentPlayers.elementAt(x)).checkAlias(playerName.toUpperCase())) {
          playerActive = true;
          break;
        }
      }
      if (playerActive) {
        sendData2("[RED]Sorry, that character is already in use.\n[CYAN]Note: If the character was in use by you and you dropped out of the game abnormally please wait 30-60 seconds for the character to be released and try logging on again.[WHITE]");
      }
    }
    while (playerActive);

    return playerName;
  }

  //receiveData method.
  //This receives data from the player.
  public String receiveData() throws IOException {
   StringBuffer incommingData;
   String incommingData2;
   int inputData;
   int idleCount = 0;
   int timeOutCount = 0;
   int timeOutLimit = 500;
   byte[] bytes;

   incommingData = new StringBuffer();

   // read data from the player until a null character is received
   try {
     while (!isCommunicationError() && incommingData.length() == 0) {

       // read data from the player until a null character is received
       if (input.available() != 0 && incommingData.length() == 0) {
         inputData = input.read();
         while (inputData != '\n' && inputData != -1 && !isCommunicationError()) {
           if (inputData != '\r') incommingData.append((char) inputData);
           inputData = input.read();
         }

         // if user is just pressing enter return an empty string
         // bypassing input parsing
         if (incommingData.length() == 0 && inputData == '\n') {
           return "";
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
             if (timeOutCount++ > timeOutLimit) {
               sendData2("\n[RED]Idle Connection terminated by Server.\n\n[YELLOW]Bye Bye[WHITE]\n");
               System.out.println("Idle connection terminated for: " + socket.getInetAddress().toString());
               throw new IOException();
             }
           }
         } catch (InterruptedException ie) {
         }
       }
     }

   } catch (IOException ioe) {
     System.out.println("Comms error receiving data (playerLogon) for: " + socket.getInetAddress().toString());
     System.out.println(ioe);
     throw ioe;
   }

   incommingData2 = parseIncomingData(incommingData.toString());

   /*
                bytes = incommingData2.getBytes();
                for (int x=0;x<incommingData2.length();x++) {
                System.out.println("RECEIVE: " + incommingData2.charAt(x) + "\t" + bytes[x] + "\t" + (int) incommingData2.charAt(x));
                }
    */

   return incommingData2;
 }

  //parseIncomingData method.
  //Used for.. something.
  public String parseIncomingData(String data) throws IOException {
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

    return output.toString();
  }


  //parseOutgoingData method.
  //Used again... for something.


  public PlayerCharacter createNewCharacter(String playerName) throws IOException {
    String password = null;
    String validatePassword;
    String gender;
    PlayerCharacter newPlayer = new PlayerCharacter(socket, input, output, playerName);

    do {
      while (password == null || password.length() == 0) {
        sendData("Enter a password for your character:");
        password = receiveData();
        if (password.length() < 6) {
          sendData2("[B][RED]Passwords must be at least six characters in length.[R][WHITE]\n");
          password = null;
        }
      }

      sendData("Enter your password again for verification:");
      validatePassword = receiveData();
      if (!password.equals(validatePassword)) {
        sendData2("[B][RED]First and second passwords do not match.[R][WHITE]");
        password = null;
      }
    }
    while (password == null);

    String race;
    do {
      System.out.println("Starting race selection");
      sendData2("Please select a race:\n");
      sendData("[R][CYAN]a) Human          [B][RED]g) Drow Elf[R][CYAN]\nb) Moon Elf       [B][RED]h) Ogre[R][CYAN]\nc) Dwarf          [B][RED]i) Duergar Dwarf[R][CYAN]\nd) Half-Elf       [B][RED]j) Illithid[R][CYAN]\ne) Gnome          [B][RED]k) Troll[R][CYAN]\nf) Aasimar        [B][RED]l) Tiefling[R][WHITE]");
      race = receiveData();

      if (!race.toUpperCase().equals("A") && !race.toUpperCase().equals("B")
          && !race.toUpperCase().equals("C") && !race.toUpperCase().equals("D")
          && !race.toUpperCase().equals("E") && !race.toUpperCase().equals("F")
          && !race.toUpperCase().equals("G") && !race.toUpperCase().equals("H")
          && !race.toUpperCase().equals("I") && !race.toUpperCase().equals("J")
          && !race.toUpperCase().equals("K") && !race.toUpperCase().equals("L")) {
        sendData("[B][RED]Please enter A through L only.[R][WHITE]");
      }
    }while (!race.toUpperCase().equals("A") && !race.toUpperCase().equals("B")
            && !race.toUpperCase().equals("C") && !race.toUpperCase().equals("D")
            && !race.toUpperCase().equals("E") && !race.toUpperCase().equals("F")
            && !race.toUpperCase().equals("G") && !race.toUpperCase().equals("H")
            && !race.toUpperCase().equals("I") && !race.toUpperCase().equals("J")
            && !race.toUpperCase().equals("K") && !race.toUpperCase().equals("L"));
    Race playerRace = Race.determineRace(race);
    //playerRace.setName("[B][YELLOW]Human[WHITE][R]");
    System.out.println("Setting race...");
    newPlayer.setRace(playerRace);
    newPlayer.setBody(Body.createMediumHumanoidBody());
    System.out.println("Setting password...");
    newPlayer.setPassword(password);
    newPlayer.setSpeed(30);
    System.out.println(playerRace.getShortName());
    if(!(playerRace.getShortName().equals("Ill"))) {
      sendData("Choose a gender for your character (M/F):");
      String choice = "";
      do {
        choice = receiveData();
        if ((!choice.toUpperCase().equals("M")) && (!choice.toUpperCase().equals("F"))) {
          sendData("[B][RED]Please enter M or F.[R][WHITE]");
        }
      }while ((!choice.toUpperCase().equals("M")) && (!choice.toUpperCase().equals("F")));

      if (choice.toUpperCase().equals("M")) {
        newPlayer.setGender(Mobile.MALE);
      }
      if (choice.toUpperCase().equals("F")) {
        newPlayer.setGender(Mobile.FEMALE);
      }
      sendData2("[CYAN][B]Gender chosen: [WHITE]" + newPlayer.getMaleFemaleIt() + "[R]\n");
      System.out.println("Sent data.");
    }

    else { System.out.println("HIT IT CLAUSE"); newPlayer.setGender(Mobile.IT); sendData2("[CYAN][B]Gender chosen: [WHITE]" + newPlayer.getMaleFemaleIt() + "[R]\n"); }
    //leave this alone for now. it will have to be re-arranged.
    //
    //####
    System.out.println("Setting name...");
    newPlayer.setName(playerName);
    System.out.println("Setting alignment...");
    Alignment alignment = new Alignment(Alignment.CHAOTIC, Alignment.EVIL);
    newPlayer.setAlignment(alignment);
    System.out.println("Setting description...");
    newPlayer.setDescription("You see nothing special about " + newPlayer.getHimHerIt());
    File currentDirectory = new File("players");
    System.out.println("Adding test spell...");
    System.out.println("Setting class...");
    newPlayer.setMobileClass(MobileClassFactory.makeBarbarian());
    MobileClass mobClass = newPlayer.getMobileClass();
    mobClass.setSkills(mobClass.getSkillList());
    newPlayer.setType(PlayerCharacter.MORTAL);
    sendData2("[CYAN]The [B][WHITE]" + newPlayer.getTypeString() +"[R][CYAN] " + newPlayer.getRaceString() + " [R][WHITE]" + newPlayer.getMobileClass().getDisplayName() + " " + newPlayer.getName() + " [CYAN]has been created.\n");
    newPlayer.setLogonDate();
    newPlayer.savePlayer();

    return newPlayer;
  }


}
