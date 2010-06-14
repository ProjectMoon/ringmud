package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.stimuli.VisualStimulus;

public class VisualSense implements Sense<VisualStimulus> {
	private boolean disabledFromBlindness;
	private boolean disabledFromDarkness;
	private String name;
	
	@Override
	public ProcessedDepiction process(VisualStimulus stimulus) {
		if (!isDisabled()) {
			return new ProcessedDepiction(stimulus.getDepiction());
		}
		else {
			return new ProcessedDepiction(stimulus.getBlindDepiction());
		}
	}

	@Override
	public boolean isDisabled() {
		return (disabledFromBlindness || disabledFromDarkness);
	}
	
	public boolean disabledFromBindness() {
		return disabledFromBlindness;
	}

	public boolean disabledFromDarkness() {
		return disabledFromDarkness;
	}
	
	public void setDisabledFromBlindness(boolean value) {
		disabledFromBlindness = value;
	}
	
	public void setDisabledFromDarkness(boolean value) {
		disabledFromDarkness = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (disabledFromBlindness ? 1231 : 1237);
		result = prime * result + (disabledFromDarkness ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		VisualSense other = (VisualSense) obj;
		if (disabledFromBlindness != other.disabledFromBlindness)
			return false;
		if (disabledFromDarkness != other.disabledFromDarkness)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
