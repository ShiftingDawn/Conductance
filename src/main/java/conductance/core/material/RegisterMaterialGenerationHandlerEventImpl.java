package conductance.core.material;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;

@RequiredArgsConstructor
final class RegisterMaterialGenerationHandlerEventImpl implements RegisterMaterialGenerationHandlerEvent {

	private final TriFunction<String, String, Consumer<MaterialGenerationHandlerBuilder>, MaterialGenerationHandler> delegate;

	@Override
	public MaterialGenerationHandler register(final String registryName, final String unlocalizedNameFactory, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.delegate.apply(registryName, unlocalizedNameFactory, builder);
	}
}
