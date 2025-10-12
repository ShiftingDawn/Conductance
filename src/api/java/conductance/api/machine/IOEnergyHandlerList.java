package conductance.api.machine;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.energy.IEnergyHandler;

public class IOEnergyHandlerList implements IEnergyHandler, ValueIOSerializable {

	private final IEnergyHandler[] handlers;
	private final @Getter CapIO io;

	public IOEnergyHandlerList(final IEnergyHandler[] handlers, final CapIO io) {
		this.handlers = handlers;
		this.io = io;
	}

	public IOEnergyHandlerList(final List<IEnergyHandler> handlers, final CapIO io) {
		this(handlers.toArray(IEnergyHandler[]::new), io);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		final ValueOutput.ValueOutputList list = valueOutput.childrenList("handlers");
		for (int i = 0; i < this.handlers.length; ++i) {
			final ValueOutput entry = list.addChild();
			entry.putInt("i", i);
			if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
				serializable.serialize(entry.child("data"));
			}
		}
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		valueInput.childrenList("handlers").ifPresent(list -> {
			for (final ValueInput entry : list) {
				final int i = entry.getIntOr("i", -1);
				if (i < 0 || i >= this.handlers.length) {
					continue;
				}
				if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
					entry.child("data").ifPresent(serializable::deserialize);
				}
			}
		});
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
		return this.io.isInput();
	}

	@Override
	public boolean canExtractEnergy(@Nullable final Direction extractingSide) {
		return this.io.isOutput();
	}

	@SuppressWarnings("deprecation")
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
