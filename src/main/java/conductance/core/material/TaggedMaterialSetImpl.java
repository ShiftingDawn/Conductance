package conductance.core.material;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.core.apiimpl.TaggedSetImpl;

public final class TaggedMaterialSetImpl extends TaggedSetImpl<Material> implements TaggedMaterialSet {

	@Getter
	private final MaterialTextureType textureType;
	@Nullable
	@Getter
	private final MaterialOreType oreType;

	public TaggedMaterialSetImpl(final MaterialTaggedSetBuilder builder) {
		super(builder);
		this.textureType = builder.getTextureType();
		this.oreType = builder.getOreType();
	}

	@Override
	public boolean canGenerateItem(final Material object) {
		return super.canGenerateItem(object) && !CAPI.materials().hasOverride(this, object);
	}

	@Override
	public boolean canGenerateBlock(final Material object) {
		return super.canGenerateBlock(object) && !CAPI.materials().hasOverride(this, object);
	}

	@Override
	public boolean canGenerateFluid(final Material object) {
		return super.canGenerateFluid(object) && !CAPI.materials().hasOverride(this, object);
	}

	@Override
	public long getUnitValue(final Material object) {
		final long override = CAPI.materials().getUnitOverride(this, object);
		return override > 0 ? override : this.getUnitValue();
	}
}
