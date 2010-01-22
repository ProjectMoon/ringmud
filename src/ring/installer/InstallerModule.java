package ring.installer;

import ring.main.RingModule;

public class InstallerModule implements RingModule {
	private boolean dbSetupSuccess = false;

	@Override
	public void execute(String[] args) {
		if (args.length < 1) {
			System.out.println("Please specify a platform to install to.");
			return;
		}
		
		String installName = args[0];
		Installer installer = null;
		
		if (installName.equalsIgnoreCase("unix")) {
			installer = new UnixInstaller();
		}
		
		if (installer != null) {
			try {
				installer.createConfigDirectory();
				installer.copyDefaultConfig();
				dbSetupSuccess = installer.setUpDatabase();
				installer.finish();
			}
			catch (InstallationException e) {
				System.err.println();
				System.err.println("--------------------------------------");
				System.err.println("Installaton error:");
				System.err.println(e.getMessage());
				System.err.println();
				System.err.println("Aborting install.");
				System.err.println("--------------------------------------");
				System.exit(1);
			}
		}
		else {
			System.err.println(installName + " is not a recognized platform.");
		}
	}

	@Override
	public boolean usesDatabase() {
		return dbSetupSuccess;
	}

}
