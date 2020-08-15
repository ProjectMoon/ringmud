package ring.magic.vancian;

import ring.magic.SpellMetadata;
import ring.magic.SpellMetadataImplementation;

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
