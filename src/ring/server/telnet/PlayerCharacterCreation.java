package ring.server.telnet;

import java.io.IOException;
import java.net.SocketException;

import ring.mobiles.Alignment;
import ring.mobiles.MobileBaseModel;
import ring.mobiles.RaceFactory;
import ring.mobiles.Alignment.Ethical;
import ring.mobiles.Alignment.Moral;
import ring.mobiles.MobileBaseModel.Gender;
import ring.mobiles.mobclass.MobileClass;
import ring.mobiles.Race;
import ring.players.PlayerCharacter;
import ring.server.Communicator;

/**
 * Class for creating a player character.
 * 
 * @author projectmoon
 * 
 */
public class PlayerCharacterCreation {
	private Communicator comms;
	
	public PlayerCharacterCreation(Communicator comms) {
		this.comms = comms;
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
	public PlayerCharacter doCreateNewCharacter(String playerName) {
					
		String password;
		Gender gender;
		Race race;
		Alignment alignment;
		MobileClass playerClass;
		PlayerCharacter newPlayer = new PlayerCharacter(comms, playerName);

		password = createPassword();

		race = chooseRace();
		comms.printlnNoSuffix("[CYAN][B]Race chosen: [WHITE]" + race.getName()
				+ "[R]\n");

		gender = chooseGender(race);
		comms.printlnNoSuffix("[CYAN][B]Gender chosen: [WHITE]" + gender + "[R]\n");

		alignment = chooseAlignment();
		comms.printlnNoSuffix("[CYAN][B]Alignment chosen: [WHITE]" + alignment.toString() + "[R]\n");

		playerClass = chooseClass();
		//comms.printlnNoSuffix("[CYAN][B]Class chosen: [WHITE]" + playerClass.getDisplayName() + "[R]\n");

		System.out.println("Setting various player attributes...");
		// Set basic info
		newPlayer.getBaseModel().setName(playerName);
		newPlayer.setPassword(password);
		newPlayer.getBaseModel().setType(MobileBaseModel.Type.MORTAL);
		//newPlayer.setMobileClass goes here.

		// Set some physical and alignment characteristics
		newPlayer.getBaseModel().setRace(race);
		newPlayer.getBaseModel().setBody(race.getBody());
		newPlayer.getDynamicModel().setSpeed(30);
		newPlayer.getBaseModel().setGender(gender);
		newPlayer.getBaseModel().setAlignment(alignment);
		newPlayer.getBaseModel().setDescription("You see nothing special about " + newPlayer.getBaseModel().getGender().getObject() + ".");
		
		// Set class, skills, etc.
		newPlayer.getBaseModel().setMobileClass(playerClass);

		// Save the player and print a message
		comms.printlnNoSuffix("[CYAN]The [B][WHITE]"
				+ newPlayer.getBaseModel().getType().getName() + "[R][CYAN] "
				+ newPlayer.getBaseModel().getRace().getName() + " [R][WHITE]"
				//+ newPlayer.getBaseModel().getMobileClass().getDisplayName() + " "
				+ newPlayer.getBaseModel().getName() + " [CYAN]has been created.\n");

		return newPlayer;
	}

	/**
	 * This method creates a valid password that the user will use to log in
	 * once their character is created. Valid passwords must be at least 6
	 * characters long, and has to be typed twice to validate it.
	 * 
	 * @return A valid password string.
	 */
	public String createPassword() {
		String password = null;
		String validatePassword = null;

		do {
			while (password == null || password.length() == 0) {
				comms.print("Enter a password for your character: ");
				password = comms.receiveData();
				
				if (password.length() < 6) {
					comms.printlnNoSuffix("[B][RED]Passwords must be at least six characters in length.[R][WHITE]");
					password = null;
				}
			}

			comms.print("Enter your password again for verification: ");
			validatePassword = comms.receiveData();
			comms.println();

			if (!password.equals(validatePassword)) {
				comms.printlnNoSuffix("[B][RED]First and second passwords do not match.[R][WHITE]");
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
	public Race chooseRace() {
		Race race = null;

		do {
			comms.printlnNoSuffix("Please select a race:");
			comms.println("[R][CYAN]a) Human          [B][RED]g) Drow Elf[R][CYAN]\nb) Moon Elf       [B][RED]h) Ogre[R][CYAN]\nc) Dwarf          [B][RED]i) Duergar Dwarf[R][CYAN]\nd) Half-Elf       [B][RED]j) Illithid[R][CYAN]\ne) Gnome          [B][RED]k) Troll[R][CYAN]\nf) Aasimar        [B][RED]l) Tiefling[R][WHITE]");
			String choice = comms.receiveData();
			race = RaceFactory.determineRace(choice);

			if (race == null) {
				comms.printlnNoSuffix("That is not a valid choice.");
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
	public Gender chooseGender(Race race) {
		String raceName = race.getShortName();

		// Illithids are only asexual
		if (raceName.equals("Ill")) {
			return Gender.IT; // Otherwise we move on to choosing M/F
		}
		String choice = "";
		Gender gender = null;

		do {
			comms.print("Please enter a gender (M/F): ");
			choice = comms.receiveData();
					
			if (choice.toLowerCase().equals("m")) {
				gender = Gender.MALE;
			} else if (choice.toLowerCase().equals("f")) {
				gender = Gender.FEMALE;
			}
		} while (gender == null);

		return gender;
	}

	/**
	 * This method asks the user to input an ethical and moral alignment in:
	 * Lawful Good, Chaotic Evil, etc.
	 * 
	 * @return The constructed alignment object.
	 */
	public Alignment chooseAlignment() {
		Ethical ethical = null;
		Moral moral = null;
		String choice = "";

		do {
			comms.print("Please choose an ethical perspective (L, N, C): ");
			choice = comms.receiveData();
						
			if (choice.toLowerCase().equals("l")) {
				ethical = Ethical.LAWFUL;
			} else if (choice.toLowerCase().equals("n")) {
				ethical = Ethical.NEUTRAL;
			} else if (choice.toLowerCase().equals("c")) {
				ethical = Ethical.CHAOTIC;
			}
		} while (ethical == null);

		do {
			comms.print("Please input a moral perspective (G, N, E): ");
			choice = comms.receiveData();
						
			if (choice.toLowerCase().equals("g")) {
				moral = Moral.GOOD;
			} else if (choice.toLowerCase().equals("n")) {
				moral = Moral.NEUTRAL;
			} else if (choice.toLowerCase().equals("e")) {
				moral = Moral.EVIL;
			}
		} while (moral == null);

		Alignment a = new Alignment(ethical, moral);
		return a;
	}

	/**
	 * This helper method allows the user to type in the name of the class they
	 * wish to play. It accepts full names (barbarian, sorcerer, etc).
	 * 
	 * @return The MobileClass object representing the chosen class.
	 */
	public MobileClass chooseClass() {
		/*
		String choice = "";
		MobileClass mc = null;

		do {
			comms
					.printlnNoSuffix("Please choose from the following classes. Type in the full name to choose it.");
			comms
					.print("Barbarian, Cleric, Druid, Fighter, Monk, Paladin, Ranger, Rogue, Sorcerer, Wizard");
			choice = comms.receiveData();
			mc = MobileClassFactory.determineClass(choice);
		} while (mc == null);

		return mc;
		*/
		comms.println("Class choosing not implemented yet");
		return null;
	}

	public String getPlayerName() {
		String playerName = null;
		boolean playerActive;
		boolean invalidName;
		boolean found;
		String allowableCharacters = new String("abcdefghijklmnopqrstuvwxyz");

		do {
			invalidName = true;
			// ask for player name and log user on
			while (invalidName) {
				comms.print("[RED]Skipping new character creation...[WHITE]\nPlease enter your character's name: ");
				playerName = comms.receiveData();
				
				invalidName = false;
				if (playerName.length() > 15 || playerName.length() < 1) {
					invalidName = true;
					comms.print("[RED]Sorry, character names must be between 1 and 15 characters long.[WHITE]\n");
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
							comms.print("[RED]Sorry, only the uppercase and lower case letters A-Z are allowed in a character name.[WHITE]\n");
							invalidName = true;
							break;
						}
					}
				}
			}

			// TODO: check both active and inactive players by querying the
			// player store.
			playerActive = false;
			/*
			 * List<PlayerCharacter> currentPlayers =
			 * Server.getPlayerList().getPlayers(); for (PlayerCharacter player
			 * : currentPlayers) { if
			 * (player.checkAlias(playerName.toUpperCase())) { playerActive =
			 * true; break; } }
			 */

			if (playerActive) {
				comms.print("[RED]Sorry, that character is already in use.\n[CYAN]Note: If the character was in use by you and you dropped out of the game abnormally please wait 30-60 seconds for the character to be released and try logging on again.[WHITE]");
			}
		} while (playerActive);

		return playerName;
	}
}
