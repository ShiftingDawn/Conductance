package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.machine.multi.MultiPartMachineBlockEntity;
import conductance.api.util.IO;

public final class MultiBlockFluidHatchPartMachine extends MultiPartMachineBlockEntity<MultiBlockFluidHatchPartMachine> {

	private final @Getter MachineRecipeCapabilityFluids fluids;
	private final @Getter IO io;

	public MultiBlockFluidHatchPartMachine(final MachineType<MultiBlockFluidHatchPartMachine> type, final BlockPos pos, final BlockState blockState, final IO io, final int tanks, final int capacity) {
		super(type, pos, blockState);
		this.fluids = new MachineRecipeCapabilityFluids(this, tanks, io, CapIO.BOTH, t -> new MachineFluidHandler(t, capacity));
		this.fluids.addChangedListener(this::setChanged);
		this.fluids.addChangedListener(this::syncToClient);
		this.io = io;
	}

	@Override
	public MultiBlockPartCapability getPartCapability() {
		return switch (this.io) {
			case IN -> NCMultiBlockPartCapabilities.FLUIDS_IN;
			case OUT -> NCMultiBlockPartCapabilities.FLUIDS_OUT;
		};
	}
}
