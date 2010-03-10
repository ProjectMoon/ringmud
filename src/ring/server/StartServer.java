package ring.server;

import java.io.IOException;

import ring.deployer.DeployedMUD;
import ring.deployer.DeployedMUDFactory;
import ring.main.RingModule;
import ring.persistence.ExistDB;
import ring.server.telnet.TelnetServer;
import ring.system.MUDBoot;

public class StartServer implements RingModule {

	@Override
	public void execute(String[] args) {
		//Discover the mud we are to run.
		DeployedMUD mud = DeployedMUDFactory.getMUD(args[0]);
		if (mud != null) {
			ExistDB.setRootURI(mud.getName());
			
			//Boot the mud
			MUDBoot.boot();
			
			//Only start telnet for now
			Server server = new TelnetServer();
			try {
				server.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Couldn't find a deployed MUD named \"" + args[0] + "\"");
		}
	}

	@Override
	public boolean usesDatabase() {
		return false;
	}

}
