package conductance.api.material.event;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.IConductancePluginEvent;

public interface ModifyMaterialEvent extends IConductancePluginEvent {

	ResourceLocation getMaterialId();

	boolean hasFlag(MaterialFlag flag);

	void addFlag(MaterialFlag flag);

	void removeFlag(MaterialFlag flag);

	boolean hasTrait(MaterialTraitKey<?> trait);

	<T extends MaterialTrait<T>> void addTrait(MaterialTraitKey<T> trait, T instance);

	@UnknownNullability
	<T extends MaterialTrait<T>> T getTrait(MaterialTraitKey<T> trait);

	void removeTrait(MaterialTraitKey<?> trait);

	boolean hasProp(MaterialProp<?> prop);

	<T> void addProp(MaterialProp<T> prop, T value);

	@UnknownNullability
	<T> T getProp(MaterialProp<T> prop);

	void removeProp(MaterialProp<?> prop);

	boolean hasComponent(Material material);

	boolean hasComponent(Material material, int minCount);

	int getComponentCount(Material material);

	void removeComponent(Material material);

	void addComponent(Material material, int count);

	void setComponent(Material material, int count);
}
