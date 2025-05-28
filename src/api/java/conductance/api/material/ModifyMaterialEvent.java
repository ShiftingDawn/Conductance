package conductance.api.material;

import conductance.api.plugin.IConductancePluginEvent;

public interface ModifyMaterialEvent extends IConductancePluginEvent {

	Material getMaterial();

	void addFlag(MaterialFlag flag);

	<T extends IMaterialTrait<T>> void addTrait(MaterialTraitKey<T> trait, T value);

	void removeFlag(MaterialFlag flag);

	void removeTrait(MaterialTraitKey<?> trait);
}
