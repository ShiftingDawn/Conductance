package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.registry.IRegistryObject;

public interface MaterialOreType extends IRegistryObject<ResourceLocation> {

	ResourceLocation getBearingBlockModel();

	String getUnlocalizedNameFactory();

	String getBearingStoneTagName();

	boolean hasDoubleOutput();

	boolean hasGravity();

	MapColor getMapColor();

	SoundType getSoundType();

	enum OreBlockType {
		DEFAULT, PILLAR
	}
}
