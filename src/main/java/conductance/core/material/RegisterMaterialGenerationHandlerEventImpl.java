package conductance.core.material;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;

@RequiredArgsConstructor
final class RegisterMaterialGenerationHandlerEventImpl implements RegisterMaterialGenerationHandlerEvent {

	private final TriFunction<String, Function<Material, String>, Consumer<MaterialGenerationHandlerBuilder>, MaterialGenerationHandler> delegate;

	@Override
	public MaterialGenerationHandler register(final String registryName, final Function<Material, String> unlocalizedNameFactory, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.delegate.apply(registryName, unlocalizedNameFactory, builder);
	}
}
