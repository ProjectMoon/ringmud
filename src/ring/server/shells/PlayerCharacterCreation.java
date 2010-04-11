package ring.server.shells;

import ring.comms.Communicator;
import ring.mobiles.Alignment;
import ring.mobiles.MobileBaseModel;
import ring.mobiles.Race;
import ring.mobiles.RaceFactory;
import ring.mobiles.Alignment.Ethical;
import ring.mobiles.Alignment.Moral;
import ring.mobiles.MobileBaseModel.Gender;
import ring.mobiles.mobclass.MobileClass;
import ring.persistence.DataStoreFactory;
import ring.players.PlayerCharacter;

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
	public PlayerCharacter doCreateNewCharacter() {
		String name;
		Gender gender;
		Race race;
		Alignment alignment;
		MobileClass playerClass;
		PlayerCharacter newPlayer = new PlayerCharacter();
		
		name = chooseName();
		comms.printlnNoSuffix("[CYAN][B]Name chosen: [WHITE]" + name);
		comms.println();

		race = chooseRace();
		comms.printlnNoSuffix("[CYAN][B]Race chosen: [WHITE]" + race.getName());
		comms.println();
		
		gender = chooseGender(race);
		comms.printlnNoSuffix("[CYAN][B]Gender chosen: [WHITE]" + gender);
		comms.println();

		alignment = chooseAlignment();
		comms.printlnNoSuffix("[CYAN][B]Alignment chosen: [WHITE]" + alignment.toString());
		comms.println();

		playerClass = chooseClass();
		//comms.printlnNoSuffix("[CYAN][B]Class chosen: [WHITE]" + playerClass.getDisplayName() + "[R]\n");
		comms.println();

		System.out.println("Setting various player attributes...");
		// Set basic info
		newPlayer.getBaseModel().setName(name);
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
			comms.print("Enter choice: ");
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

	public String chooseName() {
		String playerName = null;
		String allowableCharacters = "abcdefghijklmnopqrstuvwxyz";

		//This loop runs forever until a user creates a name that meets the following:
		//2 - 15 characters long.
		//Letters only.
		//The validation conditions use continue to iterate the loop again.
		//A break is at the very end to kill the loop if the name is valid.
		while (true) {
			comms.print("Enter a character name: ");
			playerName = comms.receiveData();

			if (playerName.length() > 15 || playerName.length() < 2) {
				comms.println("[RED]Sorry, character names must be between 2 and 15 characters long.");
				continue;
			}
			else {
				for (char ch : playerName.toCharArray()) {
					if (allowableCharacters.indexOf(ch) < 0) {
						comms.println("[RED]Character names must contain letters only.");
						continue;
					}
				}
			}

			PlayerCharacter pc = DataStoreFactory.getDefaultStore().retrievePlayerCharacter(playerName);
			if (pc != null) {
				comms.println("[RED]Sorry, that character name is already in use.");
				continue;
			}
			
			//The name is valid at this point. End the loop.
			break;
		}
	
		//Capitalize first letter of name.
		playerName = playerName.substring(0, 1).toUpperCase() + playerName.substring(1);
		return playerName;
	}
}
