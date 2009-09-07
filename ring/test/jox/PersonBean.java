package ring.test.jox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonBean implements Serializable {
	public static final long serialVersionUID = 1;
	
	private String name;
	private List<AddressBean> address;
	
	public PersonBean() {
		address = new ArrayList<AddressBean>();
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(AddressBean[] addresses) {
		System.out.println("sup");
		address = new ArrayList<AddressBean>();
		for (AddressBean a : addresses) {
			address.add(a);
		}
	}
	
	public void setAddress(int i, AddressBean addr) {
		address.set(i, addr);
	}
	
	public AddressBean[] getAddress() {
		return address.toArray(new AddressBean[0]);
	}
	
	public AddressBean getAddress(int i) {
		return address.get(i);
	}
	
	public String toString() {
		String addr = "";
		for (AddressBean a : address) {
			addr += "[" + a.toString() + "]";
		}
		return name + " (" + addr + ")";
	}
}
