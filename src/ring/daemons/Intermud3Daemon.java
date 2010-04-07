package ring.daemons;

import java.io.IOException;
import java.util.Properties;

import ring.system.MUDConfig;

import com.aelfengard.i3.I3Client;

/**
 * A daemon for connecting RingMUD to Intermud3 chat networks.
 * This daemon is started by the MUD server, and pulls its
 * information from mud.config.
 * @author projectmoon
 *
 */
public class Intermud3Daemon implements Daemon {
	private static Intermud3Daemon daemon;
	private I3Client client;
	
	public static Intermud3Daemon currentDaemon() {
		return daemon;
	}
	
	@Override
	public void halt() throws IOException {
		
	}

	@Override
	public void start() throws IOException {
		daemon = this;
		Properties i3props = MUDConfig.getPluginProperties("i3");
		
		String host = i3props.getProperty("i3.host");
		String router = i3props.getProperty("i3.router");
		int port = Integer.parseInt(i3props.getProperty("i3.port"));
		
		client = new I3Client();
		client.setHost(host);
		client.setRouterName(router);
		client.setPort(port);
		client.connect();
		
		initMUDInfo();
	}

	@Override
	public void stop() throws IOException {
		
	}
	
	public I3Client getClient() {
		return client;
	}
	
	private void initMUDInfo() {
		client.setMudName("");
		client.setMudType("RingMUD");
	}
	
	

}
