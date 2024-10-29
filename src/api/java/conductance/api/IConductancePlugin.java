package conductance.api;

import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.PeriodicElementBuilder;

public interface IConductancePlugin {

	default void registerPeriodicElements(final PeriodicElementBuilder builder) {
	}

	default void registerMaterialTextureTypes(MaterialTextureTypeRegister register) {
	}

	default void registerMaterialTextureSets(MaterialTextureSetRegister register) {
	}
}
