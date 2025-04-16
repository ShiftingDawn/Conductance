package conductance.core.machine;

import conductance.api.machine.MachineType;

public class BronzeDirectionalMachineRuntimeModelProvider extends DirectionalMachineRuntimeModelProvider {

	public BronzeDirectionalMachineRuntimeModelProvider(final MachineType<?> machineType) {
		super(machineType);
	}

	@Override
	protected String getModelPath() {
		return "models/block/machine_block_bronze.json";
	}
}
