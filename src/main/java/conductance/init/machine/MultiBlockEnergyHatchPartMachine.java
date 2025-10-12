package conductance.init.machine;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.machine.CapabilityHelper;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.MachineRecipeCapabilityEnergy;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.machine.multi.MultiPartMachineBlockEntity;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public final class MultiBlockEnergyHatchPartMachine extends MultiPartMachineBlockEntity<MultiBlockEnergyHatchPartMachine> {

	private final @Getter MachineRecipeCapabilityEnergy energy;
	private final @Getter IO io;
	private @Nullable MachineTick tick = null;

	public MultiBlockEnergyHatchPartMachine(final MachineType<MultiBlockEnergyHatchPartMachine> type, final BlockPos pos, final BlockState blockState, final IO io, final Tier tier) {
		super(type, pos, blockState);
		final long capacity = tier.getVoltage() * 64;
		this.energy = switch (io) {
			case IN -> MachineRecipeCapabilityEnergy.createInput(this, IO.IN, capacity, tier.getVoltage(), 2);
			case OUT -> MachineRecipeCapabilityEnergy.createOutput(this, IO.OUT, capacity, tier.getVoltage(), 2);
		};
		this.energy.setCapabilityValidator(side -> side == this.getFacing());
		this.energy.addChangedListener(this::revalidateTick);
		this.io = io;
	}

	protected void revalidateTick() {
		if (!this.isServerSide() || this.io == IO.IN) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
	}

	private void tick() {
		CapabilityHelper.tryExportEnergy(this.energy, this.getLevel(), this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite());
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.revalidateTick();
	}

	@Override
	public MultiBlockPartCapability getPartCapability() {
		return switch (this.io) {
			case IN -> NCMultiBlockPartCapabilities.ENERGY_IN;
			case OUT -> NCMultiBlockPartCapabilities.ENERGY_OUT;
		};
	}

	@Override
	public void attachCapabilities(final BiConsumer<IO, MachineRecipeCapability<?>> consumer) {
		consumer.accept(this.io, this.energy);
	}
}
