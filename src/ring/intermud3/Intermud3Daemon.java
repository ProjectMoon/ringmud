package ring.intermud3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ring.daemons.Daemon;
import ring.deployer.DeployedMUDFactory;
import ring.players.Player;
import ring.server.MUDConnectionManager;
import ring.system.MUDConfig;

import com.aelfengard.i3.*;
import com.aelfengard.i3.packet.*;

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
	private static Map<Player, Intermud3Client> clientMap = Collections.synchronizedMap(new HashMap<Player, Intermud3Client>());
	
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
//		client.setHost(host);
//		client.setRouterName(router);
//		client.setPort(port);
        client.setRouterName("*i4");
        client.setHost("204.209.44.3");
        client.setPort(8080);
        client.addEventListener(new I3SystemEventListener());
        //client.addChannelListener("spam", new MyI3ChannelListener());
		//String mudName = DeployedMUDFactory.currentMUD().getName();
        
		String mudName = "RingMUD";
		client.setMudName(mudName);
		client.setAdminEmail("not-filled-out@nowhere.net");
		client.setMudType("RingMUD");
		
		try {
			client.connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws IOException {
		
	}
	
	protected I3Client getClient() {
		return client;
	}
		
	public Intermud3Client getClientForPlayer(Player p) {
		Intermud3Client cl = clientMap.get(p);
		
		if (cl == null) {
			cl = new Intermud3Client(p);
			clientMap.put(p, cl);
		}
		
		return cl;
	}
		
	public static void main(String[] args) throws IOException {
		MUDConfig.loadProperties();
		new Intermud3Daemon().start();
	}
}