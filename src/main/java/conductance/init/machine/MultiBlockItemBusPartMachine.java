package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.machine.multi.MultiPartMachineBlockEntity;
import conductance.api.util.IO;

public final class MultiBlockItemBusPartMachine extends MultiPartMachineBlockEntity<MultiBlockItemBusPartMachine> {

	private final @Getter MachineRecipeCapabilityItems items;
	private final @Getter IO io;

	public MultiBlockItemBusPartMachine(final MachineType<MultiBlockItemBusPartMachine> type, final BlockPos pos, final BlockState blockState, final IO io, final int slots) {
		super(type, pos, blockState);
		this.items = new MachineRecipeCapabilityItems(this, slots, io, CapIO.BOTH, MachineInventory::new);
		this.items.addChangedListener(this::setChanged);
		this.items.addChangedListener(this::syncToClient);
		this.io = io;
	}

	@Override
	public MultiBlockPartCapability getPartCapability() {
		return switch (this.io) {
			case IN -> NCMultiBlockPartCapabilities.ITEMS_IN;
			case OUT -> NCMultiBlockPartCapabilities.ITEMS_OUT;
		};
	}
}
