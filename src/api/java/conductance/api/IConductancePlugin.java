package conductance.api;

import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialOreTypeRegister;
import conductance.api.plugin.MaterialOverrideMap;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.MaterialUnitOverrideMap;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.api.plugin.TagRegister;

public interface IConductancePlugin {

	default void registerPeriodicElements(final PeriodicElementBuilder builder) {
	}

	default void registerMaterialTextureTypes(final MaterialTextureTypeRegister register) {
	}

	default void registerMaterialTextureSets(final MaterialTextureSetRegister register) {
	}

	default void registerMaterialTraits(final MaterialTraitRegister register) {
	}

	default void registerMaterialFlags(final MaterialFlagRegister register) {
	}

	default void registerMaterialOreTypes(final MaterialOreTypeRegister register) {
	}

	default void registerMaterialTaggedSets(final MaterialTaggedSetRegister register) {
	}

	default void registerMaterials(final MaterialRegister register) {
	}

	default void registerMaterialOverrides(final MaterialOverrideMap overrides) {
	}

	default void registerMaterialUnitOverrides(final MaterialUnitOverrideMap overrides) {
	}

	default void registerTags(final TagRegister tags) {
	}
}
