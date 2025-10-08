package conductance.api.machine.event;

import java.util.function.Consumer;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMachineEvent extends IConductancePluginEvent {

	<T extends MachineBlockEntity<T>> MachineType<T> register(String registryName, MachineBlockEntityFactory<T> blockEntityFactory, Consumer<MachineBuilder<T>> builder);

	<T extends MultiMachineBlockEntity<T> & IMultiBlockController<T>> MultiMachineType<T> multi(String registryName, MultiMachineBlockEntityFactory<T> blockEntityFactory, Consumer<MultiBlockMachineBuilder<T>> builder);

}
