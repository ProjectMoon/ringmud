package ring.server.telnet;

import java.io.PrintStream;
import java.util.Scanner;

import ring.server.CommunicationException;
import ring.server.Communicator;
import ring.util.TextParser;

public class TelnetStreamCommunicator implements Communicator {
	private TelnetInputStream lowlevelInput;
	private TelnetOutputStream lowlevelOutput;
	
	private Scanner in;
	private PrintStream out;
	private String suffix = "";
	private boolean error = false;
	private boolean screenWidthParsing = true;
	
	public TelnetStreamCommunicator(TelnetInputStream in, TelnetOutputStream out) {
		lowlevelInput = in;
		lowlevelOutput = out;
		this.in = new Scanner(in);
		this.out = new PrintStream(out);
	}
	
	@Override
	public void close() {
		in.close();
		out.close();
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
	
	public boolean isSecure() {
		//TODO later, return true if connection is from localhost.
		return false;
	}
	
	public void setScreenWidthParsing(boolean val) {
		screenWidthParsing = val;
	}
	
	public boolean getScreenWidthParsing() {
		return screenWidthParsing;
	}

	private void doSendln(String data) throws CommunicationException {
		out.println(data);
	}
	
	private void doSend(String data) throws CommunicationException {
		out.print(data);
	}

	private boolean isValid(String data) {
		//return (!data.equals("") && data.length() > 0);
		return true;
	}

	private String formatData(String data, boolean useSuffix) {
		data = TextParser.trimNewlines(data);

		if (useSuffix) {
			StringBuilder sb = new StringBuilder(data);
			sb.append(getSuffix());
			data = sb.toString();
		}
		
		data = TextParser.parseOutgoingData(data, screenWidthParsing);

		return data;
	}

	/**
	 * Basic method that sends data without a newline on the end.
	 * 
	 * @param data
	 */
	public void print(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true);
			doSend(data);
		}
	}

	/**
	 * Sends a line of text to the client.
	 * 
	 * @param data
	 */
	public void println(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true);
			doSendln(data);
		}
	}

	/**
	 * Sends a newline character to the client without the suffix.
	 */
	public void println() throws CommunicationException {
		out.println();
	}
	
	public void printSuffix() {
		//This weirdness works because the low level input stream
		//echoes newlines. So, there is a newline generated when enter
		//is pressed, and thus one \n between two empty suffixes.
		out.print(formatData(getSuffix(), false));
	}

	/**
	 * Sends some text without appending the suffix.
	 * 
	 * @param data
	 */
	public void printNoSuffix(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, false);
			doSend(data);
		}
	}

	/**
	 * Sends a line of text without appending the suffix.
	 * 
	 * @param data
	 */
	public void printlnNoSuffix(String data) {
		if (isValid(data)) {
			data = formatData(data, false);
			doSendln(data);
		}
	}

	/**
	 * Prepends a newline character to the data after having parsed it normally
	 * as per the send method. Also appends the suffix. This is mostly used for
	 * sending data to the user from code external to the shell. Instead of the text
	 * appearing in the middle of their command line it appears below it.
	 * 
	 * @param data
	 */
	public void printWithPreline(String data) throws CommunicationException {
		if (isValid(data)) {
			data = formatData(data, true);
			data = "\n" + data;
			doSendln(data);
		}
	}

	/**
	 * Sets the suffix that is appended to outgoing data in most versions of the
	 * send command. In general, the suffix is a command prompt of some kind.
	 * However, it can be used for other things.
	 * 
	 * @param s - The suffix to use.
	 */
	public void setSuffix(String s) {
		suffix = s;
	}
	
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Waits for the user on the other end of this Communicator to send us some
	 * data. The wait is indefinite until the idle timeout. After a while the
	 * server will terminate the connection and the communication error variable
	 * will be set.
	 * 
	 * @return The received data.
	 */
	public String receiveData() throws CommunicationException {
		return in.nextLine();
	}
	
	public boolean isConnected() {
		//Terrible implementation.
		try {
			out.print("");
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public TelnetInputStream getInputStream() {
		return lowlevelInput;
	}
	
	public TelnetOutputStream getOutputStream() {
		return lowlevelOutput;
	}

}
