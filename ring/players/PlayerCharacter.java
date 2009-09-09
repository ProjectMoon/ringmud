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
import ring.server.Communicator;

public class PlayerCharacter extends Mobile implements Runnable, CommandSender, TickerListener {
    //Player Constants.
    //Player-Server connection.
    // the player to server connection
    private transient Communicator communicator;
    private transient BufferedInputStream input;
    private transient BufferedOutputStream output;
    private transient Thread thread;    //Player instance variables.
    private String password;
    private Date lastLogon;
    private transient boolean TTPActive;
    private transient long TTPTotal;
    private transient int TTPCount;
    private transient Date TTPStart;
    private transient Date TTPEnd;
    private transient Date sessionStartTime;
    private transient String lastCommand = null;    //Other variables.
    private boolean quitting;

    public PlayerCharacter() {
    }

    public PlayerCharacter(Socket socket, String pName) throws IOException {
        communicator = new Communicator(socket);
        communicator.setSuffix(getPrompt());
        password = "";
        lastLogon = new Date();
        super.setName(pName);
    //World.getWorld().getTicker().addTickerListener(this, "PULSE");
    }

    public void setCommunicator(Communicator c) {
        communicator = c;
    }
    
    public void setConnection(Socket sock) {
        communicator = new Communicator(sock);
    }
    
    public void setPassword(String pass) {
        password = pass;
    }
    
    public String getPassword() {
        return password;
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
        super.processTick(e);
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
        Room room = World.getWorld().r1;
        room.addMobile(this);
        setLocation(room);
       
        //The player has to poof into existence!
        World.sendVisualToLocation(this, "There is a loud bang and a puff of smoke, and " + super.getName() + " appears in the world once more!", "You hear a loud bang and smell acrid smoke. Someone has appeared in the world once more!");
        doCommand("look");        

        //Wait for commands.
        while (!isDead) {
            Thread.currentThread().yield();
            communicator.setSuffix(getPrompt()); //Necessary in case of updates to prompt info.
            doCommand(communicator.receiveData());
        }
    }

    //getPrompt method.
    //This returns the prompt that the player has set.
    private String getPrompt() {
        return "\n\n[B][GREEN]HP: " + super.getCurrentHPString() + "/" + super.getMaxHPString() + " MV: " + super.getCurrentMV() + "/" + super.getMaxMV() + " ]> [R][WHITE]";
    }
    
    //getPrompt method.
    //This returns the prompt that the player has set, except with only 1 newline.
    private String getSingleLinePrompt() {
        return "\n[B][GREEN]HP: " + super.getCurrentHPString() + "/" + super.getMaxHPString() + " MV: " + super.getCurrentMV() + "/" + super.getMaxMV() + " ]> [R][WHITE]";
    }    

    //decrementLockTime method.
    //Overriden to display "Your head clears." after time is up.
    public void decrementLockTime() {
        super.decrementLockTime();
        if (super.lockTimeRemaining <= 0) {
            communicator.send(super.lockFinishedMessage);
        }
    }

    //doCommand method.
    //This is the main way to send commands to the world so it parses, handles them, and
    //returns a result.
    public void doCommand(String command) {
        //Was anything even typed?
        if (command.length() <= 0) {
            communicator.setSuffix(getSingleLinePrompt());
            communicator.send("");
            communicator.setSuffix(getPrompt());
            return;        
        }
        
        //Is the player locked?
        if (this.isLocked) {
            communicator.send(super.lockMessage + " (" + super.lockTimeRemaining * 2 + " seconds left)");
            return;
        }
        
        //Is the player requesting to repeat the last command?
        if (command.equals("!!")) command = lastCommand;

        System.out.println("Sending: " + command);

        //Send the command.
        CommandResult res = super.handler.sendCommand(command);
        String result = res.getText();
        System.out.println("RESULT: " + result);
        
        communicator.send(result);
        
        //Only update last command if the last command wasn't !!
        if (!command.equals("!!"))
            lastCommand = command;
        
        communicator.setSuffix(getPrompt()); //Necessary in case of updates to prompt info.       
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
    
    public Communicator getCommunicator() {
        return communicator;
    }
}
