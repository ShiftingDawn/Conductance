package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRecipeCapabilityEnergy;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.api.IWorkable;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public final class TransformerMachine extends MachineBlockEntity<TransformerMachine> implements IWorkable {

	private final @Getter Tier inputTier;
	private final @Getter TransformerEnergyHandler energy;
	private @Nullable MachineTick tick = null;

	public TransformerMachine(final MachineType<TransformerMachine> type, final BlockPos pos, final BlockState blockState, final Tier inputTier, final long outputAmps) {
		super(type, pos, blockState);
		this.inputTier = inputTier;
		this.energy = new TransformerEnergyHandler(this, IO.IN, inputTier.getVoltage() * 4 * outputAmps * 4, inputTier.getVoltage(), outputAmps * 4, inputTier.getNextTier().getVoltage(), outputAmps, false);
		this.energy.setInputSidePredicate(side -> side != this.getFacing() && this.isTransformUp());
		this.energy.setOutputSidePredicate(side -> side == this.getFacing() && this.isTransformUp());
		this.energy.addChangedListener(this::revalidateTick);
	}

	private boolean isTransformUp() {
		return this.isProcessingAllowed();
	}

	@Override
	public void setProcessingAllowed(final boolean allowed) {
		super.setProcessingAllowed(allowed);
		if (allowed != this.isProcessingAllowed()) {
			this.energy.switchIO();
		}
	}

	@Override
	public void setWorking(final boolean working) {
	}

	@Override
	public boolean isWorking() {
		return this.isProcessingAllowed();
	}

	protected void revalidateTick() {
		if (!this.isServerSide()) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
	}

	@Override
	public void onBlockStateChanged(final BlockState oldState, final BlockState newState) {
		if (oldState != newState) {
			this.revalidateTick();
		}
	}

	@SuppressWarnings("deprecation")
	private void tick() {
		assert this.tick != null && this.level != null;
		if (this.getEnergy().getEnergyStored() < this.getEnergy().getOutputVoltage()) {
			this.tick.invalidate();
			return;
		}
		long ampsLeft = this.getEnergy().getOutputAmperage();
		for (final Direction side : Direction.values()) {
			if (ampsLeft == 0 || this.getEnergy().getEnergyStored() < this.getEnergy().getOutputVoltage()) {
				return;
			}
			if (this.energy.canExtractEnergy(side)) {
				final IEnergyHandler handler = this.level.getCapability(NCCapabilities.ENERGY_HANDLER_BLOCK, this.getBlockPos().relative(side), side.getOpposite());
				if (handler != null) {
					final long acceptedAmps = handler.receiveEnergy(side.getOpposite(), this.getEnergy().getOutputVoltage(), ampsLeft);
					if (acceptedAmps > 0) {
						ampsLeft -= acceptedAmps;
						this.energy.removeEnergy(acceptedAmps * this.energy.getOutputVoltage());
					}
				}
			}
		}
	}

	private static class TransformerEnergyHandler extends MachineRecipeCapabilityEnergy {

		TransformerEnergyHandler(
			final MachineBlockEntity<?> machine, final IO recipeIoMode, final long capacity, final long inputVoltage, final long inputAmperage, final long outputVoltage, final long outputAmperage,
			final boolean canOverclock
		) {
			super(machine, recipeIoMode, capacity, inputVoltage, inputAmperage, outputVoltage, outputAmperage, canOverclock);
		}

		void switchIO() {
			final long inVoltage = this.getInputVoltage();
			final long inAmps = this.getInputAmperage();
			final long outVoltage = this.getOutputVoltage();
			final long outAmps = this.getOutputAmperage();
			this.setInputVoltage(outVoltage);
			this.setInputAmperage(outAmps);
			this.setOutputVoltage(inVoltage);
			this.setOutputAmperage(inAmps);
			this.setChanged();
		}
	}
}
