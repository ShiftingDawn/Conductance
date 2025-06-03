package conductance.api.material;

import conductance.api.registry.TaggedSetRegistry;

public interface MaterialRegistry extends TaggedSetRegistry<Material, TaggedMaterialSet> {

	boolean hasOverride(TaggedMaterialSet set, Material material);

	boolean hasUnitOverride(TaggedMaterialSet set, Material material);

	long getUnitOverride(TaggedMaterialSet set, Material material);
}
