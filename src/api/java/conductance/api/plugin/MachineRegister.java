package conductance.api.plugin;

import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityFactory;

public interface MachineRegister {

	<T extends MetaBlockEntity<T>> MachineBuilder<T> register(String registryName, MetaBlockEntityFactory<T> constructor);
}
