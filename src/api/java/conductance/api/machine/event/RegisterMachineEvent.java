package conductance.api.machine.event;

import java.util.function.Consumer;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MachineType;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMachineEvent extends IConductancePluginEvent {

	<T extends MachineBlockEntity<T>> MachineType<T> register(String registryName, MachineBlockEntityFactory<T> constructor, Consumer<MachineBuilder<T>> builder);
}
