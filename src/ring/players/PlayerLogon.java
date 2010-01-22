package ring.players;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import ring.mobiles.Alignment;
import ring.mobiles.Mobile;
import ring.mobiles.MobileClass;
import ring.mobiles.MobileClassFactory;
import ring.mobiles.Race;
import ring.server.Server;
import ring.server.CommunicationException;
import ring.server.Communicator;
import ring.server.TelnetCommunicator;
import ring.world.World;

public class PlayerLogon extends Thread {

	// This socket is the player-server connection.
	private Socket socket;

	// A communicator to facilitate sending data back and forth
	// for this login session. This gets forwarded to the player thread.
	private Communicator comms;

	// The ever-ubiqutious logger.
	private static final Logger log = Logger.getLogger(PlayerLogon.class
			.getName());

	// Useful stuff.
	private boolean invalidLogin;
	private boolean waiting = true;
	private static String welcomeText = "\n\n[CYAN]                                   RingMUD Alpha[CYAN]\n                              A [WHITE]Forgotten Realms[CYAN] MUD.\n\n[WHITE]                                     Forgers:\n                                [CYAN] Ao (Code & Areas)\n                                 Rillifane (Code)\n                                 Istishia (Code)\n                                 Erevan (Code)\n                                 Fenmarel (Areas & RP)\n                                 Kelemvor (Admin)[WHITE]\n";
	private static String newsText = "\n[YELLOW]There is currently no news.[WHITE]\n";
	private World world;

	public PlayerLogon(Socket connection) {
		super();
		comms = new TelnetCommunicator(connection);
		comms.setSuffix("\n[R][GREEN]>[R]");
		socket = connection;
	}

	/**
	 * This method is where we wait for the player to log on. The player is
	 * first prompted if they want to create a new character. At this point they
	 * may type yes or no. If they choose yes, the method createNewCharacter()
	 * is called. In this method, all of the information is gathered for a new
	 * character. The information is then written to the player file.
	 * 
	 * Ifthey choose no, they go to the login screen.
	 */
	public void run() {
		//comms.setConnectCallback(new DefaultCallback());
		//comms.setDisconnectCallback(new DefaultCallback());
		//comms.getConnectCallback().execute(CallbackEvent.CONNECTED);
		PlayerCharacter enteringPlayer = null;

		// wait for log on.
		waiting = true;
		
		//Explicitly ignore screen width parsing so formatting for title
		//is preserved.
		comms.setScreenWidthParsing(false);
		comms.send(welcomeText + "\n[WHITE]Create new character? (Y/N)");
		comms.setScreenWidthParsing(true);

		while (waiting) {
			try {
				String answer = comms.receiveData();

				if (answer.toLowerCase().equals("y")) {
					enteringPlayer = createNewCharacter();
				} else if (answer.toLowerCase().equals("n")) {
					comms.send("Please type your name: ");
					String name = comms.receiveData();
					try {
						enteringPlayer = loadCharacter(name);
					} catch (IOException e) {
						enteringPlayer = null;
						// Need to log it, but we should be able to continue.
					}
				} else {
					comms.send("Please enter Y or N.");
				}

				// We should now have a player to load. If not, we start the
				// whole process again.
				if (enteringPlayer != null) {
					waiting = false;
					comms.setSuffix(enteringPlayer.getPrompt());
					//comms.setDisconnectCallback(new PlayerExitingCallback(enteringPlayer));
					Thread playerThread = new Thread(World.getWorld().getPlayerThreadGroup(), enteringPlayer, "Player ["
							+ enteringPlayer.getName() + "] ");
					playerThread.setDaemon(true);
					enteringPlayer.setThread(playerThread);
					playerThread.start();
					//Server.getPlayerList().addPlayer(enteringPlayer);
				}
			} catch (CommunicationException e) {
				waiting = false;
			}
		}

		if (comms.isCommunicationError()) {
			log.info("Logon connection dead; abandoning login.");
			//comms.getDisconnectCallback().execute(CallbackEvent.UNEXPECTED_QUIT);
		}
	}

	public Socket getConnection() {
		return socket;
	}

	public PlayerCharacter loadCharacter(String name) throws IOException,
			SocketException {
				/*
		comms.send("Please enter your password:");
		String pw = comms.receiveData();
		PlayerCharacter pc = (PlayerCharacter) MobileLoader.loadMobile(name
				+ ".mob");
		if (pc.getPassword().equals(pw)) {
			pc.setLastLogon();
			pc.setCommunicator(comms);
			return pc;
		} else {
			comms.send("Invalid password.");
			return null;
		}
		*/
		throw new UnsupportedOperationException();
	}

	public PlayerCharacter createNewCharacter() {
		try {
			comms
					.send("[RED]Entering new character creation mode...\n[WHITE]Please enter a name for this character:");

			String name = comms.receiveData();
			if (comms.isCommunicationError()) {
				log.severe("Error creating character; returning null");
				return null;
			}

			PlayerCharacter character = doCreateNewCharacter(name);
			return character;
		} catch (Exception e) {
			log.severe("Error creating character; returning null");
			return null;
		}
	}

