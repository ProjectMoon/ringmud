package ring.test.jox;

import java.io.Serializable;

public class PersonBean implements Serializable {
	public static final long serialVersionUID = 1;
	
	private String name;
	private AddressBean address;
	
	public PersonBean() {
		
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(AddressBean addr) {
		address = addr;
	}
	
	public AddressBean getAddress() {
		return address;
	}
	
	public String toString() {
		return name + " (" + address.toString() + ")";
	}
}
