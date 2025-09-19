package conductance.core.material;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.event.MaterialOreBearerBuilder;
import conductance.api.material.event.RegisterMaterialOreBearerEvent;

@RequiredArgsConstructor
final class RegisterMaterialOreBearerEventImpl implements RegisterMaterialOreBearerEvent {

	interface Delegate {
		MaterialOreBearer apply(String registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType, @Nullable Consumer<MaterialOreBearerBuilder> builder);
	}

	private final Delegate delegate;

	@Override
	public MaterialOreBearer register(final String registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType, @Nullable final Consumer<MaterialOreBearerBuilder> builder) {
		return this.delegate.apply(registryName, bearingBlockModel, mapColor, soundType, builder);
	}
}
