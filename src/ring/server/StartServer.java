package ring.server;

import java.io.IOException;

import ring.main.RingModule;

public class StartServer implements RingModule {

	@Override
	public void execute(String[] args) {
		//Only start telnet for now
		Server server = new TelnetServer();
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean usesDatabase() {
		return false;
	}

}
