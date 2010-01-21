package ring.nrapi.players;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ring.nrapi.business.AbstractBusinessObject;
import ring.nrapi.data.DataStoreFactory;

@XmlRootElement
/**
 * Class representing a player and their collection of characters.
 * This class holds information such as last logon date, password,
 * etc.
 * @author projectmoon
 *
 */
public class Player extends AbstractBusinessObject {
	private String password;
	private Date lastLogon;
	private List<String> charIDs = new ArrayList<String>();
	
	@Override
	public void createChildRelationships() {}
	
	@XmlElement
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String pw) {
		password = pw;
	}
	
	@XmlElement
	public Date getLastLogon() {
		return lastLogon;
	}
	
	public void setLastLogon(Date lastLogon) {
		this.lastLogon = lastLogon;
	}
	
	@XmlElementWrapper(name = "characters")
	@XmlElement(name = "character")
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
}
