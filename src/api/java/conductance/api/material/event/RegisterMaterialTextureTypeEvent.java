package conductance.api.material.event;

import conductance.api.material.MaterialTextureType;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialTextureTypeEvent extends IConductancePluginEvent {

	MaterialTextureType register(String name);
}
