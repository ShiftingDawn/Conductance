package conductance.api.plugin;

import conductance.api.material.MaterialTextureType;

@FunctionalInterface
public interface MaterialTextureTypeRegister {

	MaterialTextureType register(String name);
}
