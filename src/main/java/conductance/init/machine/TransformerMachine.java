package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRecipeCapabilityEnergy;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public final class TransformerMachine extends MachineBlockEntity<TransformerMachine> {

	private final @Getter Tier inputTier;
	private final @Getter MachineRecipeCapabilityEnergy energy;
	private @Nullable MachineTick tick = null;
	private @Nullable BlockCapabilityCache<IEnergyHandler, Direction> outputCache;

	public TransformerMachine(final MachineType<TransformerMachine> type, final BlockPos pos, final BlockState blockState, final Tier inputTier, final long outputAmps) {
		super(type, pos, blockState);
		this.inputTier = inputTier;
		this.energy = new MachineRecipeCapabilityEnergy(this, IO.IN, inputTier.getVoltage() * 4 * outputAmps * 64, inputTier.getVoltage(), outputAmps * 4, inputTier.getNextTier().getVoltage(), outputAmps, false);
		this.energy.setInputSidePredicate(side -> side != this.getFacing());
		this.energy.setOutputSidePredicate(side -> side == this.getFacing());
		this.energy.addChangedListener(this::revalidateTick);
	}

	protected void revalidateTick() {
		if (!this.isServerSide()) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
		this.outputCache = null;
	}

	private void invalidateCapCache() {
		this.outputCache = null;
	}

	@Override
	public void onBlockStateChanged(final BlockState oldState, final BlockState newState) {
		if (oldState != newState) {
			this.outputCache = null;
		}
	}

	private void tick() {
		assert this.tick != null;
		final long outputAmount = this.getEnergy().getOutputVoltage() * this.getEnergy().getOutputAmperage();
		if (this.getEnergy().getEnergyStored() < outputAmount) {
			this.tick.invalidate();
			return;
		}
		if (this.outputCache == null) {
			this.onServer(level -> {
				this.outputCache = BlockCapabilityCache.create(NCCapabilities.ENERGY_HANDLER_BLOCK, level, this.getBlockPos().relative(this.getFacing()), this.getFacing().getOpposite(),
					() -> !this.isRemoved(), this::invalidateCapCache);
			});
		}
		if (this.outputCache != null) {
			final long acceptedAmps = this.outputCache.getCapability().receiveEnergy(this.getFacing().getOpposite(), this.getEnergy().getOutputVoltage(), this.getEnergy().getOutputAmperage());
			if (acceptedAmps > 0) {
				this.energy.removeEnergy(acceptedAmps * this.energy.getOutputVoltage());
			}
		}
	}
}
