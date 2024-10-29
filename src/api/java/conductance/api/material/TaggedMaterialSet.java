package conductance.api.material;

import conductance.api.registry.TaggedSet;

public interface TaggedMaterialSet extends TaggedSet<Material> {

	MaterialTextureType getTextureType();
}
