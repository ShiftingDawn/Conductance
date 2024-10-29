package conductance.api.plugin;

import conductance.api.material.MaterialTextureSet;

@FunctionalInterface
public interface MaterialTextureSetRegister {

	MaterialTextureSet register(String name, String parentSet);

	default MaterialTextureSet register(String name, MaterialTextureSet parent) {
		return register(name, parent.getRegistryKey());
	}

	default MaterialTextureSet register(final String name) {
		return this.register(name, "dull");
	}
}
