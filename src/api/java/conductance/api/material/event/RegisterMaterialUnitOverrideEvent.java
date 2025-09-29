package conductance.api.material.event;

import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialUnitOverrideEvent extends IConductancePluginEvent {

	void add(MaterialGenerationHandler handler, Material material, long value);
}
