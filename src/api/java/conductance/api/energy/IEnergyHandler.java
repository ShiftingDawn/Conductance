package conductance.api.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IEnergyHandler {

	/**
	 * @param receivingSide The side the energy is received from
	 * @param volts         The voltage
	 * @param amps          The amperage
	 * @return the amount of accepted amps
	 */
	long receiveEnergy(@Nullable Direction receivingSide, long volts, long amps);

	boolean canReceiveEnergy(@Nullable Direction receivingSide);

	default boolean canExtractEnergy(@Nullable final Direction extractingSide) {
		return false;
	}

	/**
	 * @param differenceAmount
	 * @return the accepted difference amount
	 */
	@Deprecated
	long modifyEnergy(long differenceAmount);

	/**
	 * @param energyToAdd
	 * @return the added amount
	 */
	@Deprecated
	default long addEnergy(final long energyToAdd) {
		return this.modifyEnergy(energyToAdd);
	}

	/**
	 * @param energyToRemove
	 * @return the removed amount
	 */
	@Deprecated
	default long removeEnergy(final long energyToRemove) {
		return -this.modifyEnergy(-energyToRemove);
	}

	default long getEnergySpace() {
		return this.getEnergyCapacity() - this.getEnergyStored();
	}

	long getEnergyStored();

	long getEnergyCapacity();

	long getInputVoltage();

	long getInputAmperage();

	default long getOutputVoltage() {
		return 0L;
	}

	default long getOutputAmperage() {
		return 0L;
	}

	IEnergyHandler DEFAULT = new IEnergyHandler() {

		@Override
		public long receiveEnergy(@Nullable final Direction receivingSide, final long volts, final long amps) {
			return 0;
		}

		@Override
		public boolean canReceiveEnergy(@Nullable final Direction receivingSide) {
			return false;
		}

		@Override
		@Deprecated
		public long modifyEnergy(final long differenceAmount) {
			return 0;
		}

		@Override
		public long getEnergyStored() {
			return 0;
		}

		@Override
		public long getEnergyCapacity() {
			return 0;
		}

		@Override
		public long getInputAmperage() {
			return 0;
		}

		@Override
		public long getInputVoltage() {
			return 0;
		}
	};
}
