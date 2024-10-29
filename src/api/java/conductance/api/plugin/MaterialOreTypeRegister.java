package conductance.api.plugin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreType;

public interface MaterialOreTypeRegister {

	MaterialOreType register(String registryName, MaterialOreType.OreBlockType blockType, ResourceLocation bearingBlockModel, String unlocalizedNameFactory, String bearingStoneTagName, boolean hasDoubleOutput,
			boolean hasGravity, MapColor mapColor, SoundType soundType);

	default MaterialOreType register(String registryName, ResourceLocation bearingBlockModel, boolean hasDoubleOutput, boolean hasGravity, MapColor mapColor, SoundType soundType) {
		return this.register(registryName, MaterialOreType.OreBlockType.DEFAULT, bearingBlockModel, registryName + "_%s_ore", registryName, hasDoubleOutput, hasGravity, mapColor, soundType);
	}
}
