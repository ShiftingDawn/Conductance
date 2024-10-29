package conductance.api.plugin;

import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialUnitOverrideMap {

	void add(final TaggedMaterialSet set, final Material material, final long value);
}
