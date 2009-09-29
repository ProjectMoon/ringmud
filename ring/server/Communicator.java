package ring.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import ring.util.TextParser;

/**
 * This class facilitates communication between a user and the server. This is a
 * standardized method of sending data back and forth. It also alleviates issues
 * with the newline character, as there are now defined methods for newlines or
 * not newlines.
 * 
 * @author jeff
 */
public class Communicator {
	private TimeoutTask timeoutTask = new TimeoutTask();
	private Timer timeoutTimer = new Timer();
	
	/**
	 * A heartbeat task to periodically make sure this Communicator is still open.
	 */
	private TimerTask heartbeat = new TimerTask() {
		@Override
		public void run() {
			testConnection();
		}
		
	};

	private static Logger log = Logger.getLogger(Communicator.class.getName());
	private BufferedInputStream input;
	private BufferedOutputStream output;
	private boolean error;
	private Socket socket;
	private String suffix;

	/**
	 * Constructs a new Communicator given a connection (socket) to use.
	 * 
	 * @param s - The client-server connection Socket to use for I/O.
	 */
	public Communicator(Socket s) {
		if (s.isClosed()) {
			throw new IllegalArgumentException("Socket must be open!");
		}
		try {
			socket = s;
			input = new BufferedInputStream(s.getInputStream());
			output = new BufferedOutputStream(s.getOutputStream());
			error = false;
			suffix = "";
			
			//Schedule the two timers 10 ms apart.
			timeoutTimer.scheduleAtFixedRate(timeoutTask, 0, TimeoutTask.SECOND);
			timeoutTimer.scheduleAtFixedRate(heartbeat, 10, 10 * TimeoutTask.SECOND);
			
		} catch (IOException ex) {
			Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * Tells whether this Communicator has experienced an error in communicating
	 * with its client or not. This variable gets set when different things
	 * happen. For example: the player disconnects immediately instead of using
	 * the quit command.
	 * 
	 * @return
	 */
	public boolean isCommunicationError() {
		return error;
	}

	private void doSend(String data) throws CommunicationException {
		try {
			output.write(data.getBytes());
			output.write(0);
			output.flush();
		} catch (IOException e) {
			error = true;
			throw new CommunicationException(e);
		}
	}

	private boolean isValid(String data) {
		return (!data.equals("") && data.length() > 0);
	}

	private String formatData(String data, boolean useSuffix, boolean newline) {
		data = TextParser.trimNewlines(data);

		if (useSuffix || newline) {
			StringBuilder sb = new StringBuilder(data);

			if (useSuffix)
				sb.append(suffix);
			if (newline)
				sb.append('\n');

			data = sb.toString();
		}
		
		data = TextParser.parseOutgoingData(data);

		return data;
	}

	/**
	 * Basic method that sends data without a newline on the end.
	 * 
	 * @param data
	 */
	public void send(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true, false);
			doSend(data);
		}
	}