	/**
	 * This method handles the actual creation of a new character given a name.
	 * The method is further broken down into several smaller helper methods so
	 * as to not clutter the code.
	 * 
	 * @param playerName
	 * @return A completed PlayerCharacter object.
	 * @throws java.io.IOException
	 *             If there is an error saving the player to a file.
	 */
	private PlayerCharacter doCreateNewCharacter(String playerName)
			throws IOException, SocketException {
		String password;
		int gender;
		Race race;
		Alignment alignment;
		MobileClass playerClass;
		PlayerCharacter newPlayer = new PlayerCharacter(comms, playerName);

		password = createPassword();

		race = chooseRace();
		comms.sendlnNoSuffix("[CYAN][B]Race chosen: [WHITE]" + race.getName()
				+ "[R]\n");

		gender = chooseGender(race);
		comms.sendlnNoSuffix("[CYAN][B]Gender chosen: [WHITE]" + gender
				+ "[R]\n");

		alignment = chooseAlignment();
		comms.sendlnNoSuffix("[CYAN][B]Alignment chosen: [WHITE]"
				+ alignment.getAlignmentString() + "[R]\n");

		playerClass = chooseClass();
		comms.sendlnNoSuffix("[CYAN][B]Class chosen: [WHITE]"
				+ playerClass.getDisplayName() + "[R]\n");

		System.out.println("Setting various player attributes...");
		// Set basic info
		newPlayer.setName(playerName);
		newPlayer.setPassword(password);
		newPlayer.setType(PlayerCharacter.MORTAL);
		newPlayer.setLongDescription("You see nothing special about "
				+ newPlayer.getHimHerIt() + ".");

		// Set some physical and alignment characteristics
		newPlayer.setRace(race);
		newPlayer.setBody(race.getBody());
		newPlayer.setSpeed(30);
		newPlayer.setGender(gender);
		newPlayer.setAlignment(alignment);

		// Set class, skills, etc.
		newPlayer.setMobileClass(playerClass);

		// Save the player and print a message
		newPlayer.setLastLogon();
		newPlayer.savePlayer();
		comms.sendlnNoSuffix("[CYAN]The [B][WHITE]" + newPlayer.getTypeString()
				+ "[R][CYAN] " + newPlayer.getRaceString() + " [R][WHITE]"
				+ newPlayer.getMobileClass().getDisplayName() + " "
				+ newPlayer.getName() + " [CYAN]has been created.\n");

		return newPlayer;
	}

	/**
	 * This method creates a valid password that the user will use to log in
	 * once their character is created. Valid passwords must be at least 6
	 * characters long, and has to be typed twice to validate it.
	 * 
	 * @return A valid password string.
	 */
	private String createPassword() throws IOException, SocketException {
		String password = null;
		String validatePassword = null;

		do {
			while (password == null || password.length() == 0) {
				comms.send("Enter a password for your character:");
				password = comms.receiveData();
				if (password.length() < 6) {
					comms
							.sendlnNoSuffix("[B][RED]Passwords must be at least six characters in length.[R][WHITE]");
					password = null;
				}
			}

			comms.send("Enter your password again for verification:");
			validatePassword = comms.receiveData();

			if (!password.equals(validatePassword)) {
				comms
						.send("[B][RED]First and second passwords do not match.[R][WHITE]");
				password = null;
			}
		} while (password == null);

		return password;
	}

	/**
	 * This method returns a race based on a letter choice the user inputs. It
	 * will print errors until the player chooses the right race.
	 * 
	 * @return The race chosen by the user in the form of a Race object.
	 */
	private Race chooseRace() throws IOException, SocketException {
		Race race = null;

		do {
			comms.sendlnNoSuffix("Please select a race:");
			comms
					.send("[R][CYAN]a) Human          [B][RED]g) Drow Elf[R][CYAN]\nb) Moon Elf       [B][RED]h) Ogre[R][CYAN]\nc) Dwarf          [B][RED]i) Duergar Dwarf[R][CYAN]\nd) Half-Elf       [B][RED]j) Illithid[R][CYAN]\ne) Gnome          [B][RED]k) Troll[R][CYAN]\nf) Aasimar        [B][RED]l) Tiefling[R][WHITE]");
			String choice = comms.receiveData();
			race = Race.determineRace(choice);

			if (race == null) {
				comms.sendlnNoSuffix("That is not a valid choice.");
			}
		} while (race == null);

		return race;
	}

