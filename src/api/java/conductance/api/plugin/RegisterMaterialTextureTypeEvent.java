package conductance.api.plugin;

import conductance.api.material.MaterialTextureType;

public interface RegisterMaterialTextureTypeEvent extends IConductancePluginEvent {

	MaterialTextureType register(String name);
}
