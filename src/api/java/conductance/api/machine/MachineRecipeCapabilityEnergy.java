package conductance.api.machine;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.block.BlockHelper;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.recipe.MachineRecipe;
import conductance.api.util.IO;

@SuppressWarnings("deprecation")
public final class MachineRecipeCapabilityEnergy extends MachineRecipeCapability<Long> implements IBlockCapabilityHandler, IEnergyHandler {

	private final boolean canOverclock;
	private @Getter long capacity;
	private long energy;
	private @Getter long inputVoltage;
	private @Getter long inputAmperage;
	private @Getter long outputVoltage;
	private @Getter long outputAmperage;
	private long lastAcceptedTimestamp;
	private long acceptedAmpsThisTick;

	public MachineRecipeCapabilityEnergy(
		final MachineBlockEntity<?> machine, final IO recipeIoMode, final long capacity, final long inputVoltage, final long inputAmperage, final long outputVoltage, final long outputAmperage, final boolean canOverclock
	) {
		super(machine, NCRecipeElementTypes.ENERGY, recipeIoMode, MachineRecipeCapabilityEnergy.getIO(inputVoltage, inputAmperage, outputVoltage, outputAmperage));
		this.canOverclock = canOverclock;
		this.capacity = capacity;
		this.inputVoltage = inputVoltage;
		this.inputAmperage = inputAmperage;
		this.outputVoltage = outputVoltage;
		this.outputAmperage = outputAmperage;
		this.addChangedListener(machine::syncToClient);
	}

	public static MachineRecipeCapabilityEnergy createInput(
		final MachineBlockEntity<?> machine, final IO recipeIoMode, final long capacity, final long inputVoltage, final long inputAmperage, final boolean canOverclock
	) {
		return new MachineRecipeCapabilityEnergy(machine, recipeIoMode, capacity, inputVoltage, inputAmperage, 0, 0, canOverclock);
	}

	public static MachineRecipeCapabilityEnergy createOutput(
		final MachineBlockEntity<?> machine, final IO recipeIoMode, final long capacity, final long outputVoltage, final long outputAmperage, final boolean canOverclock
	) {
		return new MachineRecipeCapabilityEnergy(machine, recipeIoMode, capacity, 0, 0, outputVoltage, outputAmperage, canOverclock);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		valueOutput.putLong("energy", this.energy);
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		this.energy = valueInput.getLongOr("energy", 0);
	}

	@Override
	protected @Nullable List<Long> handleInternal(final IO io, final MachineRecipe recipe, final List<Long> inputs, final boolean simulate) {
		if (io != this.getRecipeIoMode()) {
			return inputs;
		}
		long left = inputs.stream().mapToLong(Long::longValue).sum();
		if (io == IO.IN) {
			if (left > this.getOverclockedInputVoltage() * this.getOverclockedInputAmperage()) {
				return inputs;
			}
			final long extracted = Math.min(left, this.getEnergyStored());
			if (!simulate) {
				this.removeEnergy(extracted);
			}
			left -= extracted;
		} else {
			if (left > this.getOverclockedOutputVoltage() * this.getOverclockedOutputAmperage()) {
				return inputs;
			}
			final long accepted = Math.min(left, this.getEnergySpace());
			if (!simulate) {
				this.addEnergy(accepted);
			}
			left -= accepted;
		}
		return left > 0 ? Collections.singletonList(left) : null;
	}

	@Override
	public List<Long> getAvailableContent() {
		return List.of(this.energy);
	}

	@Override
	public int getMaxSpaceForContent(final Long object) {
		final long result = Math.min(object, this.getEnergySpace());
		return (int) Math.min(Integer.MAX_VALUE, result);
	}

	public void setEnergyStored(final long energyStored) {
		if (this.energy != energyStored) {
			this.energy = energyStored;
			this.setChanged();
		}
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		final long currentTime = this.getMachine().getTimerOffset();
		if (this.lastAcceptedTimestamp < currentTime) {
			this.acceptedAmpsThisTick = 0;
			this.lastAcceptedTimestamp = currentTime;
		}
		if (this.acceptedAmpsThisTick >= this.getInputAmperage()) {
			return 0;
		}
		if (volts > 0L && (receivingSide == null || this.canReceiveEnergy(receivingSide))) {
			if (volts > this.getInputVoltage()) {
				BlockHelper.explodeOrReplaceWithFire(this.getMachine(), volts);
				return Math.min(amps, this.getInputAmperage() - this.acceptedAmpsThisTick);
			}
			if (this.getEnergySpace() >= volts) {
				final long ampsAccepted = Math.min(this.getEnergySpace() / volts, Math.min(amps, this.getInputAmperage() - this.acceptedAmpsThisTick));
				if (ampsAccepted > 0) {
					this.setEnergyStored(this.getEnergyStored() + volts * ampsAccepted);
					this.acceptedAmpsThisTick += ampsAccepted;
					return ampsAccepted;
				}
			}
		}
		return 0;
	}

	@Override
	public boolean canReceiveEnergy(@Nullable final Direction side) {
		return !this.canExtractEnergy(side) && this.getInputVoltage() > 0;
	}

	@Override
	public boolean canExtractEnergy(@Nullable final Direction side) {
		return this.outputVoltage > 0;
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
	public long getOverclockedInputVoltage() {
		if (this.canOverclock && this.getInputAmperage() >= 4) {
			return this.getInputVoltage() * 4;
		}
		return this.getInputVoltage();
	}

	@Override
	public long getOverclockedInputAmperage() {
		if (this.canOverclock && this.getInputAmperage() >= 4) {
			return this.getInputAmperage() / 4;
		}
		return this.getInputAmperage();
	}

	@Override
	public long getOverclockedOutputVoltage() {
		if (this.canOverclock && this.getOutputAmperage() > 4) {
			return this.getOutputVoltage() * 4;
		}
		return this.getOutputVoltage();
	}

	@Override
	public long getOverclockedOutputAmperage() {
		if (this.canOverclock && this.getOutputAmperage() > 4) {
			return this.getOutputAmperage() / 4;
		}
		return this.getOutputAmperage();
	}

	private static CapIO getIO(final long inputVoltage, final long inputAmperage, final long outputVoltage, final long outputAmperage) {
		final boolean input = inputVoltage > 0 && inputAmperage > 0;
		final boolean output = outputVoltage > 0 && outputAmperage > 0;
		return input && output ? CapIO.BOTH : input ? CapIO.IN : output ? CapIO.OUT : CapIO.NONE;
	}
}
