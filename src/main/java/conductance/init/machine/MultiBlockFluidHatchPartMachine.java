package conductance.init.machine;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.machine.CapIO;
import conductance.api.machine.CapabilityHelper;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.machine.multi.MultiPartMachineBlockEntity;
import conductance.api.util.IO;

public final class MultiBlockFluidHatchPartMachine extends MultiPartMachineBlockEntity<MultiBlockFluidHatchPartMachine> {

	private final @Getter MachineRecipeCapabilityFluids fluids;
	private final @Getter IO io;
	private @Nullable MachineTick tick = null;

	public MultiBlockFluidHatchPartMachine(final MachineType<MultiBlockFluidHatchPartMachine> type, final BlockPos pos, final BlockState blockState, final IO io, final int tanks, final int capacity) {
		super(type, pos, blockState);
		this.fluids = new MachineRecipeCapabilityFluids(this, tanks, io, CapIO.BOTH, t -> new MachineFluidHandler(t, capacity));
		this.fluids.addChangedListener(this::revalidateTick);
		this.io = io;
	}

	@Override
	public boolean getDefaultWorkingState() {
		return true;
	}

	protected void revalidateTick() {
		if (!this.isServerSide()) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
	}

	private void tick() {
		assert this.tick != null;
		if ((this.io == IO.OUT && this.fluids.getHandler().isEmpty()) || !this.isCurrentlyWorking()) {
			this.tick.invalidate();
			return;
		}
		if (this.haveTicksPassed(10)) {
			switch (this.io) {
				case IN -> CapabilityHelper.tryImportFluids(this.fluids.getHandler(), this.getLevel(), this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite());
				case OUT -> CapabilityHelper.tryExportFluids(this.fluids.getHandler(), this.getLevel(), this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite());
			}
		}
	}

	public void setAutoEnabled(final boolean enabled) {
		if (enabled != this.isCurrentlyWorking()) {
			this.setWorkingState(enabled);
			this.setChanged();
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.revalidateTick();
	}

	@Override
	public MultiBlockPartCapability getPartCapability() {
		return switch (this.io) {
			case IN -> NCMultiBlockPartCapabilities.FLUIDS_IN;
			case OUT -> NCMultiBlockPartCapabilities.FLUIDS_OUT;
		};
	}

	@Override
	public void attachCapabilities(final BiConsumer<IO, MachineRecipeCapability<?>> consumer) {
		consumer.accept(this.io, this.fluids);
	}
}
