package conductance.core.material;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.MaterialBuilder;
import conductance.api.material.event.RegisterMaterialEvent;

@AllArgsConstructor
final class RegisterMaterialEventImpl implements RegisterMaterialEvent {

	private final BiFunction<String, Consumer<MaterialBuilder>, Material> delegate;

	@Override
	public Material register(final String registryName, final Consumer<MaterialBuilder> builder) {
		return this.delegate.apply(registryName, builder);
	}
}
