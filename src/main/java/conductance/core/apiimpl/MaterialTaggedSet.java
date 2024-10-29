package conductance.core.apiimpl;

import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.core.register.MaterialOverrideRegister;
import conductance.core.register.MaterialUnitOverrideRegister;

public final class MaterialTaggedSet extends TaggedSetImpl<Material> implements TaggedMaterialSet {

	@Getter
	private final MaterialTextureType textureType;

	public MaterialTaggedSet(final MaterialTaggedSetBuilder builder) {
		super(builder);
		this.textureType = builder.textureType();
	}

	@Override
	public boolean canGenerateItem(final Material object) {
		return super.canGenerateItem(object) && !MaterialOverrideRegister.has(this, object);
	}

	@Override
	public boolean canGenerateBlock(final Material object) {
		return super.canGenerateBlock(object) && !MaterialOverrideRegister.has(this, object);
	}

	@Override
	public boolean canGenerateFluid(final Material object) {
		return super.canGenerateFluid(object) && !MaterialOverrideRegister.has(this, object);
	}

	@Override
	public long getUnitValue(final Material object) {
		final long override = MaterialUnitOverrideRegister.get(this, object);
		return override > 0 ? override : this.getUnitValue();
	}
}
