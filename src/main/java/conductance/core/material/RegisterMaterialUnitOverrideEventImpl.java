package conductance.core.material;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.RegisterMaterialUnitOverrideEvent;

@RequiredArgsConstructor
final class RegisterMaterialUnitOverrideEventImpl implements RegisterMaterialUnitOverrideEvent {

	private final TriConsumer<MaterialGenerationHandler, Material, Long> delegate;

	@Override
	public void add(final MaterialGenerationHandler handler, final Material material, final long value) {
		this.delegate.accept(handler, material, value);
	}
}
