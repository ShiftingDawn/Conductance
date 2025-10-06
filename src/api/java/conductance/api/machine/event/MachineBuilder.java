package conductance.api.machine.event;

import conductance.api.machine.MachineBlockEntity;
import conductance.api.tier.Tier;

public interface MachineBuilder<T extends MachineBlockEntity<T>> extends AbstractMachineBuilder<T, MachineBuilder<T>> {

	MachineBuilder<T> tieredModel(String machineModelKey, Tier tier);
}
