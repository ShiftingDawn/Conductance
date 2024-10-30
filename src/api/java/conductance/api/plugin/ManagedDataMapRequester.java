package conductance.api.plugin;

import conductance.api.machine.data.IManaged;
import conductance.api.machine.data.ManagedDataMap;

public interface ManagedDataMapRequester {

	ManagedDataMap requestDataMap(IManaged managed);
}
