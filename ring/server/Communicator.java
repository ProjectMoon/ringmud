package ring.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ring.util.TextParser;

/**
 * This class facilitates communication between a user and the server. This is
 * a standardized method of sending data back and forth. It also alleviates issues
 * with the newline character, as there are now defined methods for newlines or
 * not newlines.
 * @author jeff
 */
public class Communicator {
    private BufferedInputStream input;
    private BufferedOutputStream output;
    private boolean error;
    private Socket socket;
    private String suffix;
    
    public Communicator(Socket s) {
        try {
            socket = s;
            input = new BufferedInputStream(s.getInputStream());
            output = new BufferedOutputStream(s.getOutputStream());
            error = false;
            suffix = "";
        } 
        catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Tells whether this Communicator has experienced an error in communicating with
     * its client or not. This variable gets set when different things happen. For example:
     * the player disconnects immediately instead of using the quit command.
     * @return
     */
    public boolean isCommunicationError() {
        return error;
    }
    
    /**
     * Basic method that sends data without a newline on the end.
     * @param data
     */
    public void send(String data) {
        try {
            if (data.length() > 0) {
                data = TextParser.trimNewlines(data);
                data += suffix;
                data = TextParser.parseOutgoingData(data);
                output.write(data.getBytes());
                output.write(0);
                output.flush();
            }
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }
        
    /**
     * Sends a line of text to the client.
     * @param data
     */
    public void sendln(String data) {
        try {
            if (data.equals("") || data.length() > 0) {
                data = TextParser.trimNewlines(data);
                data += suffix;
                data = TextParser.parseOutgoingData(data);
                data += "\n";
                output.write(data.getBytes());
                output.write(0);
                output.flush();
            }
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a newline character to the client without the suffix.
     */
    public void sendln() {
        try {
            output.write("\n".getBytes());
            output.write(0);
            output.flush();
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }    
    
    /**
     * Sends some text without appending the suffix.
     * @param data
     */
    public void sendNoSuffix(String data) {
        try {
            if (data.equals("") || data.length() > 0) {
                data = TextParser.trimNewlines(data);
                data = TextParser.parseOutgoingData(data);
                output.write(data.getBytes());
                output.write(0);
                output.flush();
            }
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a line of text without appending the suffix.
     * @param data
     */
    public void sendlnNoSuffix(String data) {
        try {
            if (data.equals("") || data.length() > 0) {
                data = TextParser.trimNewlines(data);
                data = TextParser.parseOutgoingData(data);
                data += "\n";
                output.write(data.getBytes());
                output.write(0);
                output.flush();
            }
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }    
    
    public void setSuffix(String s) {
        suffix = s;
    }
    
    /**
     * Waits for the user on the other end of this Communicator to send us some data.
     * The wait is indefinite until the idle timeout; the thread will sleep for 50 
     * milliseconds every time it has to wait. After a while the server will terminate
     * the connection and the communication error variable will be set.
     * @return The received data.
     */
    public String receiveData() {
        StringBuffer incomingData;
        String retData = null;
        int incomingByte;
        int idleCount = 0;
        int timeOutCount = 0;
        int timeOutLimit = 500;
        byte[] bytes;

        incomingData = new StringBuffer();

        // read data from the player until a null character is received
        try {
            //this big while loop is needed to actually block the communicator.
            //this is what allows us to WAIT for data.
            while (!isCommunicationError() && incomingData.length() == 0) {
                //read data from the player until a null character is received
                if (input.available() != 0 && incomingData.length() == 0) {
                    incomingByte = input.read();
                    //loop through the available data of the input stream to construct
                    //the data received
                    while (incomingByte != '\n' && incomingByte != -1 && !isCommunicationError()) {
                        if (incomingByte != '\r')
                            incomingData.append((char)incomingByte);
                        
                        incomingByte = input.read();
                    }

                    //if the user is just pressing enter, just return an empty string
                    if (incomingData.length() == 0 && incomingByte == '\n')
                        return "";                    
                        
                    //we should only reach the end of a stream when the socket
                    //is closed, therefore error on it.
                    if (incomingByte == -1)
                        throw new IOException();
                    
                } 
                else {
                    //here we actually wait by having the thread sleep.
                    try {
                        Thread.sleep(50);
                        idleCount++;
                        if (idleCount > 50) {
                            send("");
                            idleCount = 0;
                            if (timeOutCount++ > timeOutLimit) {
                                sendln("[RED]Idle Connection terminated by Server.\n\n[YELLOW]Bye Bye[WHITE]");
                                System.out.println("Idle connection terminated for: " + socket.getInetAddress().toString());
                                throw new IOException();
                            }
                        }
                    } 
                    catch (InterruptedException ie) {}
                }
            } //end huge while loop

        } //end huge try block
        catch (IOException e) {
            System.out.println("Comms error receiving data (playerLogon) for: " + socket.getInetAddress().toString());
            e.printStackTrace();
            error = true;
        }

        try {
            retData = parseReceived(incomingData.toString());
        }
        catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
        return retData;
    }
    
    /**
     * Parses incoming data to remove telnet protocol commands.
     * @param data
     * @return An input data string without telnet protocol commands in it.
     * @throws java.io.IOException
     */
    private String parseReceived(String data) throws IOException {
        char dataByte;

        StringBuffer output = new StringBuffer();

        // loop through each data character checking for TELNET protocol commands
        for (int x = 0; x < data.length(); x++) {
            dataByte = data.charAt(x);
            
            /*if (dataByte != '\377' && dataByte != '\373' && dataByte != '\375')
                output.append(dataByte);
            else
                x += 2; //This is +2 so that it skips both telnet bytes. One is the cmd, one is a parameter.
             */
            
            // if hex 255 incoming IAC
            if (dataByte == '\377') {
                dataByte = data.charAt(++x);

                // if command is WILL echo DON'T
                if (dataByte == '\373') {
                    dataByte = data.charAt(++x);
                    sendNoSuffix("\377\376" + dataByte);
                    continue;
                }

                // if command is DO echo WON'T
                if (dataByte == '\375') {
                    dataByte = data.charAt(++x);
                    sendNoSuffix("\377\374" + dataByte);
                    continue;
                }
            } else {
                output.append(dataByte);
            }
        }

        return output.toString();
    }

}
