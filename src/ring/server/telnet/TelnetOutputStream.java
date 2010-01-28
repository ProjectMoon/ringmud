package ring.server.telnet;

import java.io.IOException;
import java.io.OutputStream;

import net.wimpi.telnetd.io.BasicTerminalIO;

/**
 * OutputStream implementation built on top of telnetd2. Allows standard Java I/O classes and
 * practices to be used to get and send data to the connected user.
 * <br/>
 * <br/>
 * This class should be thread-safe, as the underlying I/O library synchronizes its write methods.
 * @author projectmoon
 *
 */
public class TelnetOutputStream extends OutputStream {
	private BasicTerminalIO io;
	
	/**
	 * Creates a new output stream with the provided BasicTerminalIO object.
	 * @param io The IO object. Usually gotten from a telnet connection.
	 */
	public TelnetOutputStream(BasicTerminalIO io) {
		this.io = io;
	}
	
	@Override
	public void write(int b) throws IOException {
		io.write((byte)b);
	}
	
	@Override
	public void write(byte[] bytes) throws IOException {
		for (byte b : bytes) {
			io.write(b);	
		}
		
	}

}
