package ring.server.telnet;

import java.io.IOException;
import java.io.OutputStream;

import net.wimpi.telnetd.io.BasicTerminalIO;

public class TelnetOutputStream extends OutputStream {
	private BasicTerminalIO io;
	
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
