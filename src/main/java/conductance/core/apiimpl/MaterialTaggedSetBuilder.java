package conductance.core.apiimpl;

import java.util.function.Function;
import net.minecraft.Util;
import lombok.Getter;
import lombok.experimental.Accessors;
import conductance.api.CAPI;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.TaggedMaterialSetBuilder;

@Accessors(fluent = true)
public final class MaterialTaggedSetBuilder extends TaggedSetBuilderImpl<Material, TaggedMaterialSet, TaggedMaterialSetBuilder> implements TaggedMaterialSetBuilder {

	@Getter
	private MaterialTextureType textureType = NCTextureTypes.DUST;

	public MaterialTaggedSetBuilder(final String registryKey, final Function<Material, String> unlocalizedNameFactory) {
		super(registryKey, mat -> mat.getRegistryKey().getPath(), unlocalizedNameFactory);
	}

	@SuppressWarnings("CheckStyle")
	@Override
	public TaggedMaterialSetBuilder textureType(final MaterialTextureType newTextureType) {
		this.textureType = newTextureType;
		return this;
	}

	@Override
	public TaggedMaterialSet build() {
		return Util.make(new MaterialTaggedSet(this), set -> CAPI.regs().materialTaggedSets().register(set.getRegistryKey(), set));
	}
}
