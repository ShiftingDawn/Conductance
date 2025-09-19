package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public interface MaterialOreBearer {

	enum BlockType {
		DEFAULT, PILLAR
	}

	ResourceLocation getBearingBlockModel();

	MapColor getMapColor();

	SoundType getSoundType();

	BlockType getBlockType();

	boolean hasDoubleOutput();

	boolean hasGravity();
}