	/**
	 * Sends a line of text to the client.
	 * 
	 * @param data
	 */
	public void sendln(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true, true);
			doSend(data);
		}
	}

	/**
	 * Sends a newline character to the client without the suffix.
	 */
	public void sendln() throws CommunicationException {
		try {
			output.write("\n".getBytes());
			output.write(0);
			output.flush();
		} catch (IOException e) {
			error = true;
			throw new CommunicationException(e);
		}
	}

	/**
	 * Sends some text without appending the suffix.
	 * 
	 * @param data
	 */
	public void sendNoSuffix(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, false, false);
			doSend(data);
		}
	}

	/**
	 * Sends a line of text without appending the suffix.
	 * 
	 * @param data
	 */
	public void sendlnNoSuffix(String data) {
		if (isValid(data)) {
			data = formatData(data, false, true);
			doSend(data);
		}
	}

	/**
	 * Prepends a newline character to the data after having parsed it normally
	 * as per the send method. Also appends the suffix. This is mostly used for
	 * sending data to the user from other sources. Instead of the text
	 * appearing in the middle of their command line it appears below it.
	 * 
	 * @param data
	 */
	public void sendWithPreLine(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true, true);
			data = "\n" + data;
			doSend(data);
		}
	}

	/**
	 * A private method used to make sure the user is still alive. It simply
	 * sends a blank string to the other end of the connection.
	 */
	private void testConnection() {
		try {
			System.out.println("heartbeat for " + this);
			output.write("".getBytes());
			output.write(0);
			output.flush();
		} catch (IOException e) {
			System.err.println("socket is dead. closing.");
			timeoutTimer.cancel();
			error = true;
			if (!socket.isClosed()) {
				killConnection();
			}
		}
	}

	/**
	 * Sets the suffix that is appended to outgoing data in most versions of the
	 * send command. In general, the suffix is a command prompt of some kind.
	 * However, it can be used for other things.
	 * 
	 * @param s
	 *            - The suffix to use.
	 */
	public void setSuffix(String s) {
		suffix = s;
	}

	/**
	 * Waits for the user on the other end of this Communicator to send us some
	 * data. The wait is indefinite until the idle timeout; the thread will
	 * sleep for 50 milliseconds every time it has to wait. After a while the
	 * server will terminate the connection and the communication error variable
	 * will be set.
	 * 
	 * @return The received data.
	 */
	public String receiveData() throws CommunicationException {
		StringBuffer incomingData;
		int incomingByte;

		incomingData = new StringBuffer();

		// read data from the player until a null character is received
		try {
			// this big while loop is needed to actually block the communicator.
			// this is what allows us to WAIT for data.
			while (!isCommunicationError() && incomingData.length() == 0) {
				// Check if we're timed out. If so, the socket gets closed.
				checkTimeout();

				if (input.available() != 0 && incomingData.length() == 0) {
					// User is no longer idle.
					clearTimeout();

					// loop through the available data of the input stream to
					// construct
					// the data received
					incomingByte = input.read();
					while (incomingByte != '\n' && incomingByte != -1
							&& !isCommunicationError()) {
						if (incomingByte != '\r')
							incomingData.append((char) incomingByte);

						incomingByte = input.read();
					}

					// if the user is just pressing enter, just return an empty
					// string
					if (incomingData.length() == 0 && incomingByte == '\n') {
						return "";
					}

					// we should only reach the end of a stream when the socket
					// is closed, therefore error on it.
					if (incomingByte == -1)
						throw new CommunicationException();

				}
			} // end huge while loop

		} // end huge try block
		catch (IOException e) {
			System.out.println("Comms error receiving data (playerLogon) for: "
					+ socket.getInetAddress().toString());
			error = true;
			throw new CommunicationException(e);
		}

		return parseReceived(incomingData.toString());

	}

	private void clearTimeout() {
		timeoutTask.timeoutCount = 0;
	}

	private void checkTimeout() {
		if (timeoutTask.isTimedOut()) {
			timeoutTimer.cancel();
			sendln("[RED]Idle Connection terminated by Server.\n\n[YELLOW]Bye bye.[WHITE]");
			log.info("Idle connection terminated for: "
					+ socket.getInetAddress().toString());
			killConnection();
			// Explicitly set error flag because this was not intended
			// (usually).
			error = true;
		}
	}

	/**
	 * Kills the socket connection, but does not set error status unless there
	 * was a problem closing.
	 */
	private void killConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			error = true;
			e.printStackTrace();
		}
	}

	/**
	 * Parses incoming data to remove telnet protocol commands.
	 * 
	 * @param data
	 * @return An input data string without telnet protocol commands in it.
	 * @throws java.io.IOException
	 */
	private String parseReceived(String data) {
		char dataByte;

		StringBuffer output = new StringBuffer();

		// loop through each data character checking for TELNET protocol
		// commands
		for (int x = 0; x < data.length(); x++) {
			dataByte = data.charAt(x);

			/*
			 * if (dataByte != '\377' && dataByte != '\373' && dataByte !=
			 * '\375') output.append(dataByte); else x += 2; //This is +2 so
			 * that it skips both telnet bytes. One is the cmd, one is a
			 * parameter.
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

	public boolean isConnected() {
		return socket.isConnected();
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
