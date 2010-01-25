package ring.server.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.TelnetD;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import ring.server.Server;
import ring.system.MUDConfig;

public class TelnetServer implements Server {
	private TelnetD telnet;
	
	@Override
	public void start() throws IOException {
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("ring/server/resources/telnet.properties");
		props.load(input);
		
		//Replace whatever port is defined in the regular telnet properties with the one from mud.config
		props.setProperty("std.port", String.valueOf(MUDConfig.getServerPort()));
		
		Logger root = Logger.getRootLogger();
		root.addAppender(new ConsoleAppender(
		    new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
		
		try {
			telnet = TelnetD.createTelnetD(props);
			telnet.start();
		}
		catch (BootException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		telnet.stop();
		
	}

}
