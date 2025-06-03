package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.registry.IRegistryObject;

public interface MaterialOreType extends IRegistryObject<ResourceLocation> {

	OreBlockType getOreBlockType();

	ResourceLocation getBearingBlockModel();

	boolean hasDoubleOutput();

	boolean hasGravity();

	MapColor getMapColor();

	SoundType getSoundType();

	TagKey<Block> getRequiredToolTypeTag();

	enum OreBlockType {
		DEFAULT, PILLAR
	}
}