	/**
	 * This helper method allows the user to choose their character's gender.
	 * The genders allowed are based on the race object passed to the method.
	 * 
	 * @param race
	 *            The player's chosen race determines what genders they can be.
	 * @return An integer representing the player's gender
	 *         (Male/female/asexual);
	 */
	private int chooseGender(Race race) throws IOException, SocketException {
		String raceName = race.getShortName();

		// Illithids are only asexual
		if (raceName.equals("Ill")) {
			return Mobile.IT; // Otherwise we move on to choosing M/F
		}
		String choice = "";
		int gender = -1; // 0 is male, 1 female, 2 asexual

		do {
			comms.send("Please enter a gender (M/F):");
			choice = comms.receiveData();
			if (choice.toLowerCase().equals("m")) {
				gender = Mobile.MALE;
			} else if (choice.toLowerCase().equals("f")) {
				gender = Mobile.FEMALE;
			} else {
				gender = -1;
			}
		} while (gender < 0);

		return gender;
	}

	/**
	 * This method asks the user to input an ethical and moral alignment in:
	 * Lawful Good, Chaotic Evil, etc.
	 * 
	 * @return The constructed alignment object.
	 */
	private Alignment chooseAlignment() throws IOException, SocketException {
		int ethical = -1;
		int moral = -1;
		String choice = "";

		do {
			comms.send("Please choose an ethical perspective (L, N, C):");
			choice = comms.receiveData();
			if (choice.toLowerCase().equals("l")) {
				ethical = Alignment.LAWFUL;
			} else if (choice.toLowerCase().equals("n")) {
				ethical = Alignment.NEUTRAL;
			} else if (choice.toLowerCase().equals("c")) {
				ethical = Alignment.CHAOTIC;
			}
		} while (ethical == -1);

		do {
			comms.send("Please input a moral perspective (G, N, E):");
			choice = comms.receiveData();
			if (choice.toLowerCase().equals("g")) {
				moral = Alignment.GOOD;
			} else if (choice.toLowerCase().equals("n")) {
				moral = Alignment.NEUTRAL;
			} else if (choice.toLowerCase().equals("e")) {
				moral = Alignment.EVIL;
			}
		} while (moral == -1);

		Alignment a = new Alignment(ethical, moral);
		return a;
	}

	/**
	 * This helper method allows the user to type in the name of the class they
	 * wish to play. It accepts full names (barbarian, sorcerer, etc).
	 * 
	 * @return The MobileClass object representing the chosen class.
	 */
	private MobileClass chooseClass() throws IOException, SocketException {
		String choice = "";
		MobileClass mc = null;

		do {
			comms
					.sendlnNoSuffix("Please choose from the following classes. Type in the full name to choose it.");
			comms
					.send("Barbarian, Cleric, Druid, Fighter, Monk, Paladin, Ranger, Rogue, Sorcerer, Wizard");
			choice = comms.receiveData();
			mc = MobileClassFactory.determineClass(choice);
		} while (mc == null);

		return mc;
	}

	// #############################################
	// #############################################
	// BEGIN BIG CLUNKY METHODS
	// These methods are big, hard to read, but very necessary.

	// getPlayerName method.
	// Used for logging in.
	public String getPlayerName() throws IOException, SocketException {
		String playerName = null;
		boolean playerActive;
		boolean invalidName;
		boolean found;
		String allowableCharacters = new String("abcdefghijklmnopqrstuvwxyz");

		do {
			invalidName = true;
			// ask for player name and log user on
			while (invalidName) {
				comms
						.send("[RED]Skipping new character creation...[WHITE]\nPlease enter your character's name:");
				playerName = comms.receiveData();
				invalidName = false;
				if (playerName.length() > 15 || playerName.length() < 1) {
					invalidName = true;
					comms
							.send("[RED]Sorry, character names must be between 1 and 15 characters long.[WHITE]\n");
				} else {
					for (int x = 0; x < playerName.length(); x++) {
						found = false;
						for (int y = 0; y < allowableCharacters.length(); y++) {
							if (playerName.substring(x, x + 1)
									.equalsIgnoreCase(
											allowableCharacters.substring(y,
													y + 1))) {
								found = true;
								break;
							}
						}
						if (!found) {
							comms
									.send("[RED]Sorry, only the uppercase and lower case letters A-Z are allowed in a character name.[WHITE]\n");
							invalidName = true;
							break;
						}
					}
				}
			}

			System.out.println("Player [" + getConnection().getInetAddress()
					+ "] logged on as " + playerName);

			// TODO: check both active and inactive players by querying the
			// player store.
			playerActive = false;
			/*
			List<PlayerCharacter> currentPlayers = Server.getPlayerList().getPlayers();
			for (PlayerCharacter player : currentPlayers) {
				if (player.checkAlias(playerName.toUpperCase())) {
					playerActive = true;
					break;
				}
			}
			*/

			if (playerActive) {
				comms
						.send("[RED]Sorry, that character is already in use.\n[CYAN]Note: If the character was in use by you and you dropped out of the game abnormally please wait 30-60 seconds for the character to be released and try logging on again.[WHITE]");
			}
		} while (playerActive);

		return playerName;
	}
}
