package conductance.core.material;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;

@RequiredArgsConstructor
final class RegisterMaterialGenerationHandlerEventImpl implements RegisterMaterialGenerationHandlerEvent {

	interface Delegate {
		MaterialGenerationHandler apply(String registryName, Function<Material, String> unlocalizedNameFactory, @Nullable MaterialOreBearer oreBearer, Consumer<MaterialGenerationHandlerBuilder> builder);
	}

	private final Delegate delegate;

	@Override
	public MaterialGenerationHandler register(
		final String registryName, final Function<Material, String> unlocalizedNameFactory, @Nullable final MaterialOreBearer oreBearer, final Consumer<MaterialGenerationHandlerBuilder> builder
	) {
		return this.delegate.apply(registryName, unlocalizedNameFactory, oreBearer, builder);
	}
}
