package conductance.api.material.event;

import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialTraitEvent extends IConductancePluginEvent {

	<T extends IMaterialTrait<T>> MaterialTraitKey<T> register(String name, Class<T> typeClass);
}
