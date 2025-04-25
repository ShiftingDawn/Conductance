package conductance.api.plugin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public interface MaterialOreTypeRegister {

	MaterialOreTypeBuilder register(String registryName, ResourceLocation bearingBlockModel, String unlocalizedNameFactory, String bearingStoneTagName, MapColor mapColor, SoundType soundType);

	default MaterialOreTypeBuilder register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType) {
		return this.register(registryName, bearingBlockModel, registryName + "_%s_ore", registryName, mapColor, soundType);
	}
}
