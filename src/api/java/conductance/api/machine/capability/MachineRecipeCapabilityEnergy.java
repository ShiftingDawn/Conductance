package conductance.api.machine.capability;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.energy.IEnergyHandler;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRunnable;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.util.IOMode;
import conductance.api.util.MiscUtils;

public final class MachineRecipeCapabilityEnergy extends MachineRecipeCapability<Long> implements IEnergyHandler {

	public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineRecipeCapabilityEnergy.class, MachineRecipeCapability.MANAGED_FIELD_HOLDER);
	@Persisted
	@DescSynced
	protected long energy;
	protected long capacity;
	protected long inputVoltage, inputAmperage;
	protected long outputVoltage, outputAmperage;
	private long lastAcceptedTimestamp, acceptedAmpsThisTick;
	@Setter
	@Nullable
	private Predicate<Direction> sideInputCondition, sideOutputCondition;
	@Nullable
	private MachineRunnable outputTickable;

	public MachineRecipeCapabilityEnergy(final MachineBlockEntity<?> machine, final long capacity, final long inputVoltage, final long inputAmperage, final long outputVoltage, final long outputAmperage) {
		super(machine, NCRecipeElementTypes.ENERGY, MachineRecipeCapabilityEnergy.getIO(inputVoltage, inputAmperage, outputVoltage, outputAmperage),
				MachineRecipeCapabilityEnergy.getIO(inputVoltage, inputAmperage, outputVoltage, outputAmperage));
		this.capacity = capacity;
		this.inputVoltage = inputVoltage;
		this.inputAmperage = inputAmperage;
		this.outputVoltage = outputVoltage;
		this.outputAmperage = outputAmperage;
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineRecipeCapabilityEnergy.MANAGED_FIELD_HOLDER;
	}

	public static MachineRecipeCapabilityEnergy createInput(final MachineBlockEntity<?> machine, final long capacity, final long inputVoltage, final long inputAmperage) {
		return new MachineRecipeCapabilityEnergy(machine, capacity, inputVoltage, inputAmperage, 0, 0);
	}

	public static MachineRecipeCapabilityEnergy createOutput(final MachineBlockEntity<?> machine, final long capacity, final long outputVoltage, final long outputAmperage) {
		return new MachineRecipeCapabilityEnergy(machine, capacity, 0, 0, outputVoltage, outputAmperage);
	}

	@Override
	@Nullable
	public List<Long> handleInternal(final IOMode ioMode, final IRecipe recipe, final List<Long> leftOver, final boolean simulate) {
		final IEnergyHandler capability = this;
		long sum = leftOver.stream().reduce(0L, Long::sum);
		if (ioMode == IOMode.INPUT) {
			final long canOutput = capability.getEnergyStored();
			if (!simulate) {
				capability.addEnergy(-Math.min(canOutput, sum));
			}
			sum = sum - canOutput;
		} else if (ioMode == IOMode.OUTPUT) {
			final long canInput = capability.getEnergyCapacity() - capability.getEnergyStored();
			if (!simulate) {
				capability.addEnergy(Math.min(canInput, sum));
			}
			sum = sum - canInput;
		}
		return sum <= 0 ? null : Collections.singletonList(sum);
	}

	public void updateOutputTickable() {
		if (this.getOutputVoltage() > 0 && this.getOutputAmperage() > 0) {
			if (this.getEnergyStored() >= this.getOutputVoltage()) {
				this.outputTickable = this.getMachineBlockEntity().addTick(this.outputTickable, this::tick);
			} else if (this.outputTickable != null) {
				this.outputTickable.invalidate();
				this.outputTickable = null;
			}
		}
	}

	private void tick() {
		final long outVolts = this.getOutputVoltage();
		final long outAmps = Math.min(this.getEnergyStored() / outVolts, this.getOutputAmperage());
		if (outAmps == 0) {
			return;
		}
		long ampsUsed = 0;
		for (final Direction side : Direction.values()) {
			if (!this.canExtractEnergy(side)) {
				continue;
			}
			final Direction oppositeSide = side.getOpposite();
			final IEnergyHandler energyContainer = CapabilityHelper.getEnergyHandler(this.getMachineBlockEntity().getLevel(), this.getMachineBlockEntity().getBlockPos().relative(side), oppositeSide);
			if (energyContainer != null && energyContainer.canReceiveEnergy(oppositeSide)) {
				ampsUsed += energyContainer.receiveEnergy(oppositeSide, outVolts, outAmps - ampsUsed);
				if (ampsUsed == outAmps) {
					break;
				}
			}
		}
		if (ampsUsed > 0) {
			this.setEnergyStored(this.getEnergyStored() - ampsUsed * outVolts);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.updateOutputTickable();
	}

	public void setEnergyStored(final long newEnergy) {
		if (this.energy != newEnergy) {
			this.energy = newEnergy;
			this.updateOutputTickable();
			this.notifyListeners();
		}
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long voltage, final long amperage) {
		final long currentTime = this.getMachineBlockEntity().getTimerOffset();
		if (this.lastAcceptedTimestamp < currentTime) {
			this.acceptedAmpsThisTick = 0;
			this.lastAcceptedTimestamp = currentTime;
		}
		if (this.acceptedAmpsThisTick >= this.getInputAmperage()) {
			return 0;
		}
		if (voltage > 0L && (receivingSide == null || this.canReceiveEnergy(receivingSide))) {
			if (voltage > this.getInputVoltage()) {
				MiscUtils.explode(this.getMachineBlockEntity(), voltage);
				return Math.min(amperage, this.getInputAmperage() - this.acceptedAmpsThisTick);
			}
			if (this.getEnergySpace() >= voltage) {
				final long ampsAccepted = Math.min(this.getEnergySpace() / voltage, Math.min(amperage, this.getInputAmperage() - this.acceptedAmpsThisTick));
				if (ampsAccepted > 0) {
					this.setEnergyStored(this.getEnergyStored() + voltage * ampsAccepted);
					this.acceptedAmpsThisTick += ampsAccepted;
					return ampsAccepted;
				}
			}
		}
		return 0;
	}

	@Override
	public boolean canReceiveEnergy(@Nullable final Direction side) {
		return !this.canExtractEnergy(side) && this.getInputVoltage() > 0 && (this.sideInputCondition == null || this.sideInputCondition.test(side));
	}

	@Override
	public boolean canExtractEnergy(@Nullable final Direction side) {
		return this.outputVoltage > 0 && (this.sideOutputCondition == null || this.sideOutputCondition.test(side));
	}

	@Override
	public long modifyEnergy(final long differenceAmount) {
		final long oldEnergy = this.getEnergyStored();
		long newEnergy = this.getEnergyCapacity() - oldEnergy < differenceAmount ? this.capacity : oldEnergy + differenceAmount;
		if (newEnergy < 0) {
			newEnergy = 0;
		}
		this.setEnergyStored(newEnergy);
		return newEnergy - oldEnergy;
	}

	@Override
	public long getEnergyStored() {
		return this.energy;
	}

	@Override
	public long getEnergyCapacity() {
		return this.capacity;
	}

	@Override
	public long getInputVoltage() {
		return this.inputVoltage;
	}

	@Override
	public long getInputAmperage() {
		return this.inputAmperage;
	}

	@Override
	public long getOutputVoltage() {
		return this.outputVoltage;
	}

	@Override
	public long getOutputAmperage() {
		return this.outputAmperage;
	}

	private static IOMode getIO(final long inputVoltage, final long inputAmperage, final long outputVoltage, final long outputAmperage) {
		final boolean input = inputVoltage > 0 && inputAmperage > 0;
		final boolean output = outputVoltage > 0 && outputAmperage > 0;
		return input && output ? IOMode.INPUT_OUTPUT : input ? IOMode.INPUT : output ? IOMode.OUTPUT : IOMode.BLOCKED;
	}
}
