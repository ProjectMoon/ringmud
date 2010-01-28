package ring.server.telnet;

import java.io.IOException;
import java.io.InputStream;

import net.wimpi.telnetd.io.BasicTerminalIO;

/**
 * InputStream implementation built on top of telnetd2. Allows standard Java I/O classes and
 * practices to be used to get and send data to the connected user. By default, this InputStream
 * echoes all input back to the user. This can be turned off by calling setEcho(false).
 * <br/>
 * <br/>
 * This class should be thread-safe, as the underlying I/O library synchronizes its read methods.
 * @author projectmoon
 *
 */
public class TelnetInputStream extends InputStream {
	private BasicTerminalIO io;
	private boolean echo = true;
	
	/**
	 * Creates an input stream from the given BasicTerminalIO object.
	 * @param io The IO object. Usually gotten from a telnet connection.
	 */
	public TelnetInputStream(BasicTerminalIO io) {
		this.io = io;
	}
	
	@Override
	public int read() throws IOException {
		int i = io.read();
		
		if (echo) {
			io.write((char)i);
		}
		
		return i;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int bytesRead = 0;
		for (int c = off; c < len; c++) {
			int i = io.read();
				
			//Yes, this is a terrible hack. BACKSPACE - 1 is the actual backspace key,
			//whereas BACKSPACE is ^H key. This should cover most modern telnet connections...
			//Depending on user settings, they could have this mapped to either BACKSPACE
			//or BACKSPACE -1.
			if (i == BasicTerminalIO.BACKSPACE || i == BasicTerminalIO.BACKSPACE - 1) {
				//Can't backspace beyond the start...
				if (bytesRead > 0) {
					
					//Update visually.
					io.moveLeft(1);
					io.write(' ');
					io.moveLeft(1);
					
					//and internally.
					b[c - off] = (byte)0;					
					bytesRead --;
					//needs to be decremented because we are moving backwards in the array.
					c--; 
					
				}
				
				//decrement c because we either went backwards or nowhere.
				//loop's c++ will offset this, but if we don't do it,
				//c keeps increasing.
				c--;
				continue;
			}
			else {
				//This is a regular character, so we read it into the stream.				
				b[c - off] = (byte)i;
				bytesRead++;
				
				//Echo it back, if it's turned on.
				//Currently will also echo newlines.
				if (echo) {
					io.write((char)i);
				}
			}
			
			if ((char)b[c - off] == '\n') break;
		} //end of for loop.
		
		return bytesRead;
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
	
	/**
	 * Whether or not to echo input back to the user as input is typed.
	 * This defaults to true.
	 * @return
	 */
	public boolean getEcho() {
		return echo;
	}
	
	/**
	 * Sets whether or not to echo input back to the user as input is typed.
	 * @param echo
	 */
	public void setEcho(boolean echo) {
		this.echo = echo;
	}

}
