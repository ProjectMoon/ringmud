package ring.players;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Socket socket;    //These are the input and output of above connection.
    private BufferedInputStream input;
    private BufferedOutputStream output;    //Stuff.
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

    /**
     * This method is where we wait for the player to log on. The player is first prompted
     * if they want to create a new character. At this point they may type yes or no.
     * If they choose yes, the method createNewCharacter() is called. In this method, all
     * of the information is gathered for a new character. The information is then written
     * to the player file.
     * 
     * Ifthey choose no, they go to the login screen.
     */
    public void run() {
        PlayerCharacter enteringPlayer = null;

        try {
            //wait for log on.
            waiting = true;
            sendData(welcomeText + "\n[WHITE]Create new character? (Y/N)");

            while (waiting) {
                String answer = receiveData();

                if (answer.toLowerCase().equals("y")) {
                    enteringPlayer = createNewCharacter();
                }
                else if (answer.toLowerCase().equals("n")) {
                    sendData("Please type your name: ");
                    String name = receiveData();
                    enteringPlayer = loadCharacter(name);
                } 
                else {
                    sendData("Please enter Y or N.");
                }

                //We should now have a player to load. If not, we start the whole process again.
                if (enteringPlayer != null) {
                    waiting = false;
                    Thread playerThread = new Thread(World.getWorld().getPlayerThreadGroup(), enteringPlayer,
                            "Player [" + enteringPlayer.getName() + "] ");
                    playerThread.setDaemon(true);
                    enteringPlayer.setThread(playerThread);
                    playerThread.start();
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in character creation.");
        }
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
            sendData2(data);
        }
       
    }

    //sendData2 method.
    //This handles the actual sending of the data. sendData checks to see if there is
    //any data to actually send.
    public void sendData2(String data) throws IOException {
        if (data.length() != 0) {
            data = TextParser.parseOutgoingData(data);
        }
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

    
    public PlayerCharacter loadCharacter(String name) {
        try {
            sendData("You cannot yet load characters!");
        } catch (IOException ex) {
            Logger.getLogger(PlayerLogon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public PlayerCharacter createNewCharacter() {
        try {
            sendData("[RED]Entering new character creation mode...\n[WHITE]Please enter a name for this character:");
            String name = receiveData();
            PlayerCharacter character = doCreateNewCharacter(name);
            return character;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * This method handles the actual creation of a new character given a name.
     * The method is further broken down into several smaller helper methods so as
     * to not clutter the code.
     * @param playerName
     * @return A completed PlayerCharacter object.
     * @throws java.io.IOException If there is an error saving the player to a file.
     */
    private PlayerCharacter doCreateNewCharacter(String playerName) throws IOException {
        String password;
        int gender;
        Race race;
        Alignment alignment;
        MobileClass playerClass;
        PlayerCharacter newPlayer = new PlayerCharacter(socket, input, output, playerName);

        password = createPassword();

        race = chooseRace();
        sendData2("[CYAN][B]Race chosen: [WHITE]" + race.getName() + "[R]\n");

        gender = chooseGender(race);
        sendData2("[CYAN][B]Gender chosen: [WHITE]" + gender + "[R]\n");

        alignment = chooseAlignment();
        sendData2("[CYAN][B]Alignment chosen: [WHITE]" + alignment.getAlignmentString() + "[R]\n");

        playerClass = chooseClass();
        sendData2("[CYAN][B]Class chosen: [WHITE]" + playerClass.getDisplayName() + "[R]\n");

        System.out.println("Setting various player attributes...");
        //Set basic info
        newPlayer.setName(playerName);
        newPlayer.setPassword(password);
        newPlayer.setType(PlayerCharacter.MORTAL);
        newPlayer.setDescription("You see nothing special about " + newPlayer.getHimHerIt() + ".");

        //Set some physical and alignment characteristics
        newPlayer.setRace(race);
        newPlayer.setBody(race.getBody());
        newPlayer.setSpeed(30);
        newPlayer.setGender(gender);
        newPlayer.setAlignment(alignment);

        //Set class, skills, etc.
        newPlayer.setMobileClass(playerClass);

        //Save the player and print a message
        newPlayer.setLogonDate();
        newPlayer.savePlayer();
        sendData2("[CYAN]The [B][WHITE]" + newPlayer.getTypeString() + "[R][CYAN] " + newPlayer.getRaceString() + " [R][WHITE]" + newPlayer.getMobileClass().getDisplayName() + " " + newPlayer.getName() + " [CYAN]has been created.\n");


        return newPlayer;
    }

    /**
     * This method creates a valid password that the user will use to log in once their character
     * is created. Valid passwords must be at least 6 characters long, and has to be typed twice to
     * validate it.
     * @return A valid password string.
     */
    private String createPassword() throws IOException {
        String password = null;
        String validatePassword = null;

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
        } while (password == null);

        return password;
    }

    /**
     * This method returns a race based on a letter choice the user inputs.
     * It will print errors until the player chooses the right race.
     * @return The race chosen by the user in the form of a Race object.
     */
    private Race chooseRace() throws IOException {
        Race race = null;

        do {
            sendData2("Please select a race:\n");
            sendData("[R][CYAN]a) Human          [B][RED]g) Drow Elf[R][CYAN]\nb) Moon Elf       [B][RED]h) Ogre[R][CYAN]\nc) Dwarf          [B][RED]i) Duergar Dwarf[R][CYAN]\nd) Half-Elf       [B][RED]j) Illithid[R][CYAN]\ne) Gnome          [B][RED]k) Troll[R][CYAN]\nf) Aasimar        [B][RED]l) Tiefling[R][WHITE]");
            String choice = receiveData();
            race = Race.determineRace(choice);

            if (race == null) {
                sendData("That is not a valid choice.");
            }
        } while (race == null);

        return race;
    }

    /**
     * This helper method allows the user to choose their character's gender.
     * The genders allowed are based on the race object passed to the method.
     * @param race The player's chosen race determines what genders they can be.
     * @return An integer representing the player's gender (Male/female/asexual);
     */
    private int chooseGender(Race race) throws IOException {
        String raceName = race.getShortName();

        //Illithids are only asexual
        if (raceName.equals("Ill")) {
            return Mobile.IT;        //Otherwise we move on to choosing M/F
        }
        String choice = "";
        int gender = -1; //0 is male, 1 female, 2 asexual

        do {
            sendData("Please enter a gender (M/F)");
            choice = receiveData();
            if (choice.toLowerCase().equals("m")) {
                gender = Mobile.MALE;
            } else if (choice.toLowerCase().equals("f")) {
                gender = Mobile.FEMALE;
            } else {
                gender = -1;
            }
        } while (gender < 0);


        return gender;
    }

    /**
     * This method asks the user to input an ethical and moral alignment in:
     * Lawful Good, Chaotic Evil, etc.
     * @return The constructed alignment object.
     */
    private Alignment chooseAlignment() throws IOException {
        int ethical = -1;
        int moral = -1;
        String choice = "";


        do {
            sendData("Please choose an ethical perspective (L, N, C)");
            choice = receiveData();
            if (choice.toLowerCase().equals("l")) {
                ethical = Alignment.LAWFUL;
            } else if (choice.toLowerCase().equals("n")) {
                ethical = Alignment.NEUTRAL;
            } else if (choice.toLowerCase().equals("c")) {
                ethical = Alignment.CHAOTIC;
            }
        } while (ethical == -1);



        do {
            sendData("Please input a moral perspective (G, N, E)");
            choice = receiveData();
            if (choice.toLowerCase().equals("g")) {
                moral = Alignment.GOOD;
            } else if (choice.toLowerCase().equals("n")) {
                moral = Alignment.NEUTRAL;
            } else if (choice.toLowerCase().equals("e")) {
                moral = Alignment.EVIL;
            }
        } while (moral == -1);

        Alignment a = new Alignment(ethical, moral);
        return a;
    }

    /**
     * This helper method allows the user to type in the name of the class they
     * wish to play. It accepts full names (barbarian, sorcerer, etc).
     * @return The MobileClass object representing the chosen class.
     */
    private MobileClass chooseClass() throws IOException {
        String choice = "";
        MobileClass mc = null;

        do {
            sendData2("Please choose from the following classes. Type in the full name to choose it.\n");
            sendData("Barbarian, Cleric, Druid, Fighter, Monk, Paladin, Ranger, Rogue, Sorcerer, Wizard\n");
            choice = receiveData();
            mc = MobileClassFactory.determineClass(choice);
        } while (mc == null);

        return mc;
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
        } while (playerActive);

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
                        if (inputData != '\r') {
                            incommingData.append((char) inputData);
                        }
                        inputData = input.read();
                    }

                    // if user is just pressing enter return an empty string
                    // bypassing input parsing
                    if (incommingData.length() == 0 && inputData == '\n') {
                        return "";
                    }

                    // we should only reach the end of a stream when the socket
                    // is closed, therefore error on it.
                    if (inputData == -1) {
                        throw new IOException();
                    //log.message(" Received " + incommingData.length() + " bytes from " + getMobileName());
                    }
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
                    //sendData2("\377\376" + dataByte);
                    continue;
                }

                // if command is DO echo WON'T
                if (dataByte == '\375') {
                    dataByte = data.charAt(++x);
                    //sendData2("\377\374" + dataByte);
                    continue;
                }
            } else {
                output.append(dataByte);
            }
        }

        return output.toString();
    }    //parseOutgoingData method.
    //Used again... for something.
}
