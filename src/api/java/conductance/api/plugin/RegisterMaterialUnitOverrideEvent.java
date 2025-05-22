package conductance.api.plugin;

import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface RegisterMaterialUnitOverrideEvent extends IConductancePluginEvent {

	void add(TaggedMaterialSet set, Material material, long value);
}
