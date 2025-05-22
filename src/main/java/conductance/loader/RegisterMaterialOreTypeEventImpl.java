package conductance.loader;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.RegisterMaterialOreTypeEvent;
import conductance.core.apiimpl.MaterialOreTypeBuilderImpl;

@AllArgsConstructor
//TODO refactor
final class RegisterMaterialOreTypeEventImpl implements RegisterMaterialOreTypeEvent {

	public interface MaterialOreTypeBuilderFactory {

		MaterialOreTypeBuilderImpl create(ResourceLocation registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType);
	}

	private final String modid;
	private final MaterialOreTypeBuilderFactory delegate;

	@Override
	public MaterialOreType register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType, final Consumer<MaterialOreTypeBuilder> builder) {
		final MaterialOreTypeBuilderImpl materialOreTypeBuilder = this.delegate.create(ResourceLocation.fromNamespaceAndPath(this.modid, registryName), bearingBlockModel, mapColor, soundType);
		builder.accept(materialOreTypeBuilder);
		return materialOreTypeBuilder.build();
	}

	@Override
	public MaterialOreType register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType) {
		return this.register(registryName, bearingBlockModel, mapColor, soundType, builder -> {
		});
	}
}
