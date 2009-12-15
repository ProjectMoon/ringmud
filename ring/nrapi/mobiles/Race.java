package ring.mobiles;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Race implements Serializable {
	public static final long serialVersionUID = 1;
    private int strMod;
    private int dexMod;
    private int conMod;
    private int intMod;
    private int wisMod;
    private int chaMod;

	//Text to display when showing the race of a mobile.
	private String raceName;

	//Default Body of the race.
	private Body raceBody;

	//Classes this race can play.
	private List<MobileClass> classesAllowed = new ArrayList<MobileClass>();

	//Miscellaneous flags.
	//Is this race playable by PCs?
	private boolean isPCRace;

	//This will create a default race with relatives of all 0.
	public Race() {

	}

	//getName method.
  	//Returns the full name of the race.
  	public String getName() {
	    return raceName;
  	}
  	
  	public void setName(String name) {
		raceName = name;
	}

	//getShortName method.
	//Returns the first three letters of the race name.
	public String getShortName() {
		int startPos = raceName.lastIndexOf("]", raceName.length() - 10) + 1;
		return raceName.substring(startPos, startPos + 3);
	}

	//getBody method.
	//Returns the Body object of this Race object.
	public Body getBody() {
		return raceBody;
	}
	
	public void setBody(Body body) {
		raceBody = body;
	}

	//getClassesAllowed method.
	//Returns the available classes this race can have.
	public List<MobileClass> getClassesAllowed() {
		return classesAllowed;
	}

	//isPCRace method.
	//Returns if the race is available to players or not.
	public boolean isPCRace() {
		return isPCRace;
	}
	
	public void setPCRace(boolean val) {
		isPCRace = val;
	}
	
	public int getStrModifier() {
		return strMod;
	}
	
	public int getDexModifier() {
		return dexMod;
	}
	
	public int getConModifier() {
		return conMod;
	}
	
	public int getIntModifier() {
		return intMod;
	}
	
	public int getWisModifier() {
		return wisMod;
	}
	
	public int getChaModifier() {
		return chaMod;
	}
	
	public void setStrModifier(int mod) {
		strMod = mod;
	}

	public void setDexModifier(int mod) {
		dexMod = mod;
	}

	public void setConModifier(int mod) {
		conMod = mod;
	}
	
	public void setIntModifier(int mod) {
		intMod = mod;
	}
	
	public void setWisModifier(int mod) {
		wisMod = mod;
	}
	
	public void setChaModifier(int mod) {
		chaMod = mod;
	}
}
