package conductance.api.registry;

import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialRegistry extends TaggedSetRegistry<Material, TaggedMaterialSet> {

	boolean hasOverride(TaggedMaterialSet set, Material material);

	boolean hasUnitOverride(TaggedMaterialSet set, Material material);

	long getUnitOverride(TaggedMaterialSet set, Material material);
}
