package conductance.api.plugin;

import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialTraitKey;

public interface RegisterMaterialTraitEvent extends IConductancePluginEvent {

	<T extends IMaterialTrait<T>> MaterialTraitKey<T> register(String name, Class<T> typeClass);
}
