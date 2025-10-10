package conductance.api.machine;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.energy.IEnergyHandler;

public interface IDelegatedEnergyHandler extends IEnergyHandler {

	IEnergyHandler getRealEnergyHandler();

	@Override
	default long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
		return this.getRealEnergyHandler().receiveEnergy(receivingSide, volts, amps);
	}

	@Override
	default boolean canReceiveEnergy(@Nullable final Direction receivingSide) {
		return this.getRealEnergyHandler().canReceiveEnergy(receivingSide);
	}

	@Override
	default boolean canExtractEnergy(final @Nullable Direction extractingSide) {
		return this.getRealEnergyHandler().canExtractEnergy(extractingSide);
	}

	@SuppressWarnings("deprecation")
	@Override
	default long modifyEnergy(final long differenceAmount) {
		return this.getRealEnergyHandler().modifyEnergy(differenceAmount);
	}

	@Override
	default long getEnergyStored() {
		return this.getRealEnergyHandler().getEnergyStored();
	}

	@Override
	default long getEnergyCapacity() {
		return this.getRealEnergyHandler().getEnergyCapacity();
	}

	@Override
	default long getInputVoltage() {
		return this.getRealEnergyHandler().getInputVoltage();
	}

	@Override
	default long getInputAmperage() {
		return this.getRealEnergyHandler().getInputAmperage();
	}

	@Override
	default long getOutputAmperage() {
		return this.getRealEnergyHandler().getOutputAmperage();
	}

	@Override
	default long getOutputVoltage() {
		return this.getRealEnergyHandler().getOutputVoltage();
	}
}
