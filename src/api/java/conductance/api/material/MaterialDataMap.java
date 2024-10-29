package conductance.api.material;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface MaterialDataMap {

	int getMaterialColorRGB();

	int getMaterialColorARGB();

	MaterialTextureSet getTextureSet();

	long getProtons();

	long getNeutrons();

	long getMass();

	TagKey<Block> getBlockRequiredToolTag();

	int getBlockLightLevel();

	int getBurnTime();
}
