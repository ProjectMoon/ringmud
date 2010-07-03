package ring.mobiles.senses;

/**
 * Represents a processed depiction of a stimulus.
 * Returned from a sense's process() method.
 * @author projectmoon
 *
 */
public class ProcessedDepiction {
	private String depiction;
	
	public ProcessedDepiction() {}
	
	public ProcessedDepiction(String depiction) {
		setDepiction(depiction);
	}
	
	public String getDepiction() {
		return depiction;
	}
	
	public void setDepiction(String depiction) {
		this.depiction = depiction;
	}
}
