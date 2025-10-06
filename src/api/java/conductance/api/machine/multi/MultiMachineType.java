package conductance.api.machine.multi;

import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public interface MultiMachineType<T extends MachineBlockEntity<T>> extends MachineType<T> {

	MultiBlockStructure getStructure();
}
