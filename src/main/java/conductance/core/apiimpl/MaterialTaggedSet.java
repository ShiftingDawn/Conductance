package conductance.core.apiimpl;

import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.core.register.MaterialOverrideRegister;

public final class MaterialTaggedSet extends TaggedSetImpl<Material> implements TaggedMaterialSet {

	@Getter
	private final MaterialTextureType textureType;

	public MaterialTaggedSet(MaterialTaggedSetBuilder builder) {
		super(builder);
		this.textureType = builder.textureType();
	}

	@Override
	public boolean canGenerateItem(Material object) {
		return super.canGenerateItem(object) && !MaterialOverrideRegister.has(this, object);
	}

	@Override
	public boolean canGenerateBlock(Material object) {
		return super.canGenerateBlock(object) && !MaterialOverrideRegister.has(this, object);
	}

	@Override
	public boolean canGenerateFluid(Material object) {
		return super.canGenerateFluid(object) && !MaterialOverrideRegister.has(this, object);
	}
}
