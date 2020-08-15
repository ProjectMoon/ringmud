package ring.players;

import ring.nrapi.business.BusinessObject;
import ring.persistence.DataStoreFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing a player and their collection of characters.
 * This class holds information such as last logon date, password,
 * etc.
 * @author projectmoon
 *
 */
public class Player extends BusinessObject {
	private String name;
	private String password;
	private Date lastLogon;
	private List<String> charIDs = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String pw) {
		password = pw;
	}
	
	public Date getLastLogon() {
		return lastLogon;
	}
	
	public void setLastLogon(Date lastLogon) {
		this.lastLogon = lastLogon;
	}
	
	public List<String> getCharacterIDs() {
		return charIDs;
	}
	
	public void setCharacterIDs(List<String> ids) {
		this.charIDs = ids;
	}
	
	public PlayerCharacter getCharacter(String name) {		
		return DataStoreFactory.getDefaultStore().retrievePlayerCharacter(name);
	}
	
	public void addCharacter(PlayerCharacter pc) {
		charIDs.add(pc.getID());
	}
	
	public boolean removeCharacter(PlayerCharacter pc) {
		return charIDs.remove(pc.getID());
	}

	public void addCharacter(String name) {
		charIDs.add(name);
	}
	
	public boolean removeCharacter(String name) {
		return charIDs.remove(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((charIDs == null) ? 0 : charIDs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (charIDs == null) {
			if (other.charIDs != null)
				return false;
		} else if (!charIDs.equals(other.charIDs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}	
}
