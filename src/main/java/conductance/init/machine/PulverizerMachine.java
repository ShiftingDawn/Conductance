package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapabilityInventory;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineType;

public final class PulverizerMachine extends MachineBlockEntity<PulverizerMachine> {

	private final MachineCapabilityInventory inventory;

	public PulverizerMachine(final MachineType<PulverizerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inventory = new MachineCapabilityInventory("inv", this, new MachineInventory(2));
		this.inventory.addChangedListener(this::setChanged);
	}
}
