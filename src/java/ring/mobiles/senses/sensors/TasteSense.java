package ring.mobiles.senses.sensors;

import ring.mobiles.senses.ProcessedDepiction;
import ring.mobiles.senses.stimuli.TasteStimulus;

public class TasteSense implements Sense<TasteStimulus> {
	private String name;
		
	@Override
	public ProcessedDepiction process(TasteStimulus stimulus) {
		if (!isDisabled()) {
			return new ProcessedDepiction(stimulus.getDepiction());
		}
		else {
			return new ProcessedDepiction("Your " + getName() + " can't taste anything!");
		}
	}

	@Override
	public boolean isDisabled() {
		return false;
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
		TasteSense other = (TasteSense) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
