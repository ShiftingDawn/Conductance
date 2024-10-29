package conductance.core.apiimpl;

import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;

public final class MaterialTaggedSet extends TaggedSetImpl<Material> implements TaggedMaterialSet {

	@Getter
	private final MaterialTextureType textureType;

	public MaterialTaggedSet(MaterialTaggedSetBuilder builder) {
		super(builder);
		this.textureType = builder.textureType();
	}
}
