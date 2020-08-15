package ring.mobiles;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import ring.mobiles.mobclass.MobileClass;

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

	public void setClassesAllowed(List<MobileClass> classesAllowed) {
		this.classesAllowed = classesAllowed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chaMod;
		result = prime * result
				+ ((classesAllowed == null) ? 0 : classesAllowed.hashCode());
		result = prime * result + conMod;
		result = prime * result + dexMod;
		result = prime * result + intMod;
		result = prime * result + (isPCRace ? 1231 : 1237);
		result = prime * result
				+ ((raceBody == null) ? 0 : raceBody.hashCode());
		result = prime * result
				+ ((raceName == null) ? 0 : raceName.hashCode());
		result = prime * result + strMod;
		result = prime * result + wisMod;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Race other = (Race) obj;
		if (chaMod != other.chaMod)
			return false;
		if (classesAllowed == null) {
			if (other.classesAllowed != null)
				return false;
		} else if (!classesAllowed.equals(other.classesAllowed))
			return false;
		if (conMod != other.conMod)
			return false;
		if (dexMod != other.dexMod)
			return false;
		if (intMod != other.intMod)
			return false;
		if (isPCRace != other.isPCRace)
			return false;
		if (raceBody == null) {
			if (other.raceBody != null)
				return false;
		} else if (!raceBody.equals(other.raceBody))
			return false;
		if (raceName == null) {
			if (other.raceName != null)
				return false;
		} else if (!raceName.equals(other.raceName))
			return false;
		if (strMod != other.strMod)
			return false;
		if (wisMod != other.wisMod)
			return false;
		return true;
	}
}
