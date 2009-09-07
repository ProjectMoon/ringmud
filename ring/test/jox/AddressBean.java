package ring.test.jox;

public class AddressBean implements java.io.Serializable {
	public static final long serialVersionUID = 1;
	
	private String addressLine1;
	private String addressLine2;
	
	public AddressBean() {}
	
	public void setLine1(String line) {
		addressLine1 = line;
	}
	
	public String getLine1() {
		return addressLine1;
	}
	
	public void setLine2(String line) {
		addressLine2 = line;
	}
	
	public String getLine2() {
		return addressLine2;
	}
	
	public String toString() {
		return addressLine1 + "; " + addressLine2;
	}
}
