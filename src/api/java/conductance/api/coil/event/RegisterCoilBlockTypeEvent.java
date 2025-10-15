package conductance.api.coil.event;

import conductance.api.coil.CoilBlockType;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterCoilBlockTypeEvent extends IConductancePluginEvent {

	CoilBlockType register(String name, int color, CoilBlockType previousCoil);

	CoilBlockType register(String name, int color);
}
