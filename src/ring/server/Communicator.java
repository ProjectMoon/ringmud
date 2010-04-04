package ring.server;

/**
 * This class facilitates communication between a user and the server. This is a
 * standardized method of sending data back and forth. It also alleviates issues
 * with the newline character, as there are now defined methods for newlines or
 * not newlines.
 * 
 * @author jeff
 */
public interface Communicator {
	
	/**
	 * Whether or not this communicator is considered secure.
	 * A secure Communicator has its communications encrypted or otherwise
	 * protected. Most administration commands will not work when the connection
	 * is not secure.
	 * @return true or false
	 */
	public boolean isSecure();

	/**
	 * Tells whether this Communicator has experienced an error in communicating
	 * with its client or not. This variable gets set when different things
	 * happen. For example: the player disconnects immediately instead of using
	 * the quit command.
	 * 
	 * @return
	 */
	public boolean isCommunicationError();

	/**
	 * Basic method that sends data without a newline on the end.
	 * 
	 * @param data
	 */
	public void print(String data) throws CommunicationException;

	/**
	 * Sends a line of text to the client.
	 * 
	 * @param data
	 */
	public void println(String data) throws CommunicationException;

	/**
	 * Sends a newline character to the client without the suffix.
	 */
	public void println() throws CommunicationException;
	
	/**
	 * Prints a newline, followed by only the suffix to the client. 
	 * @throws CommunicationException
	 */
	public void printSuffix() throws CommunicationException;

	/**
	 * Sends some text without appending the suffix.
	 * 
	 * @param data
	 */
	public void printNoSuffix(String data) throws CommunicationException;

	/**
	 * Sends a line of text without appending the suffix.
	 * 
	 * @param data
	 */
	public void printlnNoSuffix(String data) throws CommunicationException;

	/**
	 * Prepends a newline character to the data after having parsed it normally
	 * as per the send method. Also appends the suffix. This is mostly used for
	 * sending data to the user from other sources. Instead of the text
	 * appearing in the middle of their command line it appears below it.
	 * 
	 * @param data
	 */
	public void printWithPreline(String data) throws CommunicationException;

	/**
	 * Sets the suffix that is appended to outgoing data in most versions of the
	 * various <code>print*</code> methods. In general, the suffix is a command prompt of some kind.
	 * However, it can be used for other things.
	 * 
	 * @param s The suffix to use.
	 */
	public void setSuffix(String s);
	
	/**
	 * Gets the suffix that is appended to outgoing data in most versions of the
	 * various <code>print*</code> methods. In general, the suffix is a command
	 * prompt of some kind. However, it can be used for other things.
	 * @return
	 */
	public String getSuffix();
	

	/**
	 * Waits for the user on the other end of this Communicator to send us some
	 * data. The wait is indefinite until the idle timeout; the thread will
	 * sleep for 50 milliseconds every time it has to wait. After a while the
	 * server will terminate the connection and the communication error variable
	 * will be set.
	 * 
	 * @return The received data.
	 */
	public String receiveData() throws CommunicationException;

	/**
	 * Whether or not this Communicator is up and running.
	 * 
	 * @return true or false
	 */
	public boolean isConnected();

	/**
	 * Close the Communicator. Further calls to any send or receive methods
	 * will throw CommunicationExceptions.
	 */
	public void close();
	
	/**
	 * Whether or not to have this Communicator insert newlines based
	 * on screen width.
	 * 
	 * @param val true or false
	 */
	public void setScreenWidthParsing(boolean val);
	
	/**
	 * Gets the screen width parsing mode of this Communicator.
	 * 
	 * @return true or false.
	 */
	public boolean getScreenWidthParsing();
}
