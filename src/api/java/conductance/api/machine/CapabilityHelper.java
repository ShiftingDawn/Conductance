package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCCapabilities;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.energy.IEnergyHandler;

public final class CapabilityHelper {

	public static Optional<IItemHandler> getItemHandlerCapability(final IMachineCapabilityHolder holder, @Nullable final Direction side) {
		final List<IItemHandlerModifiable> itemHandlers = new ArrayList<>();
		for (final MachineCapability capability : holder.getCapabilities().values()) {
			if (capability instanceof final IItemHandlerModifiable itemHandler && capability.isValid(side)) {
				itemHandlers.add(itemHandler);
			}
		}
		if (itemHandlers.isEmpty()) {
			return Optional.empty();
		} else if (itemHandlers.size() == 1) {
			return Optional.of(itemHandlers.getFirst());
		}
		final CapIO io = CapIO.BOTH;
		final IOItemHandlerList handlerList = new IOItemHandlerList(itemHandlers, io);
		return Optional.of(handlerList);
	}

	public static Optional<IFluidHandler> getFluidHandlerCapability(final IMachineCapabilityHolder holder, @Nullable final Direction side) {
		final List<IFluidHandler> fluidHandlers = new ArrayList<>();
		for (final MachineCapability capability : holder.getCapabilities().values()) {
			if (capability instanceof final IFluidHandler fluidHandler && capability.isValid(side)) {
				fluidHandlers.add(fluidHandler);
			}
		}
		if (fluidHandlers.isEmpty()) {
			return Optional.empty();
		} else if (fluidHandlers.size() == 1) {
			return Optional.of(fluidHandlers.getFirst());
		}
		final CapIO io = CapIO.BOTH;
		final IOFluidHandlerList handlerList = new IOFluidHandlerList(fluidHandlers, io);
		return Optional.of(handlerList);
	}

	public static Optional<IEnergyHandler> getEnergyHandlerCapability(final IMachineCapabilityHolder holder, @Nullable final Direction side) {
		final List<IEnergyHandler> energyHandlers = new ArrayList<>();
		for (final MachineCapability capability : holder.getCapabilities().values()) {
			if (capability instanceof final IEnergyHandler energyHandler && capability.isValid(side)) {
				energyHandlers.add(energyHandler);
			}
		}
		if (energyHandlers.isEmpty()) {
			return Optional.empty();
		} else if (energyHandlers.size() == 1) {
			return Optional.of(energyHandlers.getFirst());
		}
		final CapIO io = CapIO.BOTH;
		final IOEnergyHandlerList handlerList = new IOEnergyHandlerList(energyHandlers, io);
		return Optional.of(handlerList);
	}

	public static void tryImportItems(final IItemHandler destination, final Level level, final BlockPos sourcePos, final @Nullable Direction sourceSide) {
		final IItemHandler source = level.getCapability(Capabilities.ItemHandler.BLOCK, sourcePos, sourceSide);
		if (source != null) {
			CapabilityHelper.tryTransferItems(source, destination);
		}
	}

	public static void tryExportItems(final IItemHandler source, final Level level, final BlockPos destinationPos, final @Nullable Direction destinationSide) {
		final IItemHandler destination = level.getCapability(Capabilities.ItemHandler.BLOCK, destinationPos, destinationSide);
		if (destination != null) {
			CapabilityHelper.tryTransferItems(source, destination);
		}
	}

	public static void tryTransferItems(final IItemHandler source, final IItemHandler destination) {
		for (int i = 0; i < source.getSlots(); ++i) {
			ItemStack stack = source.extractItem(i, Integer.MAX_VALUE, true);
			if (stack.isEmpty()) {
				continue;
			}
			final ItemStack leftOver = ItemHandlerHelper.insertItemStacked(destination, stack, true);
			final int inserted = stack.getCount() - leftOver.getCount();
			if (inserted > 0) {
				stack = source.extractItem(i, inserted, false);
				ItemHandlerHelper.insertItemStacked(destination, stack, false);
			}
		}
	}

	public static void tryImportFluids(final IFluidHandler destination, final Level level, final BlockPos sourcePos, final @Nullable Direction sourceSide) {
		final IFluidHandler source = level.getCapability(Capabilities.FluidHandler.BLOCK, sourcePos, sourceSide);
		if (source != null) {
			CapabilityHelper.tryTransferFluids(source, destination);
		}
	}

	public static void tryExportFluids(final IFluidHandler source, final Level level, final BlockPos destinationPos, final @Nullable Direction destinationSide) {
		final IFluidHandler destination = level.getCapability(Capabilities.FluidHandler.BLOCK, destinationPos, destinationSide);
		if (destination != null) {
			CapabilityHelper.tryTransferFluids(source, destination);
		}
	}

	public static void tryTransferFluids(final IFluidHandler source, final IFluidHandler destination) {
		FluidStack stack = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
		if (stack.isEmpty()) {
			return;
		}
		final int accepted = destination.fill(stack, IFluidHandler.FluidAction.SIMULATE);
		if (accepted > 0) {
			stack = source.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
			destination.fill(stack.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
		}
	}

	public static void tryExportEnergy(final IEnergyHandler source, final Level level, final BlockPos destinationPos, final @Nullable Direction destinationSide) {
		if (source.getOutputVoltage() <= 0 || source.getOutputAmperage() <= 0 || source.getEnergyStored() < source.getOutputVoltage()) {
			return;
		}
		final IEnergyHandler destination = NCCapabilities.getEnergyHandler(level, destinationPos, destinationSide);
		if (destination == null || !destination.canReceiveEnergy(destinationSide)) {
			return;
		}
		final long maxAmps = Math.min(source.getOutputAmperage(), source.getEnergyStored() / source.getOutputVoltage());
		if (maxAmps <= 0) {
			return;
		}
		final long acceptedAmps = destination.receiveEnergy(destinationSide, source.getOutputVoltage(), source.getOutputAmperage());
		if (acceptedAmps > 0) {
			source.removeEnergy(acceptedAmps * source.getOutputVoltage());
		}
	}

	private CapabilityHelper() {
	}
}
