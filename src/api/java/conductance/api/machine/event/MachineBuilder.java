package conductance.api.machine.event;

import conductance.api.machine.MachineBlockEntity;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);
}
