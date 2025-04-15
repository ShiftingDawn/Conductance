package conductance.api.capability.energy;

import java.util.List;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public class EnergyHandlerList implements IEnergyHandler {

	private final List<? extends IEnergyHandler> handlers;

	public EnergyHandlerList(final List<? extends IEnergyHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		long ampsUsed = 0L;
		for (final IEnergyHandler handler : this.handlers) {
			ampsUsed += handler.receiveEnergy(null, volts, amps);
			if (amps == ampsUsed) {
				return ampsUsed;
			}
		}
		return ampsUsed;
	}

	@Override
	public boolean canReceiveEnergy(@Nullable final Direction receivingSide) {
		return false;
	}

	@Override
	public boolean canExtractEnergy(@Nullable final Direction extractingSide) {
		return true;
	}

	@Override
	public long modifyEnergy(final long energyToAdd) {
		long energyAdded = 0L;
		for (final IEnergyHandler handler : this.handlers) {
			energyAdded += handler.modifyEnergy(energyToAdd - energyAdded);
			if (energyAdded == energyToAdd) {
				return energyAdded;
			}
		}
		return energyAdded;
	}

	@Override
	public long getEnergyStored() {
		long energyStored = 0L;
		for (final IEnergyHandler iEnergyHandler : this.handlers) {
			energyStored += iEnergyHandler.getEnergyStored();
		}
		return energyStored;
	}

	@Override
	public long getEnergyCapacity() {
		long energyCapacity = 0L;
		for (final IEnergyHandler iEnergyHandler : this.handlers) {
			energyCapacity += iEnergyHandler.getEnergyCapacity();
		}
		return energyCapacity;
	}

	@Override
	public long getInputAmperage() {
		return 1L;
	}

	@Override
	public long getOutputAmperage() {
		return 1L;
	}

	@Override
	public long getInputVoltage() {
		long inputVoltage = 0L;
		for (final IEnergyHandler container : this.handlers) {
			inputVoltage += container.getInputVoltage() * container.getInputAmperage();
		}
		return inputVoltage;
	}

	@Override
	public long getOutputVoltage() {
		long outputVoltage = 0L;
		for (final IEnergyHandler container : this.handlers) {
			outputVoltage += container.getOutputVoltage() * container.getOutputAmperage();
		}
		return outputVoltage;
	}
}
