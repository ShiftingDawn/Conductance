package conductance.core.material;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.event.MaterialBuilder;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.periodicelement.PeriodicElement;

@RequiredArgsConstructor
final class RegisterMaterialEventImpl implements RegisterMaterialEvent {

	private final TriFunction<String, @Nullable PeriodicElement, Consumer<MaterialBuilder>, Material> delegate;

	@Override
	public Material register(final String registryName, @Nullable final PeriodicElement element, final Consumer<MaterialBuilder> builder) {
		return this.delegate.apply(registryName, element, builder);
	}
}
