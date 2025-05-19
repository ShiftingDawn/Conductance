package conductance.api.plugin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterMaterialUnitOverrideEvent implements IConductancePluginEvent {

	public interface MaterialUnitOverrideMap {

		void add(TaggedMaterialSet set, Material material, long value);
	}

	private final MaterialUnitOverrideMap delegate;

	public void add(final TaggedMaterialSet set, final Material material, final long value) {
		this.delegate.add(set, material, value);
	}
}
