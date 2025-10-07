package conductance.api.machine.event;

import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMultiBlockPartCapabilityEvent extends IConductancePluginEvent {

	MultiBlockPartCapability register(String registryName);
}
