package conductance.api.machine;

public interface IMachineBlockItem<T extends MachineBlockEntity<T>> {

	MachineType<T> getMachineType();
}
