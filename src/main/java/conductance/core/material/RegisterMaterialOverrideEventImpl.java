package conductance.core.material;

import net.minecraft.world.level.ItemLike;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.event.RegisterMaterialOverrideEvent;

@AllArgsConstructor
final class RegisterMaterialOverrideEventImpl implements RegisterMaterialOverrideEvent {

	private final TriConsumer<TaggedMaterialSet, Material, ItemLike[]> delegate;

	@Override
	public void add(final TaggedMaterialSet set, final Material material, final ItemLike... overrides) {
		this.delegate.accept(set, material, overrides);
	}
}
