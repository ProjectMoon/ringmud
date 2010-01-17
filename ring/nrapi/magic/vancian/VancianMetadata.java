package ring.nrapi.magic.vancian;

import ring.nrapi.magic.SpellMetadata;
import ring.nrapi.magic.SpellMetadataImplementation;

public class VancianMetadata implements SpellMetadataImplementation {
	private int level;
	
	@Override
	public void transform(SpellMetadata metadata) {
		level = metadata.getAsInt("level");
	}
	
	public int getSpellLevel() {
		return level;
	}

}
