package conductance.core.material;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RegisterMaterialUnitOverrideEvent;

@AllArgsConstructor
final class RegisterMaterialUnitOverrideEventImpl implements RegisterMaterialUnitOverrideEvent {

	private final TriConsumer<TaggedMaterialSet, Material, Long> delegate;

	@Override
	public void add(final TaggedMaterialSet set, final Material material, final long value) {
		this.delegate.accept(set, material, value);
	}
}
