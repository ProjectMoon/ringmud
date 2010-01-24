package ring.server.telnet;

import java.io.IOException;
import java.io.InputStream;

import net.wimpi.telnetd.io.BasicTerminalIO;

public class TelnetInputStream extends InputStream {
	private BasicTerminalIO io;
	private boolean echo = true;
	
	public TelnetInputStream(BasicTerminalIO io) {
		this.io = io;
	}
	
	@Override
	public int read() throws IOException {
		int i = io.read();
		
		if (echo) {
			io.write((char)i);
		}
		
		if ((char)i == '\n') {
			System.out.println("Received newline input");
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
	
	public boolean getEcho() {
		return echo;
	}
	
	public void setEcho(boolean echo) {
		this.echo = echo;
	}

}
