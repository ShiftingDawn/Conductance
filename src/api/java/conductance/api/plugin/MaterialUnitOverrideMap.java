package conductance.api.plugin;

import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialUnitOverrideMap {

	void add(TaggedMaterialSet set, Material material, long value);
}
