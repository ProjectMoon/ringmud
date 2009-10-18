package ring.players;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

import ring.commands.CommandResult;
import ring.commands.CommandSender;
import ring.mobiles.Mobile;
import ring.movement.LocationManager;
import ring.movement.Room;
import ring.server.CommunicationException;
import ring.server.Communicator;
import ring.world.TickerEvent;
import ring.world.TickerListener;
import ring.world.World;

/**
 * A class representing a PlayerCharacter in the world. This particular version of Mobile implements
 * client-server communication by having a Communicator.
 * @author projectmoon
 *
 */
public class PlayerCharacter extends Mobile implements Runnable, CommandSender, TickerListener {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PlayerCharacter.class.getName());
	
    //Player-Server connection.
    private transient Communicator communicator;
    private transient Thread thread;
    
    //Player instance variables.
    private String password;
    private Date lastLogon;
    private transient String lastCommand = null;    //Other variables.
    private boolean quitting;

    public PlayerCharacter() {
    	super.initInternal();
    }

    public PlayerCharacter(Socket socket, String pName) throws IOException {
        communicator = new Communicator(socket);
        communicator.setSuffix(getPrompt());
        password = "";
        setLastLogon(new Date());
        super.setName(pName);
        super.initInternal();
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

    /**
     * This no-arguments setter for lastLogon sets the property to the
     * current date.
     */
    public void setLastLogon() {
        setLastLogon(new Date());
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
        //Start up character.
        log.info("Creating player in the world: " + getName());
        World.getWorld().getTicker().addTickerListener(this, "PULSE");
        //Set location.
        Room room = (Room) LocationManager.getOrigin();
        room.addMobile(this);
        setLocation(room);
       
        //The player has to poof into existence!
        World.sendVisualToLocation(this, "There is a loud bang and a puff of smoke, and " + super.getName() + " appears in the world once more!", "You hear a loud bang and smell acrid smoke. Someone has appeared in the world once more!");
        doCommand("look");        

        //Wait for commands.
        while (!isDead && !quitting && !communicator.isCommunicationError()) {
            Thread.yield();
            communicator.setSuffix(getPrompt()); //Necessary in case of updates to prompt info.
            try {
            	doCommand(communicator.receiveData());
            }
            catch (CommunicationException e) {
            	log.info("There was a socket error for " + this);
            	break;
            }
        }
        
        //Close the player's connection once their loop is done.
        //Handle either graceful quit or forced quit/disconnect.
        if (quitting && !communicator.isCommunicationError()) {
        	//Save player
        	//Send quit message
        	communicator.send("You have successfully quit. Good-bye.");
        	communicator.close();
        	log.info(this + " quit gracefully");
        	return;
        }
        else if (communicator.isCommunicationError()) {
        	//Save player.
        	log.info(this + " experienced disconnect/forced quit.");
        	return;
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

        //Send the command.
        ring.commands.nc.CommandResult res = super.handler.sendCommand(command);
        String result = res.getText();
        
        communicator.send(result);
        
        //Only update last command if the last command wasn't !!
        if (!command.equals("!!"))
            lastCommand = command;
        
        communicator.setSuffix(getPrompt()); //Necessary in case of updates to prompt info.       
    }
    
    public void sendData(String data) {
    	communicator.send(data);
    }

    /**
     * Sets the thread of execution for this Player object.
     * @param thread
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }
    
    /**
     * Gets the thread of execution tied to this player.
     * @return
     */
    public Thread getThread() {
    	return thread;
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

	public void setLastLogon(Date lastLogon) {
		this.lastLogon = lastLogon;
	}

	public Date getLastLogon() {
		return lastLogon;
	}

	public void quit() {
		quitting = true;
	}
	
	public boolean isQuitting() {
		return quitting;
	}
}
