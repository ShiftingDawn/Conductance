package conductance.api.plugin;

import conductance.api.material.MaterialTextureSet;

public interface RegisterMaterialTextureSetEvent extends IConductancePluginEvent {

	MaterialTextureSet register(String name, String parentSet);

	MaterialTextureSet register(String name, MaterialTextureSet parent);

	MaterialTextureSet register(String name);
}
