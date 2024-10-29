package conductance.api;

import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialOverrideMap;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.PeriodicElementBuilder;

public interface IConductancePlugin {

	default void registerPeriodicElements(final PeriodicElementBuilder builder) {
	}

	default void registerMaterialTextureTypes(MaterialTextureTypeRegister register) {
	}

	default void registerMaterialTextureSets(MaterialTextureSetRegister register) {
	}

	default void registerMaterialTraits(MaterialTraitRegister register) {
	}

	default void registerMaterialFlags(MaterialFlagRegister register) {
	}

	default void registerMaterialTaggedSets(MaterialTaggedSetRegister register) {
	}

	default void registerMaterials(MaterialRegister register) {
	}

	default void registerMaterialOverrides(MaterialOverrideMap overrides) {
	}
}
