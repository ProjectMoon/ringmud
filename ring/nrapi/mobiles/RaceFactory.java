package ring.nrapi.mobiles;

public class RaceFactory {
	//determineRace method.
	//This method is used in character creation to create a PC race.
	public static Race determineRace(String choice) {
		choice = choice.toUpperCase();
		if(choice.equalsIgnoreCase("A")) return createHuman();
		
		else if(choice.equalsIgnoreCase("B")) return createElf();
		
		else if(choice.equalsIgnoreCase("C")) return createDwarf();
		
		else if(choice.equalsIgnoreCase("D")) return createHalfElf();
		
		else if(choice.equalsIgnoreCase("E")) return createGnome();
		
		else if(choice.equalsIgnoreCase("F")) return createAasimar();
		
		else if(choice.equalsIgnoreCase("G")) return createDrow();
		
		else if(choice.equalsIgnoreCase("H")) return createOgre();

		else if(choice.equalsIgnoreCase("I")) return createDuergar();

		else if(choice.equalsIgnoreCase("J")) return createIllithid();

		else if(choice.equalsIgnoreCase("K")) return createTroll();

		else if(choice.equalsIgnoreCase("L")) return createTiefling();

		else return null;
	}
	
	//createHuman method.
	//This returns the Human PC race.
	public static Race createHuman() {
		Race r = new Race();
		r.setName("[B][YELLOW]Human[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createDrow method.
	//This returns the Drow PC race.
	public static Race createDrow() {
		Race r = new Race();
		r.setName("[MAGENTA]Drow[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createElf method.
	//This returns the Elf PC race.
	public static Race createElf() {
		Race r = new Race();
		r.setName("[CYAN]Moon Elf[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createOgre method.
	//This returns the Ogre PC race.
	public static Race createOgre() {
		Race r = new Race();
		r.setName("[B][BLUE]Ogre[R][WHITE]");
		r.setBody(BodyFactory.createLargeHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createDwarf method.
	//This returns the Dwarf PC race.
	public static Race createDwarf() {
		Race r = new Race();
		r.setName("[B][GREEN]Dwarf[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createDuergar method.
	//This returns the Duergar PC race.
	public static Race createDuergar() {
		Race r = new Race();
		r.setName("[RED]Duergar[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createHalfElf method.
	//This returns the Half-Elf PC race.
	public static Race createHalfElf() {
		Race r = new Race();
		r.setName("[GREEN]Half-Elf[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createIllithid method.
	//This method returns the Illithid PC race.
	public static Race createIllithid() {
		Race r = new Race();
		r.setName("[B][MAGENTA]Illithid[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createGnome method.
	//This method returns the Gnome PC race.
	//Tiefling: Str +0, Con +0, Dex +15, Int +10, Wis +0, Agi +10, Cha -15
	public static Race createGnome() {
		Race r = new Race();
		r.setName("[B][RED]Gnome[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createTroll method.
	//This method returns the Troll PC race.
	public static Race createTroll() {
		Race r = new Race();
		r.setName("[GREEN]Troll[R][WHITE]");
		r.setBody(BodyFactory.createLargeHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createAasimar method.
	//This method returns the Aasimar PC race.
	public static Race createAasimar() {
		Race r = new Race();
		r.setName("[B][WHITE]Aasimar[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}

	//createTiefling method.
	//This method returns the Tiefling PC race.
	public static Race createTiefling() {
		Race r = new Race();
		r.setName("[R][YELLOW]Tiefling[R][WHITE]");
		r.setBody(BodyFactory.createMediumHumanoidBody());
		r.setClassesAllowed(null);
		r.setPCRace(true);
		return r;
	}
}
