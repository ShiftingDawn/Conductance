package conductance.api.material;

import conductance.api.registry.TaggedSetBuilder;

public interface TaggedMaterialSetBuilder extends TaggedSetBuilder<Material, TaggedMaterialSet, TaggedMaterialSetBuilder> {

	TaggedMaterialSetBuilder textureType(MaterialTextureType textureType);
}
