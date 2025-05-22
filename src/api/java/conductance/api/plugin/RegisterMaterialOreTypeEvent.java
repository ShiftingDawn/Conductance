package conductance.api.plugin;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreType;

public interface RegisterMaterialOreTypeEvent extends IConductancePluginEvent {

	interface MaterialOreTypeBuilder {

		MaterialOreTypeBuilder blockType(MaterialOreType.OreBlockType blockType);

		MaterialOreTypeBuilder doubleOutput();

		MaterialOreTypeBuilder hasGravity();
	}

	MaterialOreType register(String registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType, Consumer<MaterialOreTypeBuilder> builder);

	default MaterialOreType register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType) {
		return this.register(registryName, bearingBlockModel, mapColor, soundType);
	}
}
