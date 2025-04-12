package conductance.api.plugin;

import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBuilder;

public interface MachineRegister {

	<T extends MachineBlockEntity<T>> MachineBuilder<T> register(String registryName, MachineBlockEntityFactory<T> constructor);
}
