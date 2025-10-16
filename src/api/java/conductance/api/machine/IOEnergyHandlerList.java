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
	private final long inputVoltage;
	private final long inputAmperage;
	private final long outputVoltage;
	private final long outputAmperage;

	public IOEnergyHandlerList(final IEnergyHandler[] handlers, final CapIO io) {
		this.handlers = handlers;
		this.io = io;
		long totalInputVoltage = 0;
		long highestInputVoltage = 0;
		long totalOutputVoltage = 0;
		long highestOutputVoltage = 0;
		for (final IEnergyHandler handler : handlers) {
			totalInputVoltage += (handler.getInputVoltage() * handler.getInputAmperage());
			if (handler.getInputVoltage() > highestInputVoltage) {
				highestInputVoltage = handler.getInputVoltage();
			}
			totalOutputVoltage += (handler.getOutputVoltage() + handler.getOutputAmperage());
			if (handler.getOutputVoltage() > highestOutputVoltage) {
				highestOutputVoltage = handler.getOutputVoltage();
			}
		}
		this.inputVoltage = highestInputVoltage;
		this.inputAmperage = highestInputVoltage > 0 ? totalInputVoltage / highestInputVoltage : 0;
		this.outputVoltage = highestOutputVoltage;
		this.outputAmperage = highestOutputVoltage > 0 ? totalOutputVoltage / highestOutputVoltage : 0;
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
		long result = 0L;
		for (final IEnergyHandler handler : this.handlers) {
			result += handler.getEnergyStored();
		}
		return result;
	}

	@Override
	public long getEnergyCapacity() {
		long result = 0L;
		for (final IEnergyHandler handler : this.handlers) {
			result += handler.getEnergyCapacity();
		}
		return result;
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
}
