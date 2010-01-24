package ring.server.telnet;

import java.io.IOException;
import java.io.InputStream;

import net.wimpi.telnetd.io.BasicTerminalIO;

/**
 * InputStream implementation built on top of telnetd2. Allows standard Java I/O classes and
 * practices to be used to get and send data to the connected user. By default, this InputStream
 * echoes all input back to the user. This can be turned off by calling setEcho(false).
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
			b[c - off] = (byte)io.read();
			bytesRead++;
			
			if (echo) {
				//Don't echo newlines...
				//Might remove later.
				if ((char)b[c - off] != '\n') {
					io.write(b[c - off]);
				}
			}
			
			if ((char)b[c - off] == '\n') break;
		}
		
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
