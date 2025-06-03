package conductance.core.material;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialOreType;
import conductance.api.material.event.RegisterMaterialOreTypeEvent;

@AllArgsConstructor
//TODO refactor
final class RegisterMaterialOreTypeEventImpl implements RegisterMaterialOreTypeEvent {

	public interface MaterialOreTypeBuilderFactory {

		MaterialOreType apply(String registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType, Consumer<MaterialOreTypeBuilder> builder);
	}

	private final MaterialOreTypeBuilderFactory delegate;

	@Override
	public MaterialOreType register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType, final Consumer<MaterialOreTypeBuilder> builder) {
		return this.delegate.apply(registryName, bearingBlockModel, mapColor, soundType, builder);
	}

	@Override
	public MaterialOreType register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType) {
		return this.register(registryName, bearingBlockModel, mapColor, soundType, builder -> {
		});
	}
}
