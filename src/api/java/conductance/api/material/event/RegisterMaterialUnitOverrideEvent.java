package conductance.api.material.event;

import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialUnitOverrideEvent extends IConductancePluginEvent {

	void add(TaggedMaterialSet set, Material material, long value);
}
