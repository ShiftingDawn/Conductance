package conductance.api.plugin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public interface MaterialOreTypeRegister {

	MaterialOreTypeBuilder register(String registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType);
}
